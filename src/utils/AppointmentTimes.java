package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Class to handle appointment times and converting times.
 * @author Kenly Berkowitz
 */
public class AppointmentTimes {

    private LocalDate localDate = null;
    private ArrayList<LocalDateTime> takenStartTimes = new ArrayList<>();
    private ArrayList<LocalDateTime> takenEndTimes = new ArrayList<>();
    private ArrayList<ZonedDateTime> takenStartTimesEST = new ArrayList<>();
    private ArrayList<ZonedDateTime> takenEndTimesEST = new ArrayList<>();
    private final ZoneId ESTzone = ZoneId.of("America/New_York");
    private final ZoneId defaultZone = ZoneId.of(TimeZone.getDefault().getID());
    private ObservableList<ZonedDateTime> availableStartTimeList = FXCollections.observableArrayList();
    private ObservableList<ZonedDateTime> availableEndTimeList = FXCollections.observableArrayList();

    /**
     *
     * @return LocalDate localDate
     */
    public LocalDate getLocalDate() {
        return localDate;
    }

    /**
     *
     * @param localDate Sets LocalDate localDate
     * @throws SQLException SQL error thrown in setLists
     */
    public void setLocalDate(LocalDate localDate) throws SQLException {
        this.localDate = localDate;

        clearLists();
        setLists();
    }

    /**
     *
     * @param localDate Sets localDate
     * @param startTime Sets startTime
     * @param endTime Sets endTime
     * @throws SQLException SQL error thrown in setLists
     */
    public void setLocalDate(LocalDate localDate, LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        this.localDate = localDate;

        clearLists();
        setLists(startTime,endTime);
    }

    /**
     *
     * @return ArrayList of LocalDateTime objects; takenStartTimes
     */
    public ArrayList<LocalDateTime> getTakenStartTimes() {
        return takenStartTimes;
    }

    /**
     *
     * @param takenStartTimes set ArrayList of LocalDateTime objects; takenStartTimes
     */
    public void setTakenStartTimes(ArrayList<LocalDateTime> takenStartTimes) {
        this.takenStartTimes = takenStartTimes;
    }

    //return LocalDateTime from converted ZonedDateTime
    /**
     *
     * @return ObservableList of LocalDateTime objects.
     */
    public ObservableList<LocalDateTime> getAvailableStartTimeList() {
        return convertToLocalDateTime(availableStartTimeList);
    }

    /**
     *
     * @param availableStartTimeList Sets List with object type ZonedDateTime.
     */
    public void setAvailableStartTimeList(ObservableList<ZonedDateTime> availableStartTimeList) {
        this.availableStartTimeList = availableStartTimeList;
    }

    //return LocalDateTime from converted ZonedDateTime
    /**
     *
     * @return ObservableList of LocalDateTime objects.
     */
    public ObservableList<LocalDateTime> getAvailableEndTimeList() {
        return convertToLocalDateTime(availableEndTimeList);
    }

    /**
     *
     * @param availableEndTimeList Sets List with object type ZonedDateTime.
     */
    public void setAvailableEndTimeList(ObservableList<ZonedDateTime> availableEndTimeList) {
        this.availableEndTimeList = availableEndTimeList;
    }

    /**
     *
     * @return ArrayList of LocalDateTime objects; takenEndTimes
     */
    public ArrayList<LocalDateTime> getTakenEndTimes() {
        return takenEndTimes;
    }

    /**
     *
     * @param takenEndTimes set ArrayList of LocalDateTime objects; takenEndTimes
     */
    public void setTakenEndTimes(ArrayList<LocalDateTime> takenEndTimes) {
        this.takenEndTimes = takenEndTimes;
    }

    /**
     *
     * @return ArrayList of ZonedDateTime objects; takenStartTimesEST
     */
    public ArrayList<ZonedDateTime> getTakenStartTimesEST() {
        return takenStartTimesEST;
    }

    /**
     *
     * @param takenStartTimesEST set ArrayList of ZonedDateTime objects; takenStartTimesEST
     */
    public void setTakenStartTimesEST(ArrayList<ZonedDateTime> takenStartTimesEST) {
        this.takenStartTimesEST = takenStartTimesEST;
    }

    /**
     *
     * @return takenEndTimesEST
     */
    public ArrayList<ZonedDateTime> getTakenEndTimesEST() {
        return takenEndTimesEST;
    }

    /**
     *
     * @param takenEndTimesEST Sets takenEndTimesEST.
     */
    public void setTakenEndTimesEST(ArrayList<ZonedDateTime> takenEndTimesEST) {
        this.takenEndTimesEST = takenEndTimesEST;
    }

    /**
     *
     * @throws SQLException Error thrown by getAppointmentTimes methods
     */
    private void setLists() throws SQLException {
        //gets appt start and endtimes in the database
        takenStartTimes = DBApptQuery.getAppointmentSartTimes(localDate);
        takenEndTimes = DBApptQuery.getAppointmentEndTimes(localDate);

        if (!takenStartTimes.isEmpty()) {
            //convert above array list of type LocalDateTime to ZonedDateTime EST
            for (int i = 0; i < takenStartTimes.size(); ++i) {
                takenStartTimesEST.add(ZonedDateTime.of(takenStartTimes.get(i), defaultZone).withZoneSameInstant(ESTzone));
                takenEndTimesEST.add(ZonedDateTime.of(takenEndTimes.get(i), defaultZone).withZoneSameInstant(ESTzone));
            }

        }
        initStartEndLists();
        filterTakenTimes();

    }

    /**
     *
     * @param startTime Sets lists based on startTime and EndTime
     * @param endTime Sets lists based on startTime and EndTime
     * @throws SQLException error by getAppointmentSartTimes
     */
    private void setLists(LocalDateTime startTime, LocalDateTime endTime) throws SQLException {
        //gets appt start and endtimes in the database
        takenStartTimes = DBApptQuery.getAppointmentSartTimes(localDate);
        takenEndTimes = DBApptQuery.getAppointmentEndTimes(localDate);

        for(LocalDateTime localDateTime: takenStartTimes) {

            if(localDateTime.equals(startTime)) {

                takenStartTimes.remove(localDateTime);
                break;
            }
        }

        for(LocalDateTime localDateTime: takenEndTimes) {
            if(localDateTime.equals(endTime)) {
                takenEndTimes.remove(localDateTime);
                break;
            }
        }


        if (!takenStartTimes.isEmpty()) {
            //convert above array list of type LocalDateTime to ZonedDateTime EST
            for (int i = 0; i < takenStartTimes.size(); ++i) {
                takenStartTimesEST.add(ZonedDateTime.of(takenStartTimes.get(i), defaultZone).withZoneSameInstant(ESTzone));
                takenEndTimesEST.add(ZonedDateTime.of(takenEndTimes.get(i), defaultZone).withZoneSameInstant(ESTzone));
            }

        }
        initStartEndLists();
        filterTakenTimes();

    }

    //UtilityFunctions
    //Creates available times in ZonedDateTime EST

    /**
     * Initializes availableTimeLists to hours based on EST 8am-10pm.
     */
    private void initStartEndLists() {
        int hour = 8;

        while (hour < 23) {
            availableStartTimeList.add(ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 0, 0, 0, ESTzone));
            if(!(hour == 22))
                availableStartTimeList.add(ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 30, 0, 0, ESTzone));
            hour++;
        }
        hour = 8;

        while (hour < 23) {
            availableEndTimeList.add(ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 0, 0, 0, ESTzone));
            if(!(hour == 22))
                availableEndTimeList.add(ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), hour, 30, 0, 0, ESTzone));
            hour++;
        }
    }

    //Filters out taken times in ZonedDateTime EST
    /**
     * Filter the available times by removing the takenTimes in the availableTimes list.
     */
    private void filterTakenTimes() {

        if(!takenStartTimesEST.isEmpty()) {
            for (int i = 0; i < takenStartTimesEST.size(); ++i) {
                for (int j = 0; j < availableStartTimeList.size(); ++j) {
                    if (takenStartTimesEST.get(i).equals(availableStartTimeList.get(j))) {
                        do {
                            availableStartTimeList.remove(j);
                        } while (!(availableStartTimeList.get(j).isEqual(takenEndTimesEST.get(i))));
                    }
                }
            }
            for (int i = takenEndTimesEST.size(); i > 0 ; i--) {
                for (int j = availableEndTimeList.size(); j > 0; j--) {
                    if (takenEndTimesEST.get(i - 1).equals(availableEndTimeList.get(j - 1))) {
                        int k = j -1;
                        do {
                            availableEndTimeList.remove(k);
                            k--;
                            if(k == 0)
                                break;
                        } while (!(availableEndTimeList.get(k).isEqual(takenStartTimesEST.get(i - 1))));
                    }
                }
            }
        }
    }

    /**
     *
     * @param localDateTime Passed in chosen time in order to filter out the endTimeList
     * @return return new list of endTimes
     */
    public ObservableList<LocalDateTime> filterEndTimesBasedOnStartTime(LocalDateTime localDateTime) {
        ObservableList<ZonedDateTime> tempZDTList = FXCollections.observableArrayList();
        tempZDTList.setAll(availableEndTimeList);
        ZonedDateTime tempZoneDateTime = ZonedDateTime.of(localDateTime, defaultZone).withZoneSameInstant(ESTzone);
        ZonedDateTime removeZDTAfter = null;

        tempZDTList.removeIf(tempZoneDateTime::isAfter);
        tempZDTList.removeIf(tempZoneDateTime::equals);

        for (int i = 0; i < takenStartTimesEST.size(); i++) {
            if(takenStartTimesEST.get(i).isAfter(tempZoneDateTime)) {
                System.out.println("Taken time: " + takenStartTimesEST.get(i));
                removeZDTAfter = takenStartTimesEST.get(i);
                break;
            }
        }

        if (removeZDTAfter != null)
            tempZDTList.removeIf(removeZDTAfter::isBefore);

        return convertToLocalDateTime(tempZDTList);
    }

    /**
     *
     * @param list Passes in an Observable List in order to convert it to localDateTime.
     * @return Returns new list in LocalDateTime.
     */
    private ObservableList<LocalDateTime> convertToLocalDateTime(ObservableList<ZonedDateTime> list) {
        ObservableList<LocalDateTime> tempLocalDateTimeObject = FXCollections.observableArrayList();
        for (ZonedDateTime zonedDateTime : list)
            tempLocalDateTimeObject.add(zonedDateTime.withZoneSameInstant(defaultZone).toLocalDateTime());

        return tempLocalDateTimeObject;
    }

    /**
     * Clears all lists.
     */
    private void clearLists() {
        takenStartTimes.clear();
        takenEndTimes.clear();
        takenStartTimesEST.clear();
        takenEndTimesEST.clear();
        availableStartTimeList.clear();
        availableEndTimeList.clear();
    }
}


