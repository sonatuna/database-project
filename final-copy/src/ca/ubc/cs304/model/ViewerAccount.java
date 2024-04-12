package ca.ubc.cs304.model;

public class ViewerAccount extends Account {
    private final int userId;
    private final String plan;

    public ViewerAccount(String email, String alias, String password, String livestreamLink, int userId, String plan) {
        super(email,alias,password, livestreamLink);
        this.userId = userId;
        this.plan = plan;
    }

    public int getUserId() {
        return userId;
    }

    public String getPlan() {
        return plan;
    }
}
