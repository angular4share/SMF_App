package com.shree.maulifoods.pojo;

public class Delivery {

    private String route_Desc, requirment_Date, subs_ID, customer_Name, customer_Address, customer_City, pinCode, delivery_Status,
            payMode_Name,receive_Amount,old_Balance,cls_Balance, product_ID, product_Desc, subs_Qty, issue_Qty,stock_Qty, extra_Qty, sale_Rate, time_Type,time_Slot, freq_Name, sequence;

    public Delivery(String route_Desc, String requirment_Date, String subs_ID, String customer_Name,
                    String customer_Address, String customer_City, String pinCode, String delivery_Status,
                    String payMode_Name,String receive_Amount,String old_Balance,String cls_Balance, String product_ID,
                    String product_Desc, String subs_Qty, String issue_Qty,String stock_Qty, String extra_Qty,
                    String sale_Rate, String time_Type, String time_Slot,
                    String freq_Name, String sequence) {
        this.route_Desc = route_Desc;
        this.requirment_Date = requirment_Date;
        this.subs_ID = subs_ID;
        this.customer_Name = customer_Name;
        this.customer_Address = customer_Address;
        this.customer_City = customer_City;
        this.pinCode = pinCode;
        this.delivery_Status = delivery_Status;
        this.payMode_Name = payMode_Name;
        this.receive_Amount = receive_Amount;
        this.old_Balance = old_Balance;
        this.cls_Balance = cls_Balance;
        this.product_ID = product_ID;
        this.product_Desc = product_Desc;
        this.subs_Qty = subs_Qty;
        this.issue_Qty = issue_Qty;
        this.stock_Qty = stock_Qty;
        this.extra_Qty = extra_Qty;
        this.sale_Rate = sale_Rate;
        this.time_Type = time_Type;
        this.time_Slot = time_Slot;
        this.freq_Name = freq_Name;
        this.sequence = sequence;
    }

    public String getRoute_Desc() {
        return route_Desc;
    }

    public void setRoute_Desc(String route_Desc) {
        this.route_Desc = route_Desc;
    }

    public String getRequirment_Date() {
        return requirment_Date;
    }

    public void setRequirment_Date(String requirment_Date) {
        this.requirment_Date = requirment_Date;
    }

    public String getSubs_ID() {
        return subs_ID;
    }

    public void setSubs_ID(String subs_ID) {
        this.subs_ID = subs_ID;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getDelivery_Status() {
        return delivery_Status;
    }

    public void setDelivery_Status(String delivery_Status) {
        this.delivery_Status = delivery_Status;
    }

    public String getPayMode_Name() {
        return payMode_Name;
    }

    public void setPayMode_Name(String payMode_Name) {
        this.payMode_Name = payMode_Name;
    }

    public String getReceive_Amount() {
        return receive_Amount;
    }

    public void setReceive_Amount(String receive_Amount) {
        this.receive_Amount = receive_Amount;
    }

    public String getOld_Balance() {
        return old_Balance;
    }

    public void setOld_Balance(String old_Balance) {
        this.old_Balance = old_Balance;
    }

    public String getCls_Balance() {
        return cls_Balance;
    }

    public void setCls_Balance(String cls_Balance) {
        this.cls_Balance = cls_Balance;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_Desc() {
        return product_Desc;
    }

    public void setProduct_Desc(String product_Desc) {
        this.product_Desc = product_Desc;
    }

    public String getSubs_Qty() {
        return subs_Qty;
    }

    public void setSubs_Qty(String subs_Qty) {
        this.subs_Qty = subs_Qty;
    }

    public String getIssue_Qty() {
        return issue_Qty;
    }

    public void setIssue_Qty(String issue_Qty) {
        this.issue_Qty = issue_Qty;
    }

    public String getStock_Qty() {
        return stock_Qty;
    }

    public void setStock_Qty(String stock_Qty) {
        this.stock_Qty = stock_Qty;
    }

    public String getExtra_Qty() {
        return extra_Qty;
    }

    public void setExtra_Qty(String extra_Qty) {
        this.extra_Qty = extra_Qty;
    }

    public String getSale_Rate() {
        return sale_Rate;
    }

    public void setSale_Rate(String sale_Rate) {
        this.sale_Rate = sale_Rate;
    }

    public String getTime_Type() {
        return time_Type;
    }

    public void setTime_Type(String time_Type) {
        this.time_Type = time_Type;
    }

    public String getTime_Slot() {
        return time_Slot;
    }

    public void setTime_Slot(String time_Slot) {
        this.time_Slot = time_Slot;
    }

    public String getFreq_Name() {
        return freq_Name;
    }

    public void setFreq_Name(String freq_Name) {
        this.freq_Name = freq_Name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
