package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import model.*;
import utils.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Add Appointment Controller
 * @author Kenly Berkowitz
 */
public class AddAppointmentController implements Initializable {

    public static int appointmentID;

    private boolean timeWasPicked = true;
    private static User user;
    private final AppointmentTimes apptmentTimes = new AppointmentTimes();
    private ObservableList<LocalDateTime> startTimeList = FXCollections.observableArrayList();
    private ObservableList<LocalDateTime> endTimeList = FXCollections.observableArrayList();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    Appointment tempAppointment = null;

    @FXML
    public ComboBox<Customer> customerCombo;

    @FXML
    private ComboBox<LocalDateTime> startTimeCombo;

    @FXML
    private ComboBox<LocalDateTime> endTimeCombo;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField userID;

    @FXML
    private TextField apptIDField;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private TextField typeField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    /**
     * closes window
     */
    private void closeStage() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Handle to choose start time.
     * @exception SQLException  for matchesUser method that gets all users from DB
     * @param actionEvent sets endTimePicker
     */
    @FXML
    public void startTimeComboHandle(ActionEvent actionEvent) throws SQLException {

        if (startTimeCombo.getSelectionModel().getSelectedItem() != null) {
            endTimeList = apptmentTimes.filterEndTimesBasedOnStartTime(startTimeCombo.getSelectionModel().getSelectedItem());
            timeWasPicked = false;  //used to make sure that times are not adjusted when action event takes place. This ensures date selection is shown correctly in other time zones.
            startDatePicker.setValue(startTimeCombo.getSelectionModel().getSelectedItem().toLocalDate());  //will make the date of the startDatePicker the date that coincides with the time. This solves the problem of different time zones
        }
        timeWasPicked = true;

        initEndCombo();
    }

    /**
     * Closes stage.
     * @param event calls closeStage method
     */
    @FXML
    void cancelButtonClicked(MouseEvent event) {
        closeStage();
    }

    /**
     * Saves appointment.
     * @exception SQLException  for matchesUser method that gets all users from DB
     * @exception  IOException for error dialog box
     * @param event Saves the appointment by validating and building object. Insert into DB and sets tables and RunnableNextAppt object.
     */
    @FXML
    void saveButtonClicked(MouseEvent event) throws IOException, SQLException {
        if (inputValidation()){
            buildObject();
            if(DBApptQuery.insertAppointment(tempAppointment)) {
                tempAppointment.setAppointmentID(appointmentID);
                CalendarController.addAppointment((tempAppointment));
                RunnableNextAppt.setApptListAndNextAppt();
                closeStage();
            }
        }
    }

    /**
     * Gets user.
     * @return Returns user.
     */
    public static User getUser() {
        return user;
    }

    /**
     * Sets user from static variable in PrimaryViewController
     */
    public static void setUser() {
       user = PrimaryViewController.getUser();
    }

    /**
     * Needed to use a lambda function for setOnAction so when a date is selected, it will call this function and set the date.
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContactCombo();
        initCustomerCombo();
        setUser();
        userID.setText(String.valueOf(user.getUserID()));

        startDatePicker.setOnAction((ActionEvent event) -> {
            if(startDatePicker.getValue().isBefore(LocalDate.now())) {
                try {
                    Main.errorMessage("Date selected is in the past. \nPlease select another date.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startDatePicker.setValue(LocalDate.now());
            }
            //if statement is used to make sure it does not run when startimehandle is selected and sets new dates in date picker
            if (timeWasPicked) {
                endDatePicker.setValue(startDatePicker.getValue());
                try {
                    apptmentTimes.setLocalDate(startDatePicker.getValue());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                initStartEndTimeLists();
            }

        });

    }

    /**
     * Lambda function callback to set start and end time for combo boxes.
     * Could not access toString method, so I needed to used this lambda function to modify the cells in combo box.
     */
    Callback<ListView<LocalDateTime>, ListCell<LocalDateTime>> factory = lv -> new ListCell<>() {
        @Override
        protected void updateItem(LocalDateTime localDateTime, boolean empty) {
            super.updateItem(localDateTime, empty);
            setText(empty ? "" : (localDateTime.format(formatter)));
            setAlignment(Pos.CENTER);
            setStyle("-fx-font-size: 13;");
        }
    };
    /**
     * Lambda function callback to set start and end time for combo boxes
     * Could not access toString method, so I needed to used this lambda function to modify the cells in combo box.
     */
    Callback<ListView<LocalDateTime>, ListCell<LocalDateTime>> factoryUsed = lv -> new ListCell<>() {
        @Override
        protected void updateItem(LocalDateTime localDateTime, boolean empty) {
            super.updateItem(localDateTime, empty);
            setText(empty ? "" : (localDateTime.format(formatter)));
            setAlignment(Pos.CENTER);
            setStyle("-fx-font-size: 13;");
        }
    };

    /**
     * Initializes Start combo box and sets cells.
     */
    private void initStartCombo() {
        startTimeCombo.setItems(startTimeList);
        startTimeCombo.setCellFactory(factory);
        startTimeCombo.setButtonCell(factoryUsed.call(null));
    }

    /**
     * Initializes End combo box and sets cells.
     */
    private void initEndCombo() {
        endTimeCombo.setItems(endTimeList);
        endTimeCombo.setCellFactory(factory);
        endTimeCombo.setButtonCell(factoryUsed.call(null));
    }

    /**
     * Initializes Start and end time lists. Initializes start and end combo.
     */
    private void initStartEndTimeLists() {
        startTimeList.clear();
        endTimeList.clear();
        startTimeList = apptmentTimes.getAvailableStartTimeList();
        endTimeList = apptmentTimes.getAvailableEndTimeList();
        initStartCombo();
        initEndCombo();
    }

    /**
     * Initializes Customer combo box and sets cells.
     * Could not access toString method, so I needed to used this lambda function to modify the cells in combo box.
     */
    private void initCustomerCombo() {
        customerList = DBCustomerQuery.getAllCustomers();
        customerCombo.setItems(customerList);

        Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : ("ID: " + customer.getCustID() + "   Name: " + customer.getName()));
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        Callback<ListView<Customer>, ListCell<Customer>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : ("ID: " + customer.getCustID() + "   Name: " + customer.getName()));
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        customerCombo.setCellFactory(factory);
        customerCombo.setButtonCell(factoryUsed.call(null));
    }

    /**
     * Initializes Contact combo box and sets cells.
     */
    private void initContactCombo() {
        contactList = DBContact.getAllContacts();
        contactCombo.setItems(contactList);

        Callback<ListView<Contact>, ListCell<Contact>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : (contact.getName()));
                setAlignment(Pos.CENTER);
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        Callback<ListView<Contact>, ListCell<Contact>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : (contact.getName()));
                setAlignment(Pos.CENTER);
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        contactCombo.setCellFactory(factory);
        contactCombo.setButtonCell(factoryUsed.call(null));
    }

    /**
     * Builds new object from input fields.
     */
    private void buildObject() {
        //retrieves temp variables from fields in order to build the object
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String location = locationField.getText().trim();
        String type = typeField.getText().trim();
        String contactName = contactCombo.getSelectionModel().getSelectedItem().getName();
        LocalDateTime startTime = startTimeCombo.getSelectionModel().getSelectedItem();
        LocalDateTime endTime = endTimeCombo.getSelectionModel().getSelectedItem();
        int aUserID = Integer.parseInt(userID.getText().trim());
        int custID = customerCombo.getSelectionModel().getSelectedItem().getCustID();
        int contactID = contactCombo.getSelectionModel().getSelectedItem().getID();

        //Builds new object and sets it to tempAppointment
        tempAppointment = new Appointment(AddAppointmentController.appointmentID, title, description, location, type, contactName, startTime, endTime, aUserID, custID, contactID);
    }

    /**
     * Checks input validation for all fields.
     * @exception SQLException  for matchesUser method that gets all users from DB
     * @exception  IOException for error dialog box
     * @return boolean for input validation. Returns false if the inputs are invalid. Otherwise, returns true.
     */
    private boolean inputValidation() throws IOException, SQLException {
        //Validate titleField, returns false if empty
        if(titleField.getText().trim().isEmpty()) {
            Main.errorMessage("Title Field Empty");
            return false;
        }
        //Validate descriptionField, returns false if empty
        if(descriptionField.getText().trim().isEmpty()) {
            Main.errorMessage("Description Field Empty");
            return false;
        }
        //Validate locationField, returns false if empty
        if(locationField.getText().trim().isEmpty()) {
            Main.errorMessage("Location Field Empty");
            return false;
        }
        //Validate typeField, returns false if empty
        if(typeField.getText().trim().isEmpty()) {
            Main.errorMessage("Type Field Empty");
            return false;
        }
        //Validate contactCombo, returns false if object in contact combo is not selected
        if(contactCombo.getSelectionModel().getSelectedItem() == null) {
            Main.errorMessage("Please Select a Contact");
            return false;
        }
        //Validate startTimeCombo, returns false if object in start time combo is not selected
        if(startTimeCombo.getSelectionModel().getSelectedItem() == null) {
            Main.errorMessage("Please Select A Start Time");
            return false;
        }
        //Validate endTimeCombo, returns false if object in end time combo is not selected
        if(endTimeCombo.getSelectionModel().getSelectedItem() == null) {
            Main.errorMessage("Please Select A End Time");
            return false;
        }
        //Validate userIDField, returns false if empty, or if it is not a number or if it is not a 10 or 12 digit number
        if(userID.getText().trim().isEmpty() || (!(isNumber(userID.getText().trim()))) || matchesUser()) {
            if (userID.getText().trim().isEmpty())
                Main.errorMessage("User ID Field Empty");
            if ((!(isNumber(userID.getText().trim()))))
                Main.errorMessage("User ID Field needs to be a number");
            if(matchesUser())
                Main.errorMessage("User ID Field needs to be a registered user");
            return false;
        }
        //Validate customerCombo, returns false if object in customer combo is not selected
        if(customerCombo.getSelectionModel().getSelectedItem() == null) {
            Main.errorMessage("Please Select A Customer");
            return false;
        }

        return true;
    }

    /**
     * Checks if passed string is a number.
     * @param isNum String to be check if it is a valid number.
     * @return Returns true if the whole string contains digits. Returns false when it finds a char that is not a digit.
     */
    private boolean isNumber(String isNum)  {
        for (int i = 0; i < isNum.length(); ++i) {
            if (!Character.isDigit(isNum.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Checks to see if userID is a valid userID.
     * @exception SQLException for DB query
     * @return boolean to check if stock is in between min and max
     */
    private boolean matchesUser() throws SQLException {
        ArrayList<Integer> arrayList;
        arrayList = DBLoginQuery.getUserAllUserIDs();

        for (Integer integer: arrayList) {
            if (integer == Integer.parseInt(userID.getText().trim())) {
                return false;
            }
        }
        return true;
    }
}
