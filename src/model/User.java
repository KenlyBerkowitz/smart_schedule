package model;

/**
 * User model.
 * @author Kenly Berkowitz
 */
public class User {

    private String userName;
    private int userID;

    /**
     *
     * @param userName sets userName
     * @param userID sets userID
     */
    public User(String userName, int userID) {
        this.userName = userName;
        this.userID = userID;
    }

    /**
     *
     * @return String userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName Sets userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return int userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID Sets in userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

}
