package com.example.healthup.domain;

import java.util.Objects;

public class Pill {
    private String name;
    private int id;
    private static int counter = 0;

    public Pill(String name) {
        this.name = name;
        this.id = ++counter;
    }

    public Pill(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changePillData(Pill newPill) {
        this.name = newPill.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pill)) return false;
        Pill pill = (Pill) o;
        return Objects.equals(name, pill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
