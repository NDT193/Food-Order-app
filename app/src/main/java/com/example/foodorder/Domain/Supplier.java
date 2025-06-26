package com.example.foodorder.Domain;

public class Supplier {

    private  int idSup;
    private String supName;

    public Supplier() {
    }

    public Supplier(int idSup, String supName) {
        this.idSup = idSup;
        this.supName = supName;
    }

    public int getIdSup() {
        return idSup;
    }

    public void setIdSup(int idSup) {
        this.idSup = idSup;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }
}
