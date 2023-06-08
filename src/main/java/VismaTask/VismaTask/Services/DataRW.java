package VismaTask.VismaTask.Services;

import VismaTask.VismaTask.Model.Meeting;
import VismaTask.VismaTask.VismaTaskApplication;
import com.google.gson.*;
import com.google.gson.JsonArray;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataRW {
    public static String fileName = "meetings.json";
    public static void writeMeetings(ArrayList<Meeting> meetings) {
        Gson gson = new Gson();
        JsonArray meetingsJSONArray = new JsonArray();
        for (Meeting meeting : meetings) {
            meetingsJSONArray.add(gson.toJsonTree(meeting));
        }
        try (PrintWriter out = new PrintWriter(VismaTaskApplication.class.getClassLoader().getResource(fileName).getFile())) {
            out.write(meetingsJSONArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Meeting> getMeetings() {
//        System.out.println("GETTING MEETINGS FROM DATARW");
        Gson gson = new Gson();
        ArrayList<Meeting> allMeetings = new ArrayList<>();
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
}
