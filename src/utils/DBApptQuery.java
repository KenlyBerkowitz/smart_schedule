package utils;

import controller.AddAppointmentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

/**
 * @author Kenly Berkowitz
 */
public class DBApptQuery  {

    private static final Connection conn = DBConnection.getConnection();

    /**
     *
     * @return Returns Observable List of all customers in selected month.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getAllApptByMonth() throws SQLException {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String selectByMonthStatement = "Select * FROM appointments, contacts WHERE month(Start) = month(Curdate()) AND YEAR(Start) = YEAR(Curdate()) AND appointments.Contact_ID = contacts.Contact_ID";

        DBPreparedStatement.setStatement(conn, selectByMonthStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime= rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");;
            int custID = rs.getInt("Customer_ID");;
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }

        return appointmentList;
    }

    /**
     *
     * @return Returns Observable List of all customers in selected week.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getAllApptByWeek() throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String selectByMonthStatement = "Select * FROM appointments, contacts WHERE WEEK(Start) = WEEK(curdate()) AND appointments.Contact_ID = contacts.Contact_ID";
        DBPreparedStatement.setStatement(conn, selectByMonthStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");
            int custID = rs.getInt("Customer_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }

        return appointmentList;
    }

    /**
     *
     * @param date passed date to query.
     * @return Returns Array List of all start times in selected date.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ArrayList<LocalDateTime> getAppointmentSartTimes(LocalDate date) throws SQLException {

        ZoneId zoneEST = ZoneId.of("America/New_York");
        ZonedDateTime startEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 0, 0, 0, zoneEST);
        ZonedDateTime endEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 22, 0, 0, 0, zoneEST);
        Instant startToUTC = startEST.toInstant();
        Instant endToUTC = endEST.toInstant();


        ArrayList<LocalDateTime> appointmentStartList = new ArrayList<>();
        String selectByMonthStatement = "SELECT Start FROM appointments WHERE timestamp(Start) >= timestamp('" + startToUTC.toString() + "') AND timestamp(Start) <= timestamp('" + endToUTC.toString() + "') ORDER BY Start";
        DBPreparedStatement.setStatement(conn, selectByMonthStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            appointmentStartList.add(startTime);
        }
        return appointmentStartList;
    }

    /**
     *
     * @param date Passed Date to query.
     * @return Returns Array List of all End times in selected date.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ArrayList<LocalDateTime> getAppointmentEndTimes(LocalDate date) throws SQLException {

        ZoneId zoneEST = ZoneId.of("America/New_York");
        ZonedDateTime startEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 8, 0, 0, 0, zoneEST);
        ZonedDateTime endEST = ZonedDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 22, 0, 0, 0, zoneEST);
        Instant startToUTC = startEST.toInstant();
        Instant endToUTC = endEST.toInstant();

        ArrayList<LocalDateTime> appointmentEndList =  new ArrayList<>();
        String selectByMonthStatement = "SELECT End FROM appointments WHERE timestamp(Start) >= timestamp('" + startToUTC.toString() + "') AND timestamp(Start) <= timestamp('" + endToUTC.toString() + "') ORDER BY End";
        DBPreparedStatement.setStatement(conn, selectByMonthStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            appointmentEndList.add(endTime);
        }
        return appointmentEndList;
    }

    /**
     *
     * @param tempAppointment Appointment to insert.
     * @return Boolean if insert was successful.
     */
    public static boolean insertAppointment(Appointment tempAppointment) {
        try {
            String insertAppointmentStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?)";
            DBPreparedStatement.setStatementWithKeys(conn, insertAppointmentStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();

            int appointmentID;
            String title = tempAppointment.getTitle();
            String description = tempAppointment.getDescription();
            String location = tempAppointment.getLocation();
            String type = tempAppointment.getType();
            LocalDateTime startTime = tempAppointment.getStartTime();
            LocalDateTime endTime = tempAppointment.getEndTime();
            int userID = tempAppointment.getUserID();
            int custID = tempAppointment.getCustID();
            int contactID = tempAppointment.getContactID();

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(startTime));
            ps.setTimestamp(6, Timestamp.valueOf(endTime));
            ps.setInt(7, custID);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            appointmentID = rs.getInt(1);

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + "Row(s) affected");
                AddAppointmentController.appointmentID = appointmentID;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param apptToUpdate Appointment to update.
     * @return Boolean if update was successful.
     */
    public static boolean updateAppointment(Appointment apptToUpdate) {
        try {
            String updateAppointmentStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = " + apptToUpdate.getAppointmentID();
            DBPreparedStatement.setStatement(conn, updateAppointmentStatement);
            PreparedStatement ps = DBPreparedStatement.getStatement();


            String title = apptToUpdate.getTitle();
            String description = apptToUpdate.getDescription();
            String location = apptToUpdate.getLocation();
            String type = apptToUpdate.getType();
            LocalDateTime startTime = apptToUpdate.getStartTime();
            LocalDateTime endTime = apptToUpdate.getEndTime();
            int userID = apptToUpdate.getUserID();
            int custID = apptToUpdate.getCustID();
            int contactID = apptToUpdate.getContactID();

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(startTime));
            ps.setTimestamp(6, Timestamp.valueOf(endTime));
            ps.setInt(7, custID);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);

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
     *
     * @param appointmentToDelete Appointment ID to delete.
     * @return Boolean if delete was successful.
     */
    public static boolean deleteAppointmentByApptID(Appointment appointmentToDelete)  {
        try {
            String deleteAppointmentStatement = "DELETE from appointments where Appointment_ID = '" + appointmentToDelete.getAppointmentID() + "'";
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
     *
     * @param customerToDelete Appointment by Cust ID to delete.
     * @return Boolean if delete was successful.
     */
    public static boolean deleteAppointmentByCustID(Customer customerToDelete)  {
        try {
            String deleteAppointmentStatement = "DELETE from appointments where Customer_ID = '" + customerToDelete.getCustID() + "'";
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
     *
     * @param monthNum Passed month number to get all Appointments.
     * @return Return Observable List of appointment objects.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getAllApptByMonthNumber(int monthNum) throws SQLException {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String selectByMonthStatement = "Select * FROM appointments, contacts WHERE month(Start) = " + monthNum + " AND appointments.Contact_ID = contacts.Contact_ID";

        DBPreparedStatement.setStatement(conn, selectByMonthStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime= rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");;
            int custID = rs.getInt("Customer_ID");;
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }


        return appointmentList;
    }

    /**
     *
     * @param contact Passed Contact object to get all appointments from
     * @return ObservableList of Appointments by contact.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getAllAppointmentsByContact(Contact contact) throws SQLException {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String getAllAppointmentsStatement = "Select * FROM appointments, contacts WHERE appointments.Contact_ID = " + contact.getID() + " AND appointments.Contact_ID = contacts.Contact_ID";

        DBPreparedStatement.setStatement(conn, getAllAppointmentsStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime= rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");;
            int custID = rs.getInt("Customer_ID");;
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }


        return appointmentList;
    }

    /**
     *
     * @return Observable List of Appointments by day.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getApptByDay() throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String selectByDayStatement = "Select * FROM appointments, contacts WHERE (DAY(Start) <= DAY(curdate()+1) AND DAY(Start) >= DAY(curdate())) AND appointments.Contact_ID = contacts.Contact_ID ORDER BY Start";
        DBPreparedStatement.setStatement(conn, selectByDayStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");
            int custID = rs.getInt("Customer_ID");
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }

        return appointmentList;
    }

    /**
     * Gets all Appointments.
     * @return Returns Observable List of all customers in selected month.
     * @throws SQLException Throws error if query goes wrong.
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String selectAllStatement = "Select * FROM appointments, contacts WHERE appointments.Contact_ID = contacts.Contact_ID";

        DBPreparedStatement.setStatement(conn, selectAllStatement);
        PreparedStatement ps = DBPreparedStatement.getStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contactName = rs.getString("Contact_Name");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime= rs.getTimestamp("End").toLocalDateTime();
            int userID = rs.getInt("User_ID");;
            int custID = rs.getInt("Customer_ID");;
            int contactID = rs.getInt("Contact_ID");

            Appointment tempAppointment = new Appointment(appointmentID, title, description, location, type, contactName, startTime, endTime, userID, custID, contactID);
            appointmentList.add(tempAppointment);
        }

        return appointmentList;
    }
}
