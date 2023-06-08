package VismaTask.VismaTask.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Meeting {
    private int id;
    private String name, description, category, type, startDate, endDate;
    private String responsiblePerson;
    private ArrayList<Participant> participants;

    public Meeting(String name, String description, String category, String type, String responsiblePerson, String startDate, String endDate, int id) throws ParseException {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.responsiblePerson = responsiblePerson;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.participants = new ArrayList<Participant>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", responsiblePerson='" + responsiblePerson + '\'' +
                ", participants=" + participants +
                '}';
    }

    public Boolean hasParticipant(String emailAddress) {
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            if (participant.getEmailAddress().equals(emailAddress))
                return true;
        }
        return false;
    }
}
