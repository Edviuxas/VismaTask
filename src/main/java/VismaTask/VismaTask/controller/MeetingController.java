package VismaTask.VismaTask.controller;

import VismaTask.VismaTask.DTO.MeetingDTO;
import VismaTask.VismaTask.model.Meeting;
import VismaTask.VismaTask.service.MeetingService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.*;

@RestController
public class MeetingController
{
    @Autowired
    MeetingService meetingService;
    Gson gson = new Gson();

    @RequestMapping(value = "/meetings", method = RequestMethod.GET)
    public List<MeetingDTO> getMeetings(@RequestParam(value = "desc", required = false) String description,
                                        @RequestParam(value = "resp", required = false) String responsiblePerson,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "type", required = false) String meetingType,
                                        @RequestParam(value = "start_date", required = false) String startDate,
                                        @RequestParam(value = "end_date", required = false) String endDate,
                                        @RequestParam(value = "min_attendees", required = false) String minAttendeesStr,
                                        @RequestParam(value = "max_attendees", required = false) String maxAttendeesStr) {
        return meetingService.getAllMeetings(description, responsiblePerson, category, meetingType, startDate, endDate, minAttendeesStr, maxAttendeesStr);
    }

    @RequestMapping(value = "/meetings", method = RequestMethod.POST)
    public ResponseEntity<String> createMeeting(@RequestBody Meeting meeting) {
        return meetingService.createMeeting(meeting);
    }
    @RequestMapping(value = "/meetings/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMeeting(@PathVariable("id") int meetingId, @RequestHeader("Authorization") String authToken) {
        return meetingService.deleteMeeting(meetingId, authToken);
    }

    @RequestMapping(value = "/meetings/{id}/person", method = RequestMethod.POST)
    public ResponseEntity<String> addPersonToMeeting(@PathVariable("id") int meetingId, @RequestBody String body) throws ParseException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(body, Properties.class);
        String emailAddress = data.getProperty("emailAddress");

        return meetingService.addPersonToMeeting(meetingId, emailAddress);
    }

    @RequestMapping(value = "/meetings/{id}/person", method = RequestMethod.DELETE)
    public ResponseEntity<String> removePersonFromMeeting(@PathVariable("id") int meetingId, @RequestBody String body) {
        Properties data = gson.fromJson(body, Properties.class);
        String emailAddress = data.getProperty("emailAddress");
        return meetingService.removePersonFromMeeting(meetingId, emailAddress);
    }
}