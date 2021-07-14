package model;

/**
 * Contact model.
 * @author Kenly Berkowitz
 */
public class Contact {

    private int id;
    private String name;
    private String email;

    /**
     *
     * @param id Passed int
     * @param name Passed String
     * @param email Passed String
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }

    /**
     *
     * @return int id
     */
    public int getID() {
        return id;
    }

    /**
     *
     * @param id Passe int id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email Set email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
