package VismaTask.VismaTask.service;

import VismaTask.VismaTask.DTO.MeetingDTO;
import VismaTask.VismaTask.model.Meeting;
import VismaTask.VismaTask.model.Participant;
import VismaTask.VismaTask.repository.MeetingRepositoryImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    private final MeetingRepositoryImpl meetingRepository = new MeetingRepositoryImpl();
    DateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ResponseEntity<String> removePersonFromMeeting(int meetingId, String emailAddress) {
        Meeting meeting = meetingRepository.findById(meetingId);

        if (meeting == null)
            return new ResponseEntity<>("Meeting with this ID has not been found", HttpStatus.NOT_FOUND);

        for (Participant participant : meeting.getParticipants()) {
            String responsiblePerson = meeting.getResponsiblePerson();
            if (emailAddress.equals(responsiblePerson)) {
                return new ResponseEntity<>("Cannot remove a responsible person", HttpStatus.CONFLICT);
            }
            if (participant.getEmailAddress().equals(emailAddress)) {
                meeting.getParticipants().remove(participant);
                meetingRepository.update(meeting);
                return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Could not find a participant in this meeting", HttpStatus.CONFLICT);
    }

    public ResponseEntity<String> addPersonToMeeting(int meetingId, String emailAddress) throws ParseException {
        List<Meeting> allMeetings = meetingRepository.findAll();
        Meeting meeting = meetingRepository.findById(meetingId);

        if (meeting == null)
            return new ResponseEntity<>("Meeting with this ID has not been found", HttpStatus.NOT_FOUND);

        if (meeting.hasParticipant(emailAddress))
            return new ResponseEntity<>("Person has already been added to this meeting", HttpStatus.CONFLICT);

        Date date = new Date();
        meeting.getParticipants().add(new Participant(longDateFormat.format(date), emailAddress));
        meetingRepository.update(meeting);
        if (isPersonBusyDuringMeeting(emailAddress, meeting.getStartDate(), meeting.getEndDate(), meetingId, allMeetings)) {
            return new ResponseEntity<>("WARNING: this meeting overlaps with another meeting this person is participating in", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Added successfully", HttpStatus.OK);
        }
    }

    private Boolean isPersonBusyDuringMeeting(String emailAddress, String startDateTime, String endDateTime, int meetingId, List<Meeting> allMeetings) throws ParseException {
        allMeetings = meetingRepository.findAll();
        Date startDate = longDateFormat.parse(startDateTime);
        Date endDate = longDateFormat.parse(endDateTime);
        for (Meeting meeting : allMeetings) {
            if (meeting.getId() == meetingId)
                continue;
            if (!meeting.hasParticipant(emailAddress))
                continue;
            Date currStartDate = longDateFormat.parse(meeting.getStartDate());
            Date currEndDate = longDateFormat.parse(meeting.getEndDate());
            if (checkIfDatesOverlap(startDate, endDate, currStartDate, currEndDate)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIfDatesOverlap(Date startDate, Date endDate, Date currStartDate, Date currEndDate) {
        return (currStartDate.compareTo(startDate) >= 0 && currStartDate.compareTo(endDate) < 0) || (currEndDate.compareTo(startDate) > 0 && currEndDate.compareTo(endDate) <= 0);
    }

    public ResponseEntity<String> deleteMeeting(int meetingId, String authToken) {
        List<Meeting> allMeetings = meetingRepository.findAll();
        Meeting meeting = meetingRepository.findById(meetingId);
        if (meeting == null)
            return new ResponseEntity<>("Meeting not found", HttpStatus.NOT_FOUND);

        if (!authToken.contains("Basic"))
            return new ResponseEntity<>("Authorization failed", HttpStatus.UNAUTHORIZED);

        String emailAddress = new String(Base64.getDecoder().decode(authToken.split(" ")[1])).split(":")[0]; // I am aware this is not at all a secure way to authorize a user. It would be better to use something like JWT
        if (!meeting.getResponsiblePerson().equals(emailAddress)) {
            return new ResponseEntity<>("You are not responsible for this meeting and cannot delete it", HttpStatus.UNAUTHORIZED);
        } else {
            allMeetings.remove(meeting);
            meetingRepository.saveAll(allMeetings);
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> createMeeting(Meeting meeting) {
        String category = meeting.getCategory();
        if (!category.equals("CodeMonkey") && !category.equals("Hub") && !category.equals("Short") && !category.equals("TeamBuilding"))
            return new ResponseEntity<>("Category is not correct", HttpStatus.BAD_REQUEST);

        String type = meeting.getType();
        if (!type.equals("Live") && !type.equals("InPerson"))
            return new ResponseEntity<>("Type is not correct", HttpStatus.BAD_REQUEST);

        List<Meeting> allMeetings = meetingRepository.findAll();
        for (Meeting checkMeeting : allMeetings) {
            if (checkMeeting.getId() == meeting.getId())
                return new ResponseEntity<>("Meeting with this ID already exists", HttpStatus.BAD_REQUEST);
        }

        String responsiblePerson = meeting.getResponsiblePerson();
        Date date = new Date();
        Participant responsiblePersonParticipant = new Participant(responsiblePerson, longDateFormat.format(date));
        meeting.getParticipants().add(responsiblePersonParticipant);
        meetingRepository.save(meeting);
        return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
    }

    public List<MeetingDTO> getAllMeetings(String description, String responsiblePerson, String category, String meetingType, String startDate, String endDate, String minAttendeesStr, String maxAttendeesStr) {
        Gson gson = new Gson();
        List<Meeting> filteredMeetings;
        try {
            filteredMeetings = filterMeetings(description, responsiblePerson, category, meetingType, startDate, endDate, minAttendeesStr, maxAttendeesStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return filteredMeetings.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private MeetingDTO convertEntityToDto(Meeting meeting) {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(meeting.getId());
        meetingDTO.setDescription(meeting.getDescription());
        meetingDTO.setCategory(meeting.getCategory());
        meetingDTO.setName(meeting.getName());
        meetingDTO.setParticipants(meeting.getParticipants());
        meetingDTO.setStartDate(meeting.getStartDate());
        meetingDTO.setEndDate(meeting.getEndDate());
        meetingDTO.setType(meeting.getType());
        meetingDTO.setResponsiblePerson(meeting.getResponsiblePerson());
        return meetingDTO;
    }

    private List<Meeting> filterMeetings(String description, String responsiblePerson, String category, String meetingType, String startDate, String endDate, String minAttendeesStr, String maxAttendeesStr) throws ParseException {
        List<Meeting> filteredMeetings = meetingRepository.findAll();
        filterMeetingsByDescription(description, filteredMeetings);
        filterMeetingsByResponsiblePerson(responsiblePerson, filteredMeetings);
        filterMeetingsByCategory(category, filteredMeetings);
        filterMeetingsByType(meetingType, filteredMeetings);
        filterMeetingsByDate(startDate, endDate, filteredMeetings);
        filterMeetingsByAttendeesNumber(minAttendeesStr, maxAttendeesStr, filteredMeetings);

        return filteredMeetings;
    }

    private static void filterMeetingsByType(String meetingType, List<Meeting> filteredMeetings) {
        if (meetingType != null)
            filteredMeetings.removeIf(meeting -> !meeting.getType().equals(meetingType));
    }

    private static void filterMeetingsByCategory(String category, List<Meeting> filteredMeetings) {
        if (category != null)
            filteredMeetings.removeIf(meeting -> !meeting.getCategory().equals(category));
    }

    private static void filterMeetingsByResponsiblePerson(String responsiblePerson, List<Meeting> filteredMeetings) {
        if (responsiblePerson != null)
            filteredMeetings.removeIf(meeting -> !meeting.getResponsiblePerson().equals(responsiblePerson));
    }

    private static void filterMeetingsByDescription(String description, List<Meeting> filteredMeetings) {
        if (description != null)
            filteredMeetings.removeIf(meeting -> !meeting.getName().contains(description));
    }

    private void filterMeetingsByAttendeesNumber(String minAttendeesStr, String maxAttendeesStr, List<Meeting> filteredMeetings) {
        if (minAttendeesStr != null && maxAttendeesStr != null) {
            int minAttendees = tryParseInt(minAttendeesStr, Integer.MAX_VALUE);
            int maxAttendees = tryParseInt(maxAttendeesStr, 0);
            filteredMeetings.removeIf(meeting -> (meeting.getParticipants().stream().count() < minAttendees && meeting.getParticipants().stream().count() > maxAttendees));
        } else if (minAttendeesStr != null) {
            int minAttendees = tryParseInt(minAttendeesStr, Integer.MAX_VALUE);
            filteredMeetings.removeIf(meeting -> meeting.getParticipants().stream().count() < minAttendees);
        } else if (maxAttendeesStr != null) {
            int maxAttendees = tryParseInt(maxAttendeesStr, 0);
            filteredMeetings.removeIf(meeting -> meeting.getParticipants().stream().count() > maxAttendees);
        }
    }

    private void filterMeetingsByDate(String startDate, String endDate, List<Meeting> filteredMeetings) throws ParseException {
        if (startDate != null && endDate != null) {
            Date start = shortDateFormat.parse(startDate);
            Date end = shortDateFormat.parse(endDate);
            filteredMeetings.removeIf(meeting -> {
                try {
                    return (longDateFormat.parse(meeting.getStartDate()).compareTo(end) > 0 || longDateFormat.parse(meeting.getEndDate()).compareTo(start) < 0);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (startDate != null) {
            Date start = shortDateFormat.parse(startDate);
            filteredMeetings.removeIf(meeting -> {
                try {
                    return longDateFormat.parse(meeting.getStartDate()).compareTo(start) < 0;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (endDate != null) {
            Date end = shortDateFormat.parse(endDate);
            filteredMeetings.removeIf(meeting -> {
                try {
                    return longDateFormat.parse(meeting.getEndDate()).compareTo(end) > 0;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public int tryParseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
