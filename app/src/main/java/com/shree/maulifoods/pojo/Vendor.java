package com.shree.maulifoods.pojo;

public class Vendor {

    String vendor_ID,vendor_Name,vendor_Address;

    public Vendor(String vendor_ID, String vendor_Name, String vendor_Address) {
        this.vendor_ID = vendor_ID;
        this.vendor_Name = vendor_Name;
        this.vendor_Address = vendor_Address;
    }

    public String getVendor_ID() {
        return vendor_ID;
    }

    public void setVendor_ID(String vendor_ID) {
        vendor_ID = vendor_ID;
    }

    public String getVendor_Name() {
        return vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        vendor_Name = vendor_Name;
    }

    public String getVendor_Address() {
        return vendor_Address;
    }

    public void setVendor_Address(String vendor_Address) {
        vendor_Address = vendor_Address;
    }
}
