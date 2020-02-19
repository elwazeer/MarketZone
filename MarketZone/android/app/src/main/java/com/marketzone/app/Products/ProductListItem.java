package com.marketzone.app.Products;

public class ProductListItem {
    private String imageId;
    private String title;
    private String price;
    private int PID;
    private String UID;

    public ProductListItem(String imageId, String title, String price, int pid, String UID) {
        this.imageId = imageId;
        this.title = title;
        this.price = price;
        this.PID = pid;
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

    public String getUID() {
        return UID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() { return price; }

    public int getPID() { return PID; }

    public  void setPrice(String price) { this.price = price;}

}
