package com.example.healthup.domain;

import java.util.Objects;

public class Contact {
    private String name;
    private String phone;
    private static int counter = 0;
    private int id;
    private boolean isEmergency;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.id = ++counter;
    }

    public Contact(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Contact(String name, String phone, boolean isEmergency) {
        this.name = name;
        this.phone = phone;
        this.isEmergency = isEmergency;
        this.id = ++counter;
    }

    public Contact(int id, String name, String phone, boolean isEmergency) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isEmergency = isEmergency;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }

    public static String formatPhoneNumber(String phone) {
        if (phone == null) return "";

        String cleaned = phone.replaceAll("\\D", "");

        if (cleaned.length() >= 7) {
            return cleaned.substring(0, 3) + " " + cleaned.substring(3, 6) + " " + cleaned.substring(6);
        } else if (cleaned.length() >= 4) {
            return cleaned.substring(0, 3) + " " + cleaned.substring(3);
        } else {
            return cleaned;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void changeContactData(Contact newContact) {
        this.name = newContact.getName();
        this.phone = newContact.getPhone();
        this.isEmergency = newContact.isEmergency();
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) && Objects.equals(phone, contact.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }
}
