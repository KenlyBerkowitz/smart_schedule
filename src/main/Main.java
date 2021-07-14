package main;

import controller.AttentionMessageController;
import controller.ErrorMessageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import utils.DBConnection;
import utils.RunnableNextAppt;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main controller
 * @author Kenly Berkowitz
 */
public class Main extends Application {

    /**
     *
     * @param primaryStage Passes primary stage.
     * @throws Exception throws exception for FXML loader error.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/log-in.fxml"));
        primaryStage.setTitle("Smart Schedule");
        primaryStage.setScene(new Scene(root, Color.valueOf("#303030")));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     *
     * @param args Starting argument
     * @throws SQLException Throws SQL exception if SQL error.
     */
    public static void main(String[] args) throws  SQLException {
        System.out.println("Connecting to database...");
        DBConnection.startConnection();

        System.out.println("Starting appointment tracking thread...");
        Runnable runnable = new RunnableNextAppt();
        Thread thread = new Thread(runnable);
        thread.start();

        launch(args);

        RunnableNextAppt.terminateRun = true;

        DBConnection.closeConnection();

    }

    /**
     *
     * @param string passes string to initialize dialog box
     * @throws IOException throws exception for FXML loader error.
     */
    public static void errorMessage(String string) throws IOException {
        ErrorMessageController.initString(string);
        Parent root = FXMLLoader.load(Main.class.getResource("/view/errorMessageDialogBox.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Error");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     *
     * @param string passes string to initialize dialog box.
     * @throws IOException throws exception for FXML loader error.
     */
    public static void attentionMessage(String string) throws IOException {
        AttentionMessageController.initString(string);
        Parent root = FXMLLoader.load(Main.class.getResource("/view/attentionMessageDialogBox.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Attention");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

}
