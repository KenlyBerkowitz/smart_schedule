package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Kenly Berkowitz
 */
public class DBCountryStateQueries {
    private static final Connection conn = DBConnection.getConnection();

    //query to get all countries and return observable list
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        String getCountriesQuery = "Select * FROM countries";

        DBPreparedStatement.setStatement(conn, getCountriesQuery);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int tempCountryID = rs.getInt("Country_ID");
            String tempCountryName = rs.getString("Country");
            Country tempCountry = new Country(tempCountryID, tempCountryName);
            countryList.add(tempCountry);
        }
        return countryList;
    }

    /**
     * Gets all First Level Division objects - states and provinces.
     * @return Observable List of FirstLevelDivision firstLevel
     * @throws SQLException Error in SQL Query.
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivision> firstLevel = FXCollections.observableArrayList();
        String getAllCountriesQuery = "SELECT * FROM first_level_divisions";

        DBPreparedStatement.setStatement(conn, getAllCountriesQuery);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int tempDivisionID = rs.getInt("Division_ID");
            String tempDivision = rs.getString("Division");
            FirstLevelDivision tempFLDivision = new FirstLevelDivision(tempDivision, tempDivisionID);
            firstLevel.add(tempFLDivision);
        }
        return firstLevel;
    }

    /**
     * Gets all First Level Division objects by country.
     * @param countryNum Passed country number
     * @return Observable List of FirstLevelDivision
     * @throws SQLException error in SQL query
     */
    public static ObservableList<FirstLevelDivision> getFLDivisionsByCountry(int countryNum) throws SQLException {
        ObservableList<FirstLevelDivision> firstLevel = FXCollections.observableArrayList();
        String getCountriesQueries = "Select * FROM first_level_divisions WHERE COUNTRY_ID = " + countryNum;

        DBPreparedStatement.setStatement(conn, getCountriesQueries);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int tempDivisionID = rs.getInt("Division_ID");
            String tempDivision = rs.getString("Division");
            FirstLevelDivision tempFLDivision = new FirstLevelDivision(tempDivision, tempDivisionID);
            firstLevel.add(tempFLDivision);
        }
        return firstLevel;
    }
}
