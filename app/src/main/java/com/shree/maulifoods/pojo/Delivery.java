package com.shree.maulifoods.pojo;

public class Delivery {

    private String sr_No, route_Desc, requirment_Date, subs_ID, customer_Name, customer_Address,delivery_Status, prv_Bal, heading;

    public Delivery(String sr_No, String route_Desc, String requirment_Date, String subs_ID, String customer_Name,
                    String customer_Address,String delivery_Status, String prv_Bal, String heading) {
        this.sr_No = sr_No;
        this.route_Desc = route_Desc;
        this.requirment_Date = requirment_Date;
        this.subs_ID = subs_ID;
        this.customer_Name = customer_Name;
        this.customer_Address = customer_Address;
        this.delivery_Status = delivery_Status;
        this.prv_Bal = prv_Bal;
        this.heading = heading;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
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

    public String getDelivery_Status() {
        return delivery_Status;
    }

    public void setDelivery_Status(String delivery_Status) {
        this.delivery_Status = delivery_Status;
    }

    public String getPrv_Bal() {
        return prv_Bal;
    }

    public void setPrv_Bal(String prv_Bal) {
        this.prv_Bal = prv_Bal;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
