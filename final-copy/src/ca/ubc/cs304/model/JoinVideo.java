package ca.ubc.cs304.model;

public class JoinVideo {
    private String videoLink;
    private String title;
    private int totalWatchTime;

    public JoinVideo(String videoLink, String title, int totalWatchTime) {
        this.videoLink = videoLink;
        this.title = title;
        this.totalWatchTime = totalWatchTime;
    }

    public int getTotalWatchTime() {
        return totalWatchTime;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoLink() {
        return videoLink;
    }
}
