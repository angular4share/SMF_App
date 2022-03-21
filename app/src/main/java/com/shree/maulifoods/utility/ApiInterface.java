package com.shree.maulifoods.utility;

import com.shree.maulifoods.pojo.Customer;
import com.shree.maulifoods.pojo.Delivery;
import com.shree.maulifoods.pojo.DispFrequency;
import com.shree.maulifoods.pojo.Inword;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.pojo.Route;
import com.shree.maulifoods.pojo.TimeSlot;
import com.shree.maulifoods.pojo.User;
import com.shree.maulifoods.pojo.Vendor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getDeviceRegistered")
    Call<List<User>> deviceRegistered(@Query("imei_no") String imei_no);

    @GET("getLoginCredintial")
    Call<List<User>> loginCredintial(@Query("user_id") String user_id,
                                     @Query("password") String password,
                                     @Query("imei_no") String imei_no,
                                     @Query("fcm_id") String fcm_id);

    @GET("getRequirment")
    Call<ArrayList<Requirment>> getRequirment(@Query("type") String Type,
                                              @Query("forDate") String For_Date);

    @GET("getVendor")
    Call<ArrayList<Vendor>> getVendor(@Query("vendorID") String Vendor_ID);

    @GET("getInword")
    Call<ArrayList<Inword>> getInword(@Query("type") String Type,
                                      @Query("forDate") String For_Date);

    @GET("saveRequirment")
    Call<String> saveRequirment(@Query("requirment_date") String requirment_date,
                                @Query("vendor_id") String vendor_id,
                                @Query("product_id") String product_id,
                                @Query("qty") String qty,
                                @Query("extra_qty") String extra_qty,
                                @Query("uom_id") String uom_id,
                                @Query("inword_ID") String inword_ID,
                                @Query("user_ID") String user_ID);

    @GET("saveInword")
    Call<String> saveInword(@Query("inword_id") String inword_id,
                            @Query("product_id") String product_id,
                            @Query("vendor_id") String vendor_id,
                            @Query("challan_qty") String challan_qty,
                            @Query("challan_extra_qty") String challan_extra_qty,
                            @Query("bill_no") String bill_no,
                            @Query("bill_date") String bill_date,
                            @Query("user_id") String user_id);


    @GET("getDelivery")
    Call<ArrayList<Delivery>> getDelivery(@Query("type") String Type,
                                          @Query("forDate") String For_Date,
                                          @Query("empCode") String Emp_Code);


    @GET("getRouteList")
    Call<ArrayList<Route>> getRouteList(@Query("outlet_id") String outlet_id);

    @GET("getProductList")
    Call<ArrayList<Product>> getProductList(@Query("product_id") String product_id);

    @GET("getDispFrequencyList")
    Call<ArrayList<DispFrequency>> getDispFrequencyList(@Query("freq_id") String freq_id);

    @GET("getTimeSlotList")
    Call<ArrayList<TimeSlot>> getTimeSlotList(@Query("slot_id") String slot_id);

    @GET("getCustomerList")
    Call<ArrayList<Customer>> getCustomerList(@Query("outlet_id") String outlet_id,
                                          @Query("customer_id") String customer_id,
                                          @Query("type") String type);

    @GET("saveCustomer")
    Call<String> saveCustomer(@Query("Customer_ID") String Customer_ID,
                              @Query("Customer_Name") String Customer_Name,
                              @Query("Save_Type") String Save_Type,
                              @Query("Customer_Type") String Customer_Type,
                              @Query("Customer_Address") String Customer_Address,
                              @Query("Customer_City") String Customer_City,
                              @Query("PinCode") String PinCode,
                              @Query("Mobile_No") String Mobile_No,
                              @Query("Mobile_No2") String Mobile_No2,
                              @Query("EMail_ID") String EMail_ID,
                              @Query("Route_ID") String Route_ID,
                              @Query("Subs_ID") String Subs_ID,
                              @Query("Sr_No") String Sr_No,
                              @Query("Product_ID") String Product_ID,
                              @Query("Qty") String Qty,
                              @Query("Start_Date") String Start_Date,
                              @Query("Freq_ID") String Freq_ID,
                              @Query("Time_Slot_ID") String Time_Slot_ID,
                              @Query("User_ID") String User_ID);

}

