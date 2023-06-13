package VismaTask.VismaTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meeting {
    private int id;
    private String name, description, category, type, startDate, endDate;
    private String responsiblePerson;
    private List<Participant> participants = new ArrayList<>();

    public Boolean hasParticipant(String emailAddress) {
        for (Participant participant : participants) {
            if (participant.getEmailAddress().equals(emailAddress))
                return true;
        }
        return false;
    }
}
