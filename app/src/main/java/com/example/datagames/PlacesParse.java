package com.example.datagames;

public class PlacesParse {
    float distance;
    double lat;
    double lon;
    String name;

    public PlacesParse(float distance, double lat, double lon, String name) {
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }
    public PlacesParse(){

    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
