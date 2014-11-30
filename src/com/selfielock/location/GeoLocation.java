package com.selfielock.location;

import java.io.Serializable;

public class GeoLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private double latitude;
    private double longitude;
    private String email;
    
    public GeoLocation()
    {
        super();
    }
    
    public GeoLocation(double latitude, double longitude, String email)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
