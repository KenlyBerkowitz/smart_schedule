package model;

/**
 * First Level division model.
 * @author Kenly Berkowitz
 */
public class FirstLevelDivision {

    private String division;
    private int divisionId;

    /**
     *
     * @param division Passed string to set division
     * @param divisionId Passed int to set divisionId
     */
    public FirstLevelDivision(String division, int divisionId) {
        this.division = division;
        this.divisionId = divisionId;
    }

    /**
     *
     * @return String division
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     * @param division Set String division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @return int divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     *
     * @param divisionId set int divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

}
