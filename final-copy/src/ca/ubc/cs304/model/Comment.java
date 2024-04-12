package model;

public class Comment {
    private final String time;
    private final String email;
    private final String videoLink;
    private final String text;

    public Comment(String time, String email, String videoLink, String text) {
        this.time = time;
        this.email = email;
        this.videoLink = videoLink;
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getText() {
        return text;
    }



}
