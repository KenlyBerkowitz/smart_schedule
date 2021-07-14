package model;

/**
 * Customer model.
 * @author Kenly Berkowitz
 */
public class Customer {
    private int custID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionID;
    private String stateName;
    private String countryName;

    /**
     *
     * @param custID Passed int
     * @param name Passed String
     * @param address Passed String
     * @param postalCode Passed String
     * @param phoneNumber Passed String
     * @param divisionID Passed int
     * @param stateName Passed String
     * @param countryName Passed String
     */
    public Customer(int custID, String name, String address, String postalCode, String phoneNumber, int divisionID, String stateName, String countryName) {
        this.custID = custID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = formatPhoneNumber(phoneNumber);
        this.divisionID = divisionID;
        this.stateName = stateName;
        this.countryName = countryName;
    }

    /**
     *
     * @return String stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**
     *
     * @param stateName Sets stateName
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     *
     * @return String countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     *
     * @param countryName Sets countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     *
     * @return int custID
     */
    public int getCustID() {
        return custID;
    }

    /**
     *
     * @param custID Sets custID
     */
    public void setCustID(int custID) {
        this.custID = custID;
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
     * @param name Sets name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return String address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address Sets address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return String postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode Sets postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     *
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber Sets phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return int divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @param divisionID Sets divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     *
     * @param phoneNum Passes phone number to insert hyphens
     * @return returns String of new phoneNum
     */
    private String formatPhoneNumber(String phoneNum) {
        if (!phoneNum.contains("-")){
            StringBuilder stringBuilder = new StringBuilder(phoneNum);
            if(phoneNum.length() == 10) {
                stringBuilder.insert(3, '-');
                stringBuilder.insert(7, '-');
            }
            else {
                stringBuilder.insert(2, '-');
                stringBuilder.insert(6, '-');
                stringBuilder.insert(10, '-');
            }
            return stringBuilder.toString();
        }
        return phoneNum;
    }

}
