package model;

/**
 * Country model.
 * @author Kenly Berkowitz
 */
public class Country {

    private int countryId;
    private String countryName;

    /**
     *
     * @param countryId Passed int to set
     * @param countryName Passed String to set
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     *
     * @return int countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     *
     * @param countryId Sets int countryId
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
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
     * @param countryName String Sets countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


}
