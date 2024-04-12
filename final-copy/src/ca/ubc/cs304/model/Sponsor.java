package model;

public class Sponsor {
    private final String sponsorName;
    private final String sponsorEmail;
    private final String country;

    public Sponsor(String sponsorName, String sponsorEmail, String country) {
        this.sponsorName = sponsorName;
        this.sponsorEmail = sponsorEmail;
        this.country = country;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public String getSponsorEmail() {
        return sponsorEmail;
    }

    public String getCountry() {
        return country;
    }
}
