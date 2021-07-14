package utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Kenly Berkowitz
 */
public class Logger {

    private final LocalDateTime dateTime;
    private final String user;
    private boolean failedLogin = false;

    /**
     * Constructs a Logger class with one parameter.
     * @param user sets User.
     */
    public Logger(String user) {
        this.user = user;
        this.dateTime = LocalDateTime.now();

        printLog();
    }

    /**
     * Constructs a Logger class with two parameters.
     * @param user Passed user to set.
     * @param failedLogin Boolean to set is the login failed.
     */
    public Logger(String user, boolean failedLogin) {
        this.user = user;
        this.dateTime = LocalDateTime.now();
        this.failedLogin = failedLogin;

        printLog();
    }

    /**
     * Prints the log dateTime, user name and if it was successful.
     */
    private void printLog() {
        String logInfo;
        if (failedLogin)
            if (user.isEmpty())
                logInfo = dateTime + "   null    Failed\n";
            else
                logInfo = dateTime + "   " + user + "   " + " Failed\n";
        else
            logInfo = dateTime + "   " + user + "   " + " Success\n";
        String path = "../login_activity.txt";

        try {
            File file = new File(path);

            if (!file.exists() && file.createNewFile())
                System.out.println("Created new file: " + path);

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(logInfo);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


