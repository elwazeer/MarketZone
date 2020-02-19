package com.marketzone.app;




public class orderListAdmin {
    private String Orderno;
    private String Address;
    private String time;
    private String user;

    public orderListAdmin(String Orderno, String Address, String time,String user) {
        this.Orderno = Orderno;
        this.Address = Address;
        this.time = time;
        this.user = user;
    }

    public String getOrderno() {
        return Orderno;
    }

    public void setOrderno(String Orderno) {
        this.Orderno = Orderno;
    }

    public String getAddress() { return Address; }

    public void setAddress(String Address) { this.Address = Address;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}
