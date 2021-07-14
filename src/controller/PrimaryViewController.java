package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Appointment;
import model.User;
import utils.RunnableNextAppt;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * The primary controller that loads the other controllers into it.
 * @author Kenly Berkowitz
 */
public class PrimaryViewController implements Initializable {

    private static User user;
    public static BooleanProperty apptWithinFifteen = new SimpleBooleanProperty();
    public static BooleanProperty boolProp = new SimpleBooleanProperty();
    public static Appointment nextAppt = null;

    @FXML
    public Label appointmentAlert;

    @FXML
    private Label logoutButton;

    @FXML
    private Label userID;

    @FXML
    public Label clock;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button calendarViewButton;

    @FXML
    private Button customersViewButton;

    @FXML
    private Button reportsViewButton;

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runClock();
        userID.setText("User: " + user.getUserName());
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/calendar.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(root);
        setFillToMenuButtons(1);

        appointmentAlert.setText("There are no upcoming appointments");
        appointmentAlert.setStyle("-fx-background-color: #bce3ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: white;");
        apptWithinFifteen.set(false);
        boolProp.bind(apptWithinFifteen);
        boolProp.addListener((observable, oldValue, newValue) -> {
            // Only if completed
            if (newValue) {
                System.out.println("in if set style");
                appointmentAlert.setText("Appointment within 15 minutes!!\n" +
                        "Apointment ID: " + nextAppt.getAppointmentID() + "\n" +
                        "Date: " + nextAppt.getStartTime().toLocalDate() + "\n" +
                        "Time: " + nextAppt.getStartTime().toLocalTime() + "\n");
                appointmentAlert.setStyle("-fx-background-color: #bce3ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: red;");
            }
            else {
                System.out.println("in else set style");
                appointmentAlert.setText("There are no upcoming appointments");
                appointmentAlert.setStyle("-fx-background-color: #bce3ff; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: white;");
            }

        });

        RunnableNextAppt.primaryViewHasLoaded = true;

    }

    /**
     *
     * @param event Onclick Calendar button to load FXML window.
     * @throws IOException Throws error if controller load fails.
     */
    @FXML
    void calendarViewButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/calendar.fxml"));
        mainPane.setCenter(root);
        setFillToMenuButtons(1);
    }

    /**
     *
     * @param event Onclick Customer button to load FXML window.
     * @throws IOException Throws error if controller load fails.
     */
    @FXML
    void customersViewButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        mainPane.setCenter(root);
        setFillToMenuButtons(2);
    }

    /**
     *
     * @param event Onclick Reports button to load FXML window.
     * @throws IOException Throws error if controller load fails.
     */
    @FXML
    void reportsViewButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        mainPane.setCenter(root);
        setFillToMenuButtons(3);
    }

    /**
     *
     * @param num Sets text fill for selected button.
     */
    @FXML
    private void setFillToMenuButtons(int num) {
        if(num == 1) {
            calendarViewButton.setTextFill(Color.valueOf("#bce3ff"));
            customersViewButton.setTextFill(Color.valueOf("#fff"));
            reportsViewButton.setTextFill(Color.valueOf("#fff"));
        }
        else if(num == 2) {
            calendarViewButton.setTextFill(Color.valueOf("#fff"));
            customersViewButton.setTextFill(Color.valueOf("#bce3ff"));
            reportsViewButton.setTextFill(Color.valueOf("#fff"));
        }
        else {
            calendarViewButton.setTextFill(Color.valueOf("#fff"));
            customersViewButton.setTextFill(Color.valueOf("#fff"));
            reportsViewButton.setTextFill(Color.valueOf("#bce3ff"));
        }
    }

    /**
     *
     * @param mouseEvent Onclick pressed to go back to sign in.
     * @throws IOException Throws exception for FXML loading error.
     */
    @FXML
    private void logoutButtonClicked(MouseEvent mouseEvent) throws IOException {
        System.out.println("logout button pressed.");

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/log-in.fxml"));
        Parent root = (Parent) loader.load();
        stage.setScene(new Scene(root));
        stage.show();

        closeStage();
    }

    /**
     *
     * @param mouseEvent Mouse move in to change text fill.
     */
    @FXML
    private void mouseMoveOver(MouseEvent mouseEvent) {
        logoutButton.setTextFill(Color.valueOf("#bce3ff"));
    }

    /**
     *
     * @param mouseEvent Mouse move out to change text fill.
     */
    @FXML
    private void mouseMoveOut(MouseEvent mouseEvent) {
        logoutButton.setTextFill(Color.valueOf("#fff"));
    }

    /**
     *
     * @param user Sets user.
     */
    public static void setUser(User user) {
        PrimaryViewController.user = user;
    }

    /**
     *
     * @return Gets user.
     */
    public static User getUser() {
        return PrimaryViewController.user;
    }

    /**
     * Closes stage.
     */
    private void closeStage() {
        Stage thisStage = (Stage) logoutButton.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Function creates an animation to display a clock.
     */
    private void runClock() {
        Timeline animateClock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            clock.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        animateClock.setCycleCount(Animation.INDEFINITE);
        animateClock.play();
    }

}
