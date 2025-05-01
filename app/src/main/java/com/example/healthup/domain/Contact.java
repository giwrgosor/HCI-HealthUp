package com.example.healthup.domain;

public class Contact {
    private String name;
    private String phone;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
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


}
