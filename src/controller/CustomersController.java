package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.Customer;
import utils.DBApptQuery;
import utils.DBCustomerQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Customers controller that handles the customer table along with direction buttons.
 * @author Kenly Berkowitz
 */
public class CustomersController implements Initializable {

    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, Integer> custIDcol;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, String> addressCol;

    @FXML
    private TableColumn<Customer, String> postalCodeCol;

    @FXML
    private TableColumn<Customer, String> phoneNumberCol;

    @FXML
    public TableColumn<Customer, String> stateCol;

    @FXML
    private TableColumn<Customer, String> CountryCol;

    /**
     *
     * @param event Onclick handle that deletes appointment from table and DB
     * @throws IOException Throws exception for attention message if the customer was deleted.
     */
    @FXML
    void DeleteButtonClicked(MouseEvent event) throws IOException {
        if(DBApptQuery.deleteAppointmentByCustID(customersTable.getSelectionModel().getSelectedItem())) {
            Main.attentionMessage("Customer: " + customersTable.getSelectionModel().getSelectedItem().getName() + "was deleted with all appointments.");
        }
        if(DBCustomerQuery.deleteCustomer(customersTable.getSelectionModel().getSelectedItem())) {
            customerList.remove(customersTable.getSelectionModel().getSelectedItem());
        }

    }

    /**
     *
     * @param event Onclick to add a customer, opening a new FXML with show and wait
     * @throws IOException Throws exception if load fails.
     */
    @FXML
    void addButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root, Color.valueOf("#606060")));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     *
     * @param event Onclick to modify a customer, opening a new FXML with show and wait
     * @throws IOException Throws exception if load fails.
     */
    @FXML
    void modifyButtonClicked(MouseEvent event) throws IOException {
        if(!(customersTable.getSelectionModel().getSelectedItem() == null)) {
            ModifyCustomer.setPassedCustomer(customersTable.getSelectionModel().getSelectedItem());
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyCustomer.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root, Color.valueOf("#606060")));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else
            Main.errorMessage("Please Select An Customer.");
    }

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setCustomerList();
            setCustomersTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Sets all cells in the the table and populates the table.
     * @exception SQLException error in sql query
     */
    public void setCustomersTable() throws SQLException {
        System.out.println("In set customers table");

        customersTable.setItems(CustomersController.customerList);
        custIDcol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        CountryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
    }

    /**
     *
     * @return Returns an Observable List of customers
     */
    public static ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    /**
     *
     * @throws SQLException Throws exception if error in SQL getCustomers.
     */
    public static void setCustomerList() throws SQLException {
        CustomersController.customerList = DBCustomerQuery.getAllCustomers();
    }
}

