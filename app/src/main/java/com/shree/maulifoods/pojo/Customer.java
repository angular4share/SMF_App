package com.shree.maulifoods.pojo;

public class Customer {

    private String customer_ID,customer_Name,save_Type,customer_Type, customer_Address,customer_City,pinCode,
            mobile_No,mobile_No2,eMail_ID,route_ID, route_Name,route_Desc,sr_No, product_ID, product_Desc,
            qty, start_Date, freq_ID,freq_Name,freq_Days,time_Type,time_Slot_ID,time_Slot_Name,sequence;

    public Customer(String customer_ID, String customer_Name, String save_Type, String customer_Type, String customer_Address,
                    String pinCode, String mobile_No,String mobile_No2, String eMail_ID, String route_ID, String route_Name, String route_Desc,
                    String sr_No, String product_ID, String product_Desc, String qty, String start_Date, String freq_ID,
                    String freq_Name, String freq_Days,String time_Type,String time_Slot_ID, String time_Slot_Name, String sequence) {
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.save_Type = save_Type;
        this.customer_Type = customer_Type;
        this.customer_City = customer_City;
        this.customer_Address = customer_Address;
        this.pinCode = pinCode;
        this.mobile_No = mobile_No;
        this.mobile_No2 = mobile_No2;
        this.eMail_ID = eMail_ID;
        this.route_ID = route_ID;
        this.route_Name = route_Name;
        this.route_Desc = route_Desc;
        this.sr_No = sr_No;
        this.product_ID = product_ID;
        this.product_Desc = product_Desc;
        this.qty = qty;
        this.start_Date = start_Date;
        this.freq_ID = freq_ID;
        this.freq_Name = freq_Name;
        this.freq_Days = freq_Days;
        this.time_Type=time_Type;
        this.time_Slot_ID = time_Slot_ID;
        this.time_Slot_Name = time_Slot_Name;
        this.sequence = sequence;
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

    public String getSave_Type() {
        return save_Type;
    }

    public void setSave_Type(String save_Type) {
        this.save_Type = save_Type;
    }

    public String getCustomer_Type() {
        return customer_Type;
    }

    public String getMobile_No2() {
        return mobile_No2;
    }

    public void setMobile_No2(String mobile_No2) {
        this.mobile_No2 = mobile_No2;
    }

    public void setCustomer_Type(String customer_Type) {
        this.customer_Type = customer_Type;
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

    public String getMobile_No() {
        return mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        this.mobile_No = mobile_No;
    }

    public String geteMail_ID() {
        return eMail_ID;
    }

    public void seteMail_ID(String eMail_ID) {
        this.eMail_ID = eMail_ID;
    }

    public String getRoute_ID() {
        return route_ID;
    }

    public void setRoute_ID(String route_ID) {
        this.route_ID = route_ID;
    }

    public String getRoute_Name() {
        return route_Name;
    }

    public void setRoute_Name(String route_Name) {
        this.route_Name = route_Name;
    }

    public String getRoute_Desc() {
        return route_Desc;
    }

    public void setRoute_Desc(String route_Desc) {
        this.route_Desc = route_Desc;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(String start_Date) {
        this.start_Date = start_Date;
    }

    public String getFreq_ID() {
        return freq_ID;
    }

    public void setFreq_ID(String freq_ID) {
        this.freq_ID = freq_ID;
    }

    public String getFreq_Name() {
        return freq_Name;
    }

    public void setFreq_Name(String freq_Name) {
        this.freq_Name = freq_Name;
    }

    public String getFreq_Days() {
        return freq_Days;
    }

    public void setFreq_Days(String freq_Days) {
        this.freq_Days = freq_Days;
    }

    public String getTime_Type() {
        return time_Type;
    }

    public void setTime_Type(String time_Type) {
        this.time_Type = time_Type;
    }

    public String getTime_Slot_ID() {
        return time_Slot_ID;
    }

    public void setTime_Slot_ID(String time_Slot_ID) {
        this.time_Slot_ID = time_Slot_ID;
    }

    public String getTime_Slot_Name() {
        return time_Slot_Name;
    }

    public void setTime_Slot_Name(String time_Slot_Name) {
        this.time_Slot_Name = time_Slot_Name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
