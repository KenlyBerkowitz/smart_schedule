package utils;

import controller.PrimaryViewController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Kenly Berkowitz
 */
public class RunnableNextAppt implements Runnable{

    public static boolean primaryViewHasLoaded = false;
    private static Appointment nextAppointment = null;
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static boolean apptWithin15Minutes = false;
    public static int minutedUntilAppt;
    public static boolean terminateRun = false;

    public RunnableNextAppt() throws SQLException {
       setApptListAndNextAppt();

    }

    /**
     * Runs the thread that retrieves next appointment form DB and lets PrimaryView controller that their is an appointment coming up.
     */
    @Override
    public void run() {

        while (!terminateRun) {
            if (nextAppointment != null && nextAppointment.getStartTime().isAfter(LocalDateTime.now())) {
                boolean flag = true;

                while (flag && (!terminateRun)) {
                    long minutesTil = minutesUnitlNextAppt();
                    setAlert();

                    if ( minutesTil <= 15 && minutesTil >= 0) {
                        apptWithin15Minutes = true;
                        setAlert();
                        minutedUntilAppt = (int) minutesTil;
                    }
                    else if ( minutesTil < 0) {
                        apptWithin15Minutes = false;
                        setAlert();
                        flag = false;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            nextAppointment = null;

           try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }
    }

    /**
     * Gets the next appointment.
     */
    public static void setNextAppointment()  {
        //checks if the list uis empty and then gets the first appointment in the list.
        if (!appointmentList.isEmpty()) {
            for(int i = 0; i < appointmentList.size(); i++) {
                if(appointmentList.get(i).getStartTime().isAfter(LocalDateTime.now())) {
                    nextAppointment = appointmentList.get(i);
                    break;
                }
            }
        }
    }

    /**
     * Sets the appointments by retrieving from DB.
     * @throws SQLException Error thrown if query exception.
     */
    public static void setAppointmentList() throws SQLException {
        appointmentList = DBApptQuery.getApptByDay();

    }

    /**
     * Sets Appointment Lists and the next appointment.
     * @throws SQLException Error thrown if query exception.
     */
    public static void setApptListAndNextAppt() throws SQLException {
        setAppointmentList();
        setNextAppointment();
    }

    /**
     * Gets minutes until next appointment.
     * @return long minutes until the next appointment.
     */
    public static long minutesUnitlNextAppt() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        System.out.println(nextAppointment.getStartTime());

        return ChronoUnit.MINUTES.between(localDateTime, nextAppointment.getStartTime());

    }

    /**
     * Sets the alert in PrimaryView Controller.
     */
    public void setAlert() {
        if(apptWithin15Minutes) {
            if(nextAppointment != null) {
                PrimaryViewController.nextAppt = nextAppointment;
                Platform.runLater(() -> {
                    PrimaryViewController.apptWithinFifteen.set(true);
                });
            }
        }
        else {
            Platform.runLater(() -> {
                PrimaryViewController.apptWithinFifteen.set(false);
            });
        }
    }

    /**
     * Gets the next appointment.
     * @return Appointment nextAppointment.
     */
    public static Appointment getNextAppointment() {
        return nextAppointment;
    }

}


