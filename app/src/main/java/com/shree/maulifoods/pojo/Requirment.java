package com.shree.maulifoods.pojo;

public class Requirment {
    private String sr_No, mProduct_ID,mProduct_Name,order_Qty,extra_Qty,total_Qty,uoM_Name,uoM_ID,
            order_Status,inword_ID;

    public Requirment(String sr_No, String mProduct_ID, String mProduct_Name, String order_Qty,String extra_Qty,
                      String total_Qty, String uoM_Name, String uoM_ID, String order_Status,String inword_ID) {
        this.sr_No = sr_No;
        this.mProduct_ID = mProduct_ID;
        this.mProduct_Name = mProduct_Name;
        this.order_Qty = order_Qty;
        this.extra_Qty = extra_Qty;
        this.total_Qty = total_Qty;
        this.uoM_Name = uoM_Name;
        this.uoM_ID = uoM_ID;
        this.order_Status = order_Status;
        this.inword_ID=inword_ID;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getmProduct_ID() {
        return mProduct_ID;
    }

    public void setmProduct_ID(String mProduct_ID) {
        this.mProduct_ID = mProduct_ID;
    }

    public String getmProduct_Name() {
        return mProduct_Name;
    }

    public void setmProduct_Name(String mProduct_Name) {
        this.mProduct_Name = mProduct_Name;
    }

    public String getOrder_Qty() {
        return order_Qty;
    }

    public void setOrder_Qty(String order_Qty) {
        this.order_Qty = order_Qty;
    }

    public String getExtra_Qty() {
        return extra_Qty;
    }

    public void setExtra_Qty(String extra_Qty) {
        this.extra_Qty = extra_Qty;
    }

    public String getTotal_Qty() {
        return total_Qty;
    }

    public void setTotal_Qty(String total_Qty) {
        this.total_Qty = total_Qty;
    }

    public String getUoM_Name() {
        return uoM_Name;
    }

    public void setUoM_Name(String uoM_Name) {
        this.uoM_Name = uoM_Name;
    }

    public String getUoM_ID() {
        return uoM_ID;
    }

    public void setUoM_ID(String uoM_ID) {
        this.uoM_ID = uoM_ID;
    }

    public String getOrder_Status() {
        return order_Status;
    }

    public void setOrder_Status(String order_Status) {
        this.order_Status = order_Status;
    }

    public String getInword_ID() {
        return inword_ID;
    }

    public void setInword_ID(String inword_ID) {
        this.inword_ID = inword_ID;
    }


}
