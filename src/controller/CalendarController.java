package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import utils.DBApptQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller to handle the appointments.
 * @author Kenly Berkowitz
 */
public class CalendarController implements Initializable {

    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public static boolean monthlyIsSelected;
    public static boolean weeklyIsSelected;
    public static boolean allIsSelected;


    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> apptIDcol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> descriptionCol;

    @FXML
    private TableColumn<Appointment, String> locationCol;

    @FXML
    private TableColumn<Appointment, String> contactCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> startTimeCol;

    @FXML
    private TableColumn<Appointment, String> EndTimeCol;

    @FXML
    private TableColumn<Appointment, Integer> custIDcol;

    @FXML
    public RadioButton monthlyRadioButton;

    @FXML
    public RadioButton weeklyRadioButton;

    @FXML
    public RadioButton AllRadioButton1;

    @FXML
    private Label appointmentAlert;

    /**
     * Adds appointments to tables.
     * @param appointmentToAdd passed appointment to add to the DB and tables
     * @throws SQLException Throws exception if SQL add appointment fails.
     */
    public static void addAppointment(Appointment appointmentToAdd) throws SQLException {
        if (appointmentToAdd.getStartTime().getMonthValue() == LocalDateTime.now().getMonthValue() && monthlyIsSelected) {
            appointmentList.add(appointmentToAdd);
        } else if(!monthlyIsSelected) {
            appointmentList.setAll(DBApptQuery.getAllApptByWeek());
        }
    }

    /**
     * Updates the table based on what radio button is selected.
     * @throws SQLException If SQL update throws an exception.
     */
    public static void updateTables() throws SQLException {
        if (monthlyIsSelected) {
            appointmentList.setAll(DBApptQuery.getAllApptByMonth());
        }
        else if (weeklyIsSelected){
            appointmentList.setAll(DBApptQuery.getAllApptByWeek());
        }
        else if (allIsSelected)
            appointmentList.setAll(DBApptQuery.getAllAppointments());
    }

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            CalendarController.appointmentList.setAll(DBApptQuery.getAllApptByMonth());
            monthlyIsSelected = true;
            setAppointmentsTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes selected appointment and updates tables.
     * @param event Onclick to delete selected appointment.
     * @throws SQLException Throws exception if there was an error in the deletion of object in DB
     * @throws IOException Throws exception from Attention message controller
     */
    @FXML
    void DeleteButtonClicked(MouseEvent event) throws SQLException, IOException {
        if(DBApptQuery.deleteAppointmentByApptID(appointmentsTable.getSelectionModel().getSelectedItem())) {
            Main.attentionMessage("Appointment Canceled\n" +
                                     "Appointment ID: " + appointmentsTable.getSelectionModel().getSelectedItem().getAppointmentID() + "\n" +
                                     "Type: " + appointmentsTable.getSelectionModel().getSelectedItem().getType());

            if (monthlyRadioButton.isSelected()) {
                appointmentList.setAll(DBApptQuery.getAllApptByMonth());
            }
            else if(weeklyRadioButton.isSelected()) {
                appointmentList.setAll(DBApptQuery.getAllApptByWeek());
            }
            else if(AllRadioButton1.isSelected()) {
                appointmentList.setAll(DBApptQuery.getAllAppointments());
            }
        }
    }

    /**
     * Adds an appointment by loading another FXML controller.
     * @param event OnClick to add an appointment in another FXML
     * @throws IOException Exception for opening the new stage in addAppointment FXML
     */
    @FXML
    void addButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root, Color.valueOf("#303030")));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Loads modify appointment controller if a object is selected in the table.
     * @param event OnClick to get selected appointment and modify it in another FXML
     * @throws IOException Exception for opening the new stage in modifyAppointment FXML
     */
    @FXML
    void modifyButtonClicked(MouseEvent event) throws IOException {

        if (!(appointmentsTable.getSelectionModel().getSelectedItem() == null)) {
            ModifyAppointment.passedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyAppointment.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root, Color.valueOf("#303030")));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else
            Main.errorMessage("Please Select An Appointment.");
    }

    /**
     * Retrieves all appointments by month from DB and sets table.
     * @param event Onclick that retrieves the appointments by month
     */
    @FXML
    void monthlyRadioButtonClicked(MouseEvent event) {
        try {
            CalendarController.appointmentList.setAll(DBApptQuery.getAllApptByMonth());
            monthlyIsSelected = true; //flag for static methods
            weeklyIsSelected = false;
            allIsSelected = false;
            setAppointmentsTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Retrieves all appointments by week from DB and sets table.
     * @param event Onclick that retrieves the appointments by weeks
     */
    @FXML
    void weeklyRadioButtonClicked(MouseEvent event) {
        try {
            CalendarController.appointmentList.setAll(DBApptQuery.getAllApptByWeek());
            monthlyIsSelected = false; //flag for static methods
            weeklyIsSelected = true;
            allIsSelected = false;
            setAppointmentsTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * Retrieves all appointments from DB and sets table.
     * @param mouseEvent Onclick that retrieves the appointments by weeks
     */
    @FXML
    public void allRadioButtonClicked(MouseEvent mouseEvent) {
        try {
            CalendarController.appointmentList.setAll(DBApptQuery.getAllAppointments());
            monthlyIsSelected = false; //flag for static methods
            weeklyIsSelected = false;
            allIsSelected = true;
            setAppointmentsTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sets all cells in the the table and populates the table.
     */
    private void setAppointmentsTable() {
        appointmentsTable.setItems(CalendarController.appointmentList);
        apptIDcol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeTable"));
        EndTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeTable"));
        custIDcol.setCellValueFactory(new PropertyValueFactory<>("custID"));
    }


}


