package ca.ubc.cs304.model;

public class Channel extends Account {
    private final int userID;
    private final String description;
    private final int subscribers;

    public Channel(String email, String alias, String password, String livestreamLink, int userID, String desc, int subs) {
        super(email, alias, password, livestreamLink);
        this.userID = userID;
        this.description = desc;
        this.subscribers = subs;
    }

    public int getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

    public int getSubscribers() {
        return subscribers;
    }
}
