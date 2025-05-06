package com.example.healthup.domain;

import java.io.Serializable;

public class Location implements Serializable {
    private String name;
    private double lat;
    private double lon;
    private String street;
    private String city;
    private String zipcode;
    private static int counter = 0;
    private int id;

    public Location(String name, double lat, double lon, String street, String city, String zipcode) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
        this.id = ++counter;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void changeData(String name, double latitude, double longitude, String street, String city, String zip){
        this.name = name;
        this.lat = latitude;
        this.lon = longitude;
        this.street = street;
        this.city = city;
        this.zipcode = zip;
    }

    public void changeData(Location location){
        this.name = location.getName();
        this.lat = location.getLat();
        this.lon = location.getLon();
        this.street = location.getStreet();
        this.city = location.getCity();
        this.zipcode = location.getZipcode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return Double.compare(lat, location.lat) == 0 && Double.compare(lon, location.lon) == 0 && name.equals(location.name) && street.equals(location.street) && city.equals(location.city) && zipcode.equals(location.zipcode);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Double.hashCode(lat);
        result = 31 * result + Double.hashCode(lon);
        result = 31 * result + street.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + zipcode.hashCode();
        return result;
    }
}
