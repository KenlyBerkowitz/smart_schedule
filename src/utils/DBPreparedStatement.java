package utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Kenly Berkowitz
 */
public class DBPreparedStatement {
    private static PreparedStatement statement;

    /**
     * Sets prepared statement
     * @param connection passed connection.
     * @param sqlStatement passed statement to prepare.
     * @throws SQLException error thrown is query error.
     */
    public static void setStatement(Connection connection, String sqlStatement) throws SQLException {
        statement = connection.prepareStatement(sqlStatement);
    }

    /**
     * Sets the prepared statement with keys.
     * @param connection passed connection
     * @param sqlStatement statement to prepare with keys.
     * @throws SQLException error thrown is query error.
     */
    public static void setStatementWithKeys(Connection connection, String sqlStatement) throws SQLException {
        statement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
    }

    /**
     * Returns the created prepared statement.
     * @return prepared statement.
     */
    public static PreparedStatement getStatement() {
        return statement;
    }
}
