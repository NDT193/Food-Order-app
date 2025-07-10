package com.example.foodorder.Domain;

public class Comment {
    private String Uid;
    private String Valued;
    private double Star;
    private int FoodId;
    private  String User;

    private String key;

    public Comment() {
    }

    public Comment(String uid, String valued, double star, int foodId, String user) {
        Uid = uid;
        Valued = valued;
        Star = star;
        FoodId = foodId;
        User = user;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getValued() {
        return Valued;
    }

    public void setValued(String valued) {
        Valued = valued;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getFoodId() {
        return FoodId;
    }

    public void setFoodId(int foodId) {
        FoodId = foodId;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
