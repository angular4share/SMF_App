package com.shree.maulifoods.pojo;

public class Inword {

    String sr_No, inward_ID, vendor_ID, vendor_Name, bill_No, bill_Date, product_ID, product_Desc, uoM_Name, accept_Qty,
            challan_Qty,rate,amount,old_Amount,total_Amount, payment_Mode, payment_Amt;

    public Inword(String sr_No, String inward_ID, String vendor_ID, String vendor_Name, String bill_No, String bill_Date,
                  String product_ID, String product_Desc, String uoM_Name, String accept_Qty, String challan_Qty, String rate,
                  String amount,String old_Amount, String total_Amount, String payment_Mode, String payment_Amt) {
        this.sr_No = sr_No;
        this.inward_ID = inward_ID;
        this.vendor_ID = vendor_ID;
        this.vendor_Name = vendor_Name;
        this.bill_No = bill_No;
        this.bill_Date = bill_Date;
        this.product_ID = product_ID;
        this.product_Desc = product_Desc;
        this.uoM_Name = uoM_Name;
        this.accept_Qty = accept_Qty;
        this.challan_Qty = challan_Qty;
        this.rate = rate;
        this.old_Amount = old_Amount;
        this.amount = amount;
        this.total_Amount = total_Amount;
        this.payment_Mode = payment_Mode;
        this.payment_Amt = payment_Amt;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getInward_ID() {
        return inward_ID;
    }

    public void setInward_ID(String inward_ID) {
        this.inward_ID = inward_ID;
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

    public String getUoM_Name() {
        return uoM_Name;
    }

    public void setUoM_Name(String uoM_Name) {
        this.uoM_Name = uoM_Name;
    }

    public String getAccept_Qty() {
        return accept_Qty;
    }

    public void setAccept_Qty(String accept_Qty) {
        this.accept_Qty = accept_Qty;
    }

    public String getChallan_Qty() {
        return challan_Qty;
    }

    public void setChallan_Qty(String challan_Qty) {
        this.challan_Qty = challan_Qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOld_Amount() {
        return old_Amount;
    }

    public void setOld_Amount(String old_Amount) {
        this.old_Amount = old_Amount;
    }

    public String getTotal_Amount() {
        return total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        this.total_Amount = total_Amount;
    }

    public String getPayment_Mode() {
        return payment_Mode;
    }

    public void setPayment_Mode(String payment_Mode) {
        this.payment_Mode = payment_Mode;
    }

    public String getPayment_Amt() {
        return payment_Amt;
    }

    public void setPayment_Amt(String payment_Amt) {
        this.payment_Amt = payment_Amt;
    }
}
