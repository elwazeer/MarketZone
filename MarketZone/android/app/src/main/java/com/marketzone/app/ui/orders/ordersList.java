package com.marketzone.app.ui.orders;

public class ordersList {
    private String OrderId;
    private String Status;
    private String time;
    private String userid;
    private String Address;

    public ordersList(String OrderId, String Status, String time, String userid, String Address) {
        this.OrderId = OrderId;
        this.Status = Status;
        this.time = time;
        this.userid = userid;
        this.Address = Address;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
