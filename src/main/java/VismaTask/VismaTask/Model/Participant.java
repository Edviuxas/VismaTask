package VismaTask.VismaTask.Model;

public class Participant {
    private String dateAdded;
    private String emailAddress;

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Participant(String emailAddress, String dateAdded) {
        this.emailAddress = emailAddress;
        this.dateAdded = dateAdded;
    }
}
