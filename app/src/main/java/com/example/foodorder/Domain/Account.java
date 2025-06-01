package com.example.foodorder.Domain;

import java.util.Map;

public class Account {
    private String Email;
    private boolean IsAdmin;
    private String Uid;
    private String Name;
    private String Number;
    private Map<String, String> Location;

    public Account() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Map<String, String> getLocation() {
        return Location;
    }

    public void setLocation(Map<String, String> location) {
        Location = location;
    }
}
