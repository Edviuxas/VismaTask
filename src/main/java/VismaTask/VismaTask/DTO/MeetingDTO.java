package VismaTask.VismaTask.DTO;

import VismaTask.VismaTask.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDTO {
    private int id;
    private String name, description, category, type, startDate, endDate;
    private String responsiblePerson;
    private List<Participant> participants = new ArrayList<>();
}
