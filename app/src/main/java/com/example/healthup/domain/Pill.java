package com.example.healthup.domain;

import java.util.HashMap;

public class Pill {
    private static int idCounter = 1;
    private int id;
    private String name;

    private HashMap<String, boolean[]> weeklySchedule = new HashMap<>();

    public Pill() {
        this.id = idCounter++;
        for (String day : new String[]{"ΔΕΥ", "ΤΡΙ", "ΤΕΤ", "ΠΕΜ", "ΠΑΡ", "ΣΑΒ", "ΚΥΡ"}) {
            weeklySchedule.put(day, new boolean[6]);
        }
    }

    public Pill(String name) {
        this();
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setScheduleForDay(String day, boolean[] schedule) {
        weeklySchedule.put(day, schedule);
    }

    public boolean[] getScheduleForDay(String day) {
        return weeklySchedule.get(day);
    }

    public HashMap<String, boolean[]> getWeeklySchedule() {
        return weeklySchedule;
    }

    public void changePillData(Pill newPill) {
        this.name = newPill.getName();
        this.weeklySchedule = newPill.getWeeklySchedule();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pill) {
            Pill other = (Pill) obj;
            return this.id == other.id;
        }
        return false;
    }
}
