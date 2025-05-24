package com.example.foodorder.Domain;

public class Time {
    private int id;
    private String value;

    @Override
    public String toString() {
        return value;
    }

    public Time() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
