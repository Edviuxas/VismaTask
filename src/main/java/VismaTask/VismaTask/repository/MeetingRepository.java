package VismaTask.VismaTask.repository;

import VismaTask.VismaTask.model.Meeting;

import java.util.Collection;
import java.util.List;

public interface MeetingRepository {
    void save(Meeting meeting);
    void saveAll(List<Meeting> allMeetings);
    void update(Meeting meeting);
    Collection<Meeting> findAll();
    Meeting findById(int id);
}
