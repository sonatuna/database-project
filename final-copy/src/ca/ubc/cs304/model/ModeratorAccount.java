package ca.ubc.cs304.model;

public class ModeratorAccount extends Account {
    private final int rank;
    private final int employeeID;
    public ModeratorAccount(String email, String alias, String password, String livestreamLink, int rank, int employeeID) {
        super(email, alias, password, livestreamLink);
        this.rank = rank;
        this.employeeID = employeeID;
    }

    public int getRank() {
        return rank;
    }

    public int getEmployeeID() {
        return employeeID;
    }
}
