package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Appointment model class.
 * @author Kenly Berkowitz
 */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String contactName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int userID;
    private int custID;
    private int contactID;
    //converted dateTimes for appointments TableView
    private String startTimeTable;
    private String endTimeTable;

    /**
     *
     * @param appointmentID Passed int
     * @param title Passed String
     * @param description Passed String
     * @param location Passed String
     * @param type Passed String
     * @param contactName Passed String
     * @param startTime Passed LocalDateTime
     * @param endTime Passed LocalDateTime
     * @param userID Passed int
     * @param custID Passed int
     * @param contactID Passed int
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, String contactName, LocalDateTime startTime, LocalDateTime endTime, int userID, int custID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.contactName = contactName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userID = userID;
        this.custID = custID;
        this.contactID = contactID;
        formatLocalTime();  //formats the time for the table
    }

    /**
     *
     * @return String contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName Passed string
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return int userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID int userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return int contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID int contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     *
     * @return int appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     *
     * @param appointmentID int appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     *
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title Passed string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description Passed string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return String location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location Passed string
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type Passed string
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return localDateTime startTime
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime LocalDateTime startTime
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return LocalDateTime endTime
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime LocalDateTime endTime
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return int custID
     */
    public int getCustID() {
        return custID;
    }

    /**
     *
     * @param custID int custID
     */
    public void setCustID(int custID) {
        this.custID = custID;
    }

    /**
     *
     * @return String startTimeTable
     */
    public String getStartTimeTable() {
        return startTimeTable;
    }

    /**
     *
     * @param startTimeTable Passed string
     */
    public void setStartTimeTable(String startTimeTable) {
        this.startTimeTable = startTimeTable;
    }

    /**
     *
     * @return String endTimeTable
     */
    public String getEndTimeTable() {
        return endTimeTable;
    }

    /**
     *
     * @param endTimeTable Passed string
     */
    public void setEndTimeTable(String endTimeTable) {
        this.endTimeTable = endTimeTable;
    }

    /**
     * Formats local time for tables.
     */
    private void formatLocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy    HH:mm:ss");
        startTimeTable = startTime.format(formatter);
        endTimeTable = endTime.format(formatter);
    }
}
