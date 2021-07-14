package utils;

import controller.AddCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Kenly Berkowitz
 */
public class DBCustomerQuery {

    private static final Connection conn = DBConnection.getConnection();

    /**
     * Gets all customers and returns observable List.
     * @return observable List of customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String selectAllCutomerStatement = "SELECT * FROM customers\n" +
                    "INNER JOIN first_level_divisions, countries\n" +
                    "WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.Country_ID = countries.Country_ID";
            DBPreparedStatement.setStatement(conn, selectAllCutomerStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int custID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int devisionID = rs.getInt("Division_ID");
                String stateName = rs.getString("Division");
                String countryName = rs.getString("Country");

                Customer tempCustomer = new Customer(custID, name, address, postalCode, phoneNumber, devisionID, stateName, countryName);
                customerList.add(tempCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    /**
     * Inserts customer by passed customer.
     * @param customerToInsert customer to update.
     * @return Boolean to check if insert was successful.
     */
    public static boolean insertCustomer(Customer customerToInsert) {
        try {
            String insertCustomerStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?,?,?,?,?)";
            DBPreparedStatement.setStatementWithKeys(conn, insertCustomerStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            String customerName = customerToInsert.getName();
            String address = customerToInsert.getAddress();
            String postalCode = customerToInsert.getPostalCode();
            String phoneNumber = customerToInsert.getPhoneNumber();
            int divisionID = customerToInsert.getDivisionID();

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, divisionID);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int generatedID = rs.getInt(1);

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + "Row(s) affected");
                AddCustomer.customerID = generatedID;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update Customer by passed Customer.
     * @param customerToUpdate Customer to update
     * @return Returns boolean to check if update was successful.
     */
    public static boolean updateCustomer(Customer customerToUpdate) {
        try {
            String updateCustomerStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = " + customerToUpdate.getCustID();
            DBPreparedStatement.setStatement(conn, updateCustomerStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            String customerName = customerToUpdate.getName();
            String address = customerToUpdate.getAddress();
            String postalCode = customerToUpdate.getPostalCode();
            String phoneNumber = customerToUpdate.getPhoneNumber();
            int divisionID = customerToUpdate.getDivisionID();

            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, divisionID);

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + "Row(s) affected");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete customer by customer
     * @param customerToDelete Paseed customer to be deleted.
     * @return boolean to check if it was successfully deleted.
     */
    public static boolean deleteCustomer(Customer customerToDelete) {
        try {
            String deleteAppointmentStatement = "DELETE from customers WHERE Customer_ID = '" + customerToDelete.getCustID() + "'";
            DBPreparedStatement.setStatement(conn, deleteAppointmentStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();
            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + "Row(s) affected");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get one customer by customer ID.
     * @param custID Passed int customerID
     * @return return Customer
     */
    public static String getOneCustomer(int custID) {
        String tempStr = null;
        try {
            String getOneStatement = "SELECT * FROM customers WHERE Customer_ID = " + custID;
            DBPreparedStatement.setStatement(conn, getOneStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                tempStr = rs.getString("Customer_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tempStr;
    }

}