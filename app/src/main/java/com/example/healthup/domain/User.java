package com.example.healthup.domain;

public class User {
    private String name;
    private String surname;
    private String bloodType;
    private String bloodRhFactor;

    public User(String name, String surname, String bloodType, String bloodRhFactor) {
        this.name = name;
        this.surname = surname;
        this.bloodType = bloodType;
        this.bloodRhFactor = bloodRhFactor;
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
}
