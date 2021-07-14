package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Attention Message dialog box controller.
 * @author Kenly Berkowitz
 */
public class AttentionMessageController implements Initializable {

    public static String string;

    @FXML
    private Button okBtn;

    @FXML
    private Label messageLabel;

    /**
     *
     * @param event closes stage
     */
    @FXML
    void exitMessageBtn(MouseEvent event) {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * @param url used to initialize
     * @param resourceBundle used to initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageLabel.setText(string);
    }

    /**
     *
     * @param str Passed string to be initialized in the dialog box.
     */
    public static void initString(String str) {
        string = str;
    }

}