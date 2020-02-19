package com.marketzone.app.Cart;

public class CustomList {
    private String imageId;
    private String title;
    private String price;
    private int quantity;
    private int PID;
    private String UID;

    public CustomList(String imageId, String title, String price, int PID, int quantity, String UID) {
        this.imageId = imageId;
        this.title = title;
        this.price = price;
        this.PID = PID;
        this.quantity = quantity;
        this.UID = UID;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() { return price; }

    public  void setPrice(String price) { this.price = price;}

    public String getUID() {
        return UID;
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity){this.quantity = quantity;}

    public int getPID() {
        return PID;
    }

}