package model;

public class Advertisement {
    private final int adID;
    private final double length;
    private String sponsorName;
    private double compensation;

    public Advertisement(int ID, double len, String sponsor, double comp) {
        this.adID = ID;
        this.length = len;
        this.sponsorName = sponsor;
        this.compensation = comp;
    }

    public int getAdID() {
        return adID;
    }

    public double getLength() {
        return length;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public double getCompensation() {
        return compensation;
    }

}
