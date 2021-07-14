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
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
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
 * Modify appointment controller.
 * @author Kenly Berkowitz
 */
public class ModifyAppointment  implements Initializable {

    public static Appointment passedAppointment;

    private boolean timeWasPicked = true;
    private final AppointmentTimes apptmentTimes = new AppointmentTimes();
    private ObservableList<LocalDateTime> startTimeList = FXCollections.observableArrayList();
    private ObservableList<LocalDateTime> endTimeList = FXCollections.observableArrayList();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private ObservableList<Contact> contactList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    Appointment tempAppointment = null;

    @FXML
    public ComboBox<LocalDateTime> startTimeCombo;

    @FXML
    public ComboBox<LocalDateTime> endTimeCombo;

    @FXML
    public ComboBox<Customer> customerCombo;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField userIDField;

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
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param event calls closeStage method
     */
    @FXML
    void cancelButtonClicked(MouseEvent event) {
        closeStage();
    }

    /**
     * @exception SQLException  for matchesUser method that gets all users from DB
     * @exception  IOException for error dialog box
     * @param event Saves the appointment by validating and building object. Insert into DB and sets tables and RunnableNextAppt object.
     */
    @FXML
    void saveButtonClicked(MouseEvent event) throws SQLException, IOException {
        //validates input, builds object, updates SQL database. If successful, it updates table and the runnableNextAppt thread in order for it to track the appointment
        if (inputValidation()){
            buildObject();
            if(DBApptQuery.updateAppointment(tempAppointment)) {
                CalendarController.updateTables();
                RunnableNextAppt.setApptListAndNextAppt();
                closeStage();
            }
        }
    }

    /**
     *
     * @param actionEvent sets endTimePicker
     */
    @FXML
    public void startTimeComboHandle(ActionEvent actionEvent) {
        if (startTimeCombo.getSelectionModel().getSelectedItem() != null) {
            endTimeList = apptmentTimes.filterEndTimesBasedOnStartTime(startTimeCombo.getSelectionModel().getSelectedItem());
            timeWasPicked = false;  //used to make sure that times are not adjusted when action event takes place. This ensures date selection is shown correctly in other time zones.
            startDatePicker.setValue(startTimeCombo.getSelectionModel().getSelectedItem().toLocalDate());
        }
        timeWasPicked = true;

        initEndCombo();
    }

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initContactCombo();
        initCustomerCombo();
        initAllFields();

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
     * Initializes all fields from selected appointment.
     */
    private void initAllFields() {
        //setting all textFields
        apptIDField.setText(String.valueOf(passedAppointment.getAppointmentID()));
        userIDField.setText(String.valueOf(passedAppointment.getUserID()));
        typeField.setText(passedAppointment.getType());
        titleField.setText(passedAppointment.getTitle());
        descriptionField.setText(passedAppointment.getDescription());
        locationField.setText(passedAppointment.getLocation());

        //must find matching value and set contactCombo
        for (Contact contact: contactList) {
            if(contact.getID() == passedAppointment.getContactID())
                contactCombo.setValue(contact);
        }
        //must find matching value and set customerCombo
        for (Customer customer: customerList) {
            if(customer.getCustID() == passedAppointment.getCustID())
                customerCombo.setValue(customer);
        }

        //sets start and end date picker
        startDatePicker.setValue(passedAppointment.getStartTime().toLocalDate());
        endDatePicker.setValue(startDatePicker.getValue());

        //sets appointments times in apptment times and removes the selected start and end time from taken times in the AppointmentTimes class
        try {

            apptmentTimes.setLocalDate(startDatePicker.getValue(), passedAppointment.getStartTime(), passedAppointment.getEndTime());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        initStartEndTimeLists();

        //sets start and end time combos and filter the end times according to start times
        startTimeCombo.setValue(passedAppointment.getStartTime());
        endTimeList = apptmentTimes.filterEndTimesBasedOnStartTime(startTimeCombo.getSelectionModel().getSelectedItem());
        endTimeCombo.setValue(passedAppointment.getEndTime());

        initEndCombo();

    }

    /**
     * Lambda function callback to set start and end time for combo boxes
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
     */
    private void initCustomerCombo() {
        customerList = DBCustomerQuery.getAllCustomers();
        customerCombo.setItems(customerList);

        Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : ("ID: " + customer.getCustID() + "   Name: " + customer.getName()));
                //setAlignment(Pos.CENTER);
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        Callback<ListView<Customer>, ListCell<Customer>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : ("ID: " + customer.getCustID() + "   Name: " + customer.getName()));
                //setAlignment(Pos.CENTER);
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
        int aUserID = Integer.parseInt(userIDField.getText().trim());
        int custID = customerCombo.getSelectionModel().getSelectedItem().getCustID();
        int contactID = contactCombo.getSelectionModel().getSelectedItem().getID();

        //Builds new object and sets it to tempAppointment
        tempAppointment = new Appointment(passedAppointment.getAppointmentID(), title, description, location, type, contactName, startTime, endTime, aUserID, custID, contactID);
    }

    /**
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
        if(userIDField.getText().trim().isEmpty() || (!(isNumber(userIDField.getText().trim()))) || matchesUser()) {
            if (userIDField.getText().trim().isEmpty()) {
                Main.errorMessage("User ID Field Empty");
                return false;
            }
            if ((!(isNumber(userIDField.getText().trim())))) {
                Main.errorMessage("User ID Field needs to be a number");
                return false;
            }
            if(matchesUser()) {
                Main.errorMessage("User ID Field needs to be a registered user");
                return false;
            }
        }
        //Validate customerCombo, returns false if object in customer combo is not selected
        if(customerCombo.getSelectionModel().getSelectedItem() == null) {
            Main.errorMessage("Please Select A Customer");
            return false;
        }

        return true;
    }

    /**
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
     * @exception SQLException for DB query
     * @return boolean to check if stock is in between min and max
     */
    private boolean matchesUser() throws SQLException {
        ArrayList<Integer> arrayList;
        arrayList = DBLoginQuery.getUserAllUserIDs();

        for (Integer integer: arrayList) {
            if (integer == Integer.parseInt(userIDField.getText().trim())) {
                return false;
            }
        }
        return true;
    }
}
