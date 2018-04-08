package com.example.android.quakereport;

import java.sql.Date;

public class OneEarthquake {
    private double magnitude;
    private String place;
    private long date;
    private String url;


    public OneEarthquake(double magnitude, String place, long date, String url){
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.url = url;

    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
