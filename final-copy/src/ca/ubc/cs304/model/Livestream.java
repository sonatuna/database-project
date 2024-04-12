package model;

public class Livestream {
    private final String link;
    private final int likes;
    private final int views;
    private final String email;

    public Livestream(String link, int likes, int views, String email) {
        this.link = link;
        this.likes = likes;
        this.views = views;
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public String getEmail() {
        return email;
    }
}
