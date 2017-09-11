package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by harmis on 26/9/16.
 */
public class Locationbean implements Serializable {


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    double distance=0.00;
    String locationName=null;
    String latitude=null;
    String type=null;

    public String getPlaceIDFromGoolge() {
        return placeIDFromGoolge;
    }

    public void setPlaceIDFromGoolge(String placeIDFromGoolge) {
        this.placeIDFromGoolge = placeIDFromGoolge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String placeIDFromGoolge=null;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    String longitude=null;
}
