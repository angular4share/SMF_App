package com.shree.maulifoods.pojo;

public class Inword {

    private String sr_No, inword_ID,requirment_Date,mProduct_ID,mProduct_Name,order_Qty,extra_Qty,total_Qty,
            uoM_Name,vendor_ID,vendor_Name,bill_No,bill_Date,already;

    public Inword(String sr_No, String inword_ID,String requirment_Date,String mProduct_ID,
                  String mProduct_Name,String order_Qty,String extra_Qty,String total_Qty,String uoM_Name,String vendor_ID,
                  String vendor_Name,String bill_No,String bill_Date,String already) {
        this.sr_No = sr_No;
        this.inword_ID=inword_ID;
        this.requirment_Date=requirment_Date;
        this.mProduct_ID = mProduct_ID;
        this.mProduct_Name = mProduct_Name;
        this.order_Qty = order_Qty;
        this.extra_Qty = extra_Qty;
        this.total_Qty = total_Qty;
        this.uoM_Name = uoM_Name;
        this.vendor_ID = vendor_ID;
        this.vendor_Name = vendor_Name;
        this.bill_No = bill_No;
        this.bill_Date = bill_Date;
        this.already = already;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getInword_ID() {
        return inword_ID;
    }

    public void setInword_ID(String inword_ID) {
        this.inword_ID = inword_ID;
    }

    public String getRequirment_Date() {
        return requirment_Date;
    }

    public void setRequirment_Date(String requirment_Date) {
        this.requirment_Date = requirment_Date;
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

    public String getVendor_ID() {
        return vendor_ID;
    }

    public void setVendor_ID(String vendor_ID) {
        this.vendor_ID = vendor_ID;
    }

    public String getVendor_Name() {
        return vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        this.vendor_Name = vendor_Name;
    }

    public String getBill_No() {
        return bill_No;
    }

    public void setBill_No(String bill_No) {
        this.bill_No = bill_No;
    }

    public String getBill_Date() {
        return bill_Date;
    }

    public void setBill_Date(String bill_Date) {
        this.bill_Date = bill_Date;
    }

    public String getAlready() {
        return already;
    }

    public void setAlready(String already) {
        this.already = already;
    }
}
