package com.example.foodorder.Domain;

public class Category {
    private int Id;
    private int IdSup;
    private String ImagePath;
    private String Name;

    public Category() {
    }



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdSup() {
        return IdSup;
    }

    public void setIdSup(int idSup) {
        IdSup = idSup;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
