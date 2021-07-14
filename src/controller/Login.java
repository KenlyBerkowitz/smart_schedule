package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Main;
import model.User;
import utils.DBLoginQuery;
import utils.Logger;
import utils.RunnableNextAppt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Login FXML controller
 * @author Kenly Berkowitz
 */
public class Login implements Initializable {

    ResourceBundle resourceBundle2 = ResourceBundle.getBundle("utils.Login", Locale.getDefault());

    private User user;

    private ZoneId zoneId;

    @FXML
    public Label loginLabel;

    @FXML
    public Label userIDLabel;

    @FXML
    public Label passwordLabel;

    @FXML
    public Button loginButton;

    @FXML
    private TextField userIDField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label locationLabel;

    /**
     *
     * @param event Onclick that checks the DB if it is matching username and password
     * @throws IOException Throws IO exception for FXML dialog box.
     * @throws SQLException Throws SQL exception if the SQL requests fail.
     */
    @FXML
    void loginButtonClicked(MouseEvent event) throws IOException, SQLException {
        user = DBLoginQuery.checkUserLogin(userIDField.getText().trim(), passwordField.getText().trim());

        if(user != null) {
            if(RunnableNextAppt.getNextAppointment() != null && RunnableNextAppt.apptWithin15Minutes)
                Main.attentionMessage("Appointment within 15 minutes.\nCheck blue box on next screen");
            else
                Main.attentionMessage("There are no appointments with 15 minutes!");

            new Logger(user.getUserName());
            PrimaryViewController.setUser(user);
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/primaryView.fxml"));
            stage.setScene(new Scene(root));
            stage.show();

            closeStage();
        }
        else {
            new Logger(userIDField.getText().trim(), true);
            Main.errorMessage(resourceBundle2.getString("errorMessage"));

        }
    }

    /**
     * Closes stage.
     */
    private void closeStage() {
        Stage thisStage = (Stage) userIDField.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Initializes labels based on location.
     * @param url initializes
     * @param resourceBundle initializes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
        Locale locale = Locale.getDefault();

        resourceBundle = ResourceBundle.getBundle("utils.Login", locale);
        locationLabel.setText(resourceBundle.getString("locations") + ": " + zoneId);
        loginLabel.setText(resourceBundle.getString("login"));
        userIDLabel.setText(resourceBundle.getString("userName"));
        passwordLabel.setText(resourceBundle.getString("password"));
        loginButton.setText(resourceBundle.getString("login"));

        if (locale.equals(new Locale("fr", "FR"))) {
                loginLabel.setPrefWidth(300);
                userIDLabel.setLayoutX(-20);
                passwordLabel.setLayoutX(-20);
                loginButton.setPrefWidth(100);
        }
    }
}
