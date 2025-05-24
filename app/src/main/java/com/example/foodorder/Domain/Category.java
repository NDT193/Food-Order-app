package com.example.foodorder.Domain;

public class Category {
    private int id;
    private  String InmagePath;
    private String name;

    public Category() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInmagePath() {
        return InmagePath;
    }

    public void setInmagePath(String inmagePath) {
        InmagePath = inmagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
