package com.example.healthup.domain;

public class User {
    private String name;
    private String surname;
    private String bloodType;
    private String bloodRhFactor;
    private String address;
    private String city;
    private String zipcode;

    public User(String name, String surname, String bloodType, String bloodRhFactor, String address, String city, String zipcode) {
        this.name = name;
        this.surname = surname;
        this.bloodType = bloodType;
        this.bloodRhFactor = bloodRhFactor;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodRhFactor() {
        return bloodRhFactor;
    }

    public void setBloodRhFactor(String bloodRhFactor) {
        this.bloodRhFactor = bloodRhFactor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
