package com.shree.maulifoods.pojo;

public class Product {

    private String sr_No, product_ID,product_Name,purRate,saleRate,vendorClsAmt;

    public Product(String sr_No, String product_ID, String product_Name,String purRate,String saleRate,String vendorClsAmt) {
        this.sr_No = sr_No;
        this.product_ID = product_ID;
        this.product_Name = product_Name;
        this.purRate = purRate;
        this.saleRate = saleRate;
        this.vendorClsAmt = vendorClsAmt;
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

    public String getProduct_Name() {
        return product_Name;
    }

    public void setProduct_Name(String product_Name) {
        this.product_Name = product_Name;
    }

    public String getPurRate() {
        return purRate;
    }

    public void setPurRate(String purRate) {
        this.purRate = purRate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getVendorClsAmt() {
        return vendorClsAmt;
    }

    public void setVendorClsAmt(String vendorClsAmt) {
        this.vendorClsAmt = vendorClsAmt;
    }
}
