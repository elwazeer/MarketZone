package com.marketzone.app;

public class productList {

    private String PID;
    private String img;
    private String name;
    private String price;

    public productList(String PID, String price, String img, String name) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.PID = PID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
