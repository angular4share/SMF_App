package com.shree.maulifoods.pojo;

public class PayMode {

    String payMode_ID,payMode_Name;

    public PayMode(String payMode_ID, String payMode_Name) {
        this.payMode_ID = payMode_ID;
        this.payMode_Name = payMode_Name;
    }

    public String getPayMode_ID() {
        return payMode_ID;
    }

    public void setPayMode_ID(String payMode_ID) {
        this.payMode_ID = payMode_ID;
    }

    public String getPayMode_Name() {
        return payMode_Name;
    }

    public void setPayMode_Name(String payMode_Name) {
        this.payMode_Name = payMode_Name;
    }
}
