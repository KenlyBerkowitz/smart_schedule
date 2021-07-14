package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Appointment;
import model.Contact;
import utils.DBApptQuery;
import utils.DBContact;
import utils.DBCustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Reports controller that handles 3 different reports and tables.
 * @author Kenly Berkowitz
 */
public class ReportsController implements Initializable {
    /**
     * Created a pair data structure.
     */
    protected static class APair {
        private final String key;
        private int number;

        public APair(String key, int number) {
            this.key = key;
            this.number = number;
        }

        public String getKey() {
            return key;
        }

        public int getNumber() {
            return number;
        }
        public void addOne() {
            number++;
        }
    }

    protected enum Month {
        JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);
        private final int monthNumber;

        Month(int monthNumber) {
            this.monthNumber = monthNumber;
        }

        public int getMonthNumber() {
            return monthNumber;
        }
    }

    private final ObservableList<Appointment> appointmentListByContact = FXCollections.observableArrayList();
    private final ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<APair> apptTypeByMonth = FXCollections.observableArrayList();


    @FXML
    private TableView<Appointment> contactApptTable;

    @FXML
    private TableColumn<Appointment, ?> apptIDcol;

    @FXML
    private TableColumn<Appointment, ?> titleCol;

    @FXML
    private TableColumn<Appointment, ?> typeCol;

    @FXML
    private TableColumn<Appointment, ?> descriptionCol;

    @FXML
    private TableColumn<Appointment, ?> startDateCol;

    @FXML
    private TableColumn<Appointment, ?> endDateCol;

    @FXML
    private TableColumn<Appointment, ?> customerID;

    //Number of appointments tables
    @FXML
    private TableView<APair> numberApptTable;

    @FXML
    private TableColumn<APair, String> numApptTypeCol;

    @FXML
    private TableColumn<APair, Integer> numberApptCol;

    @FXML
    private ComboBox<Contact> contactScheduleCombo;

    @FXML
    private ComboBox<String> apptMonthCombo;

    @FXML
    private Label customerLabel;

    @FXML
    private Label dailyAppointmentCount;

    /**
     *
     * @param actionEvent Onclick with the selection of contact and retrieves list from DB
     * @throws SQLException Throws SQL exception.
     */
    public void contactCombo(ActionEvent actionEvent) throws SQLException {
        appointmentListByContact.setAll(DBApptQuery.getAllAppointmentsByContact(contactScheduleCombo.getSelectionModel().getSelectedItem()));
        System.out.println(appointmentListByContact.size());
    }

    /**
     * Initializes contact combo.
     */
    private void initContactCombo() {
        contactScheduleCombo.setItems(DBContact.getAllContacts());

        Callback<ListView<Contact>, ListCell<Contact>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : ("Name: " + contact.getName()));
                //setAlignment(Pos.CENTER);
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        Callback<ListView<Contact>, ListCell<Contact>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contact contact, boolean empty) {
                super.updateItem(contact, empty);
                setText(empty ? "" : ("Name: " + contact.getName()));
                //setAlignment(Pos.CENTER);
                setStyle("-fx-font-size: 13; -fx-font-family: Menlo");
            }
        };

        contactScheduleCombo.setCellFactory(factory);
        contactScheduleCombo.setButtonCell(factoryUsed.call(null));
    }

    /**
     * Sets contact table.
     */
    private void setAppointmentsTableByContact() {
        contactApptTable.setItems(appointmentListByContact);
        apptIDcol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startTimeTable"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endTimeTable"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("custID"));
    }

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMonthCombo();
        setNumberOfAppointmentTable();
        initContactCombo();
        setAppointmentsTableByContact();
        try {
            getDailyAppointCount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent Onclick of selection of month.
     * @throws SQLException Throws exception for SQL error.
     */
    public void monthCombo(ActionEvent actionEvent) throws SQLException {
        apptTypeByMonth.clear();
        String monthToRetreive = apptMonthCombo.getSelectionModel().getSelectedItem();
        int monthNumner = Month.valueOf(monthToRetreive).getMonthNumber();
        ObservableList<Appointment> appointmentListByMonth = DBApptQuery.getAllApptByMonthNumber(monthNumner);

       if (!appointmentListByMonth.isEmpty()) {
           for (Appointment appointment : appointmentListByMonth) {
               if (apptTypeByMonth.isEmpty()) {
                   apptTypeByMonth.add(new APair(appointment.getType(), 1));
                   continue;
               }
               for (int i = 0; i < apptTypeByMonth.size(); ++i) {
                   if (appointment.getType().equals(apptTypeByMonth.get(i).getKey())) {
                       apptTypeByMonth.get(i).addOne();
                       break;
                   }
                   if (i == apptTypeByMonth.size() - 1)
                       apptTypeByMonth.add(new APair(appointment.getType(), 1));
               }
           }
       }

    }

    /**
     * Initialize month combo
     */
    private void initMonthCombo() {
        for(Month month: Month.values()) {
            String strMonth = month.name();
            monthList.add(strMonth);
        }
        apptMonthCombo.setItems(monthList);
    }

    /**
     * Sets appointment table.
     */
    private void setNumberOfAppointmentTable() {
        numberApptTable.setItems(apptTypeByMonth);
        numApptTypeCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        numberApptCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    /**
     *
     * @throws SQLException Exception from SQL retrieval from DB
     */
    private void getDailyAppointCount() throws SQLException {
        ObservableList<Appointment> allAppointments = DBApptQuery.getAllApptByMonth();
        Map<Integer, Integer> personMap = new HashMap<>();
        int tempHighValue = 0;
        int tempCustID = 0;
        boolean flag;
        boolean flag2 = true;
        String customerName = null;

        for(Appointment appointment: allAppointments) {
            if(personMap.isEmpty()) {
                personMap.put(appointment.getCustID(), 1);
                continue;
            }
            flag2 = true;

            for (Map.Entry<Integer, Integer> e : personMap.entrySet()) {
                if (appointment.getCustID() == e.getKey()) {
                    int temp = e.getValue();
                    temp++;
                    e.setValue(temp);
                    flag2 = false;
                }
            }
            if(flag2)
                personMap.put(appointment.getCustID(), 1);
        }
        flag = true;

        for (Map.Entry<Integer, Integer> e : personMap.entrySet()) {
            if (flag) {
                tempHighValue = e.getValue();
                tempCustID = e.getKey();
                flag = false;
                continue;
            }
            if(tempHighValue < e.getValue()) {
                tempHighValue = e.getValue();
                tempCustID = e.getKey();
            }
        }
        customerName = DBCustomerQuery.getOneCustomer(tempCustID);
        if (!(customerName == null)) {
            customerLabel.setText(customerName);
            dailyAppointmentCount.setText(String.valueOf(tempHighValue));
        }
        
    }
}



