package ca.ubc.cs304.model;

public abstract class Account {
    private final String Email;
    private final String alias;

    private final String password;
    private final String livestreamLink;

    public Account(String email, String alias, String password, String livestreamLink) {
        this.Email = email;
        this.alias = alias;
        this.password = password;
        this.livestreamLink = livestreamLink;
    }

    public String getEmail() {
        return Email;
    }

    public String getAlias(){
        return alias;
    }

    public String getPassword() {
        return password;
    }

    public String getLivestreamLink() {
        return livestreamLink;
    }

}
