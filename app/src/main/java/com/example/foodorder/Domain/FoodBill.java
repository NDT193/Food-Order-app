package com.example.foodorder.Domain;

public class FoodBill {
    private String FoodTittle;
    private String Location;
    private String Name;
    private int Quantity;
    private String Uid;
    private double Price;

    public FoodBill() {
    }

    public String getFoodTittle() {
        return FoodTittle;
    }

    public void setFoodTittle(String foodTittle) {
        FoodTittle = foodTittle;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
