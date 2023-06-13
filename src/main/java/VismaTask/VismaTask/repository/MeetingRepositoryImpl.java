package VismaTask.VismaTask.repository;

import VismaTask.VismaTask.VismaTaskApplication;
import VismaTask.VismaTask.model.Meeting;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MeetingRepositoryImpl implements MeetingRepository {
    public String fileName = "meetings.json";
    Gson gson = new Gson();

    @Override
    public void save(Meeting meeting) {
        List<Meeting> allMeetings = findAll();
        allMeetings.add(meeting);
        saveAll(allMeetings);
    }

    @Override
    public void saveAll(List<Meeting> allMeetings) {
        try (PrintWriter out = new PrintWriter(VismaTaskApplication.class.getClassLoader().getResource(fileName).getFile())) {
            out.write(gson.toJson(allMeetings));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Meeting meeting) {
        List<Meeting> allMeetings = findAll();
        for (int i = 0; i < allMeetings.size(); i++) {
            Meeting singleMeeting = allMeetings.get(i);
            if (singleMeeting.getId() == meeting.getId()) {
                allMeetings.set(i, meeting);
            }
        }
        saveAll(allMeetings);
    }

    @Override
    public List<Meeting> findAll() {
        List<Meeting> allMeetings = new ArrayList<>();
        File file = new File(VismaTaskApplication.class.getClassLoader().getResource(fileName).getFile());
        if (file.exists()) {
//            System.out.println("FILE EXISTS");
            JsonElement json = JsonParser.parseReader( new InputStreamReader(VismaTaskApplication.class.getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8));
            JsonArray jsonArray = json.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                allMeetings.add(gson.fromJson(jsonArray.get(i), Meeting.class));
            }
        }
        return allMeetings;
    }

    @Override
    public Meeting findById(int id) {
        List<Meeting> allMeetings = findAll();
        for (Meeting meeting : allMeetings) {
            if (meeting.getId() == id)
                return meeting;
        }
        return null;
    }
}
