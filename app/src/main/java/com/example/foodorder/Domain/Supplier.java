package com.example.foodorder.Domain;

public class Supplier {

    private  int IdSup;
    private String Name;

    public Supplier() {
    }

    public Supplier(int idSup, String name) {
        IdSup = idSup;
        Name = name;
    }

    public int getIdSup() {
        return IdSup;
    }

    public void setIdSup(int idSup) {
        IdSup = idSup;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
