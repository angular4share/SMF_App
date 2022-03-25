package com.shree.maulifoods.pojo;

public class Receipt {

    private String receipt_No, receipt_Dt, customer_ID, customer_Name, customer_Type,
    mobile_No,customer_Address, customer_City,payMode_Name,route_Desc, cls_Balance, receive_Amount;

    public Receipt(String receipt_No, String receipt_Dt, String customer_ID,
                   String customer_Name, String customer_Type, String mobile_No,
                   String customer_Address, String customer_City, String payMode_Name,
                   String route_Desc,String cls_Balance, String receive_Amount) {
        this.receipt_No = receipt_No;
        this.receipt_Dt = receipt_Dt;
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.customer_Type = customer_Type;
        this.mobile_No = mobile_No;
        this.customer_Address = customer_Address;
        this.customer_City = customer_City;
        this.route_Desc = route_Desc;
        this.payMode_Name = payMode_Name;
        this.cls_Balance = cls_Balance;
        this.receive_Amount = receive_Amount;
    }

    public String getReceipt_No() {
        return receipt_No;
    }

    public void setReceipt_No(String receipt_No) {
        this.receipt_No = receipt_No;
    }

    public String getReceipt_Dt() {
        return receipt_Dt;
    }

    public void setReceipt_Dt(String receipt_Dt) {
        this.receipt_Dt = receipt_Dt;
    }

    public String getCustomer_ID() {
        return customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        this.customer_ID = customer_ID;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public String getCustomer_Type() {
        return customer_Type;
    }

    public void setCustomer_Type(String customer_Type) {
        this.customer_Type = customer_Type;
    }

    public String getMobile_No() {
        return mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        this.mobile_No = mobile_No;
    }

    public String getCustomer_Address() {
        return customer_Address;
    }

    public void setCustomer_Address(String customer_Address) {
        this.customer_Address = customer_Address;
    }

    public String getCustomer_City() {
        return customer_City;
    }

    public void setCustomer_City(String customer_City) {
        this.customer_City = customer_City;
    }

    public String getPayMode_Name() {
        return payMode_Name;
    }

    public void setPayMode_Name(String payMode_Name) {
        this.payMode_Name = payMode_Name;
    }

    public String getRoute_Desc() {
        return route_Desc;
    }

    public void setRoute_Desc(String route_Desc) {
        this.route_Desc = route_Desc;
    }

    public String getCls_Balance() {
        return cls_Balance;
    }

    public void setCls_Balance(String cls_Balance) {
        this.cls_Balance = cls_Balance;
    }

    public String getReceive_Amount() {
        return receive_Amount;
    }

    public void setReceive_Amount(String receive_Amount) {
        this.receive_Amount = receive_Amount;
    }
}
