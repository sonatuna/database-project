package ca.ubc.cs304.model;

public class Watch {
    private final String videoLink;
    private final Integer watchTime;
    private final String email;

    public Watch(String videoLink, int watchTime, String email) {
        this.videoLink = videoLink;
        this.email = email;
        this.watchTime = watchTime;

    }

    public String getVideoLink() {
        return videoLink;
    }
    public int getWatchTime() {
        return watchTime;
    }
    public String getEmail() {
        return email;
    }
}
