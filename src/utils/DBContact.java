package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Kenly Berkowitz
 */
public class DBContact {

    private static final Connection conn = DBConnection.getConnection();

    /**
     * Returns all contacts in DB.
     * @return Observable List contactList.
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String selectByMonthStatement = "SELECT * FROM contacts";

            DBPreparedStatement.setStatement(conn, selectByMonthStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contact tempContact = new Contact(id, name, email);
                contactList.add(tempContact);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}
