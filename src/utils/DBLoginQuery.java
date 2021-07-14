package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Kenly Berkowitz
 */
public class DBLoginQuery {
    private static final Connection conn = DBConnection.getConnection();

    /**
     * Checks users login and return user.
     * @param userName Passed username to query and compare to password
     * @param password Passed password to query and compare to username
     * @return User
     * @throws SQLException error if SQl error thrown
     */
    public static User checkUserLogin(String userName, String password) throws SQLException {
        User tempUser = null;
        String checkLoginStatement = "SELECT * From users\n" +
                                     "WHERE User_Name = '" + userName + "' AND Password = '" + password + "'";

        DBPreparedStatement.setStatement(conn, checkLoginStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int resultUserId = rs.getInt("User_ID");
            String resultUserName = rs.getString("User_Name");
            tempUser = new User(resultUserName, resultUserId);
        }
        return tempUser;
    }

    /**
     * Returns an array list of all user IDs.
     * @return array list of all user IDs
     * @throws SQLException error if SQl error thrown
     */
    public static ArrayList<Integer> getUserAllUserIDs() throws SQLException {
        ArrayList<Integer> userIDs = new ArrayList<>();
        String getUserIDsStatement = "SELECT * From users";

        DBPreparedStatement.setStatement(conn, getUserIDsStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int resultUserId = rs.getInt("User_ID");
            userIDs.add(resultUserId);
        }
        return userIDs;
    }
}
