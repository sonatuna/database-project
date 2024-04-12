package ca.ubc.cs304.model;

public class Video {
    private final String videoLink;
    private final String title;
    private final int likes;
    private final String email;
    private final String sponsor;

    public Video(String videoLink, String title, int likes, String email, String sponsor) {
        this.videoLink = videoLink;
        this.title = title;
        this.likes = likes;
        this.email = email;
        this.sponsor = sponsor;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getTitle() {
        return title;
    }

    public int getLikes() {
        return likes;
    }

    public String getEmail() {
        return email;
    }

    public String getSponsor() {
        return sponsor;
    }
}
