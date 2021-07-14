package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;
import utils.DBCountryStateQueries;
import utils.DBCustomerQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds customer Controller.
 * @author Kenly Berkowitz
 */
public class AddCustomer implements Initializable {

    public static int customerID;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField customerIDField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ComboBox<Country> countryCombo;

    @FXML
    private ComboBox<FirstLevelDivision> stateCombo;

    @FXML
    private TextField phoneNumberField;

    /**
     *
     * closes window
     */
    private void closeStage() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     *
     * @param event closes window
     */
    @FXML
    private void cancelButtonClicked(MouseEvent event) {
        closeStage();
    }

    /**
     *
     * @param event Onclick saves the new customer to the DB and Lists.
     * @exception SQLException  for adding Customer to DB
     * @exception  IOException for error dialog box
     */
    @FXML
    private void saveButtonClicked(MouseEvent event) throws SQLException, IOException {
        boolean errorFlag = false;
        Customer newCustomer = null;
        newCustomer = buildCustomer();
        if(newCustomer != null)
            errorFlag = addCustToDBAndList(newCustomer);  //returns true is success and no errors

        if (errorFlag)
            closeStage();
    }

    /**
     *
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCountryCombo();
        initFLDivisionCombo();

    }

    /**
     *
     * @param actionEvent closes window
     */
    public void countryComboHandle(ActionEvent actionEvent) {
        Country tempCountry = countryCombo.getSelectionModel().getSelectedItem();
        initFLDividionComboByCountry(tempCountry.getCountryId());
    }

    /**
     * Initializes Country combo box and sets cells.
     */
    private void initCountryCombo() {
        try {
            countryCombo.setItems(DBCountryStateQueries.getAllCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Callback<ListView<Country>, ListCell<Country>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Country country, boolean empty) {
                super.updateItem(country, empty);
                setText(empty ? "" : (country.getCountryName()));
            }
        };
        Callback<ListView<Country>, ListCell<Country>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Country country, boolean empty) {
                super.updateItem(country, empty);
                setText(empty ? "" : (country.getCountryName()));
            }
        };
        countryCombo.setCellFactory(factory);
        countryCombo.setButtonCell(factoryUsed.call(null));
    }

    //First level division combo by all countries
    /**
     * Initializes FLDivision combo box and sets cells.
     */
    private void initFLDivisionCombo() {
        try {
            stateCombo.setItems(DBCountryStateQueries.getAllFirstLevelDivisions());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Callback<ListView<FirstLevelDivision>, ListCell<FirstLevelDivision>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(FirstLevelDivision division, boolean empty) {
                super.updateItem(division, empty);
                setText(empty ? "" : (division.getDivision()));
            }
        };
        Callback<ListView<FirstLevelDivision>, ListCell<FirstLevelDivision>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(FirstLevelDivision division, boolean empty) {
                super.updateItem(division, empty);
                setText(empty ? "" : (division.getDivision()));
            }
        };
        stateCombo.setCellFactory(factory);
        stateCombo.setButtonCell(factoryUsed.call(null));
    }

    //First level division combo by specific country
    /**
     *
     * @param countryNum Passes country ID in order to populate FLDivisionComboBox by country
     */
    private void initFLDividionComboByCountry(int countryNum) {
        try {
            stateCombo.setItems(DBCountryStateQueries.getFLDivisionsByCountry(countryNum));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Callback<ListView<FirstLevelDivision>, ListCell<FirstLevelDivision>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(FirstLevelDivision division, boolean empty) {
                super.updateItem(division, empty);
                setText(empty ? "" : (division.getDivision()));
            }
        };
        Callback<ListView<FirstLevelDivision>, ListCell<FirstLevelDivision>> factoryUsed = lv -> new ListCell<>() {
            @Override
            protected void updateItem(FirstLevelDivision division, boolean empty) {
                super.updateItem(division, empty);
                setText(empty ? "" : (division.getDivision()));
            }
        };
        stateCombo.setCellFactory(factory);
        stateCombo.setButtonCell(factoryUsed.call(null));
    }

    //returns true if add customer success or return false if it fails
    /**
     *
     * @param newCustomer Customer to add to the DB and tables.
     * @return Returns true if Successful, Otherwise, false.
     * @exception SQLException error if sql error.
     */
    private boolean addCustToDBAndList(Customer newCustomer) throws SQLException {

        if (DBCustomerQuery.insertCustomer(newCustomer)) {
            newCustomer.setCustID(customerID);  //sets the newCustomer objects custID before adding it to the table
            CustomersController.customerList.add(newCustomer);
            return true;
        }
        return false;
    }

    //checks input, builds new Customer object and returns it
    /**
     * @exception  IOException for error dialog box
     * @return Returns built Customer object if input validation returns as true. Returns null if validation returns false.
     */
    private Customer buildCustomer() throws IOException {
        if (inputValidation()) {
            String customerName = customerNameField.getText().trim();
            String address = addressField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String countryName = countryCombo.getSelectionModel().getSelectedItem().getCountryName();
            int flDivisionID = stateCombo.getSelectionModel().getSelectedItem().getDivisionId();
            String stateName = stateCombo.getSelectionModel().getSelectedItem().getDivision();
            String phoneNumber = phoneNumberField.getText().trim();

            return new Customer(AddCustomer.customerID, customerName, address, postalCode, phoneNumber, flDivisionID, stateName, countryName);
        }
        return null;
    }

    /**
     * @exception  IOException for error dialog box
     * @return Returns true if all the input fields are valid. Otherwise, false.
     */
    private boolean inputValidation()throws IOException {
        if(customerNameField.getText().trim().isEmpty()) {
            Main.errorMessage("Customer Name Field Empty");
            return false;
        }
        if(addressField.getText().trim().isEmpty() || (!addressValidation(addressField.getText().trim()))) {
            if(addressField.getText().trim().isEmpty())
                Main.errorMessage("Address Field Empty");

            return false;
        }
        if(postalCodeField.getText().trim().isEmpty()) {
            Main.errorMessage("Postal Code Field Empty");
            return false;
        }
        if(phoneNumberField.getText().trim().isEmpty() || (!(validatePhoneNumber()))) {
            if(phoneNumberField.getText().trim().isEmpty())
                Main.errorMessage("Phone Number Field Empty");
            return false;
        }
        if((countryCombo.getSelectionModel().getSelectedItem() == null)) {
            Main.errorMessage("Please select a country");
            return false;
        }

        if((stateCombo.getSelectionModel().getSelectedItem() == null)) {
            Main.errorMessage("Please select a state");
            return false;
        }
        return true;
    }

    //if address matches the regular expression, it will return true. Otherwise, false
    /**
     * @param tempAddress to check if it is a valid address.
     * @exception  IOException for error dialog box
     * @return If phone number has no hyphens and is 10 or 12 numbers, return true, otherwise, return false
     */
    private boolean addressValidation(String tempAddress) throws IOException {
            String regex = "^\\d+\\s[A-z\\s]+,\\s[A-z\\s]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(tempAddress);
            System.out.println(matcher.matches());
            if (!matcher.matches())
                Main.errorMessage("Invalid address format.\n Please re-enter address with format of:\n 25 Pike Pl, Seattle ");
            return matcher.matches();

    }

    /**
     * @param isNum String to be check if it is a valid number.
     * @return Returns true if the whole string contains digits. Returns false when it finds a char that is not a digit.
     */
    private boolean isNumber(String isNum)  {
            for (int i = 0; i < isNum.length(); ++i) {
                if (!Character.isDigit(isNum.charAt(i))) {
                    return false;
                }
            }
        return true;
    }

    /**
     * @exception  IOException for error dialog box
     * @return If phone number has no hyphens and is 10 or 12 numbers, return true, otherwise, return false
     */
    private boolean validatePhoneNumber() throws IOException {
        //if phoneNumber is valid and returns true, it will not run if statement
        if(!isNumber(phoneNumberField.getText().trim())){
            Main.errorMessage("Phone Number: Must be digits only with no spaces and no hyphens.");
            return false;
        }

        if ((phoneNumberField.getText().trim().length() == 10) || (phoneNumberField.getText().trim().length() == 12)) {
            return true;
        }
        Main.errorMessage("Phone Number: Must be 10 or 12 digits.");
        return false ;
    }

}


