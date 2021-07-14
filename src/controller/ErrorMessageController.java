package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;


import java.net.URL;
import java.util.ResourceBundle;


/**
 * Error message dialog box.
 * @author Kenly Berkowitz
 */
public class  ErrorMessageController implements Initializable {

    public static String string;

    @FXML
    public Button okBtn;

    @FXML
    public Label messageLabel;

    /**
     * @param url used to initialize
     * @param resourceBundle used to initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel.setText(string);
    }

    /**
     * @param str sets error message

     */
    public static void initString(String str) {
        string = str;
    }

    /**
     * @param mouseEvent closes stage
     */
    @FXML
    public void exitMessageBtn(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}
