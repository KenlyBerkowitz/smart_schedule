package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Kenly Berkowitz
 */
public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07VIn";

    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "U07VIn";
    private static final String password = "53689140901";
    private static Connection connection = null;

    /**
     * Starts and returns connection.
     * @return connection.
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Returns a connection object.
     * @return connection
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception ignored){
        }
    }

}
