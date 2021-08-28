package com.example.quake;

import java.util.Date;

public class earthquake {
    private final String place;
    private final long date;
    private  final  double magnitude;
    private final String webLink;

    public earthquake(double magnitude, String place, long date,String webLink) {
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.webLink=webLink;
    }

    public Date getDate() {
        return (new Date(date));
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public String getWebLink() {
        return webLink;
    }
}
