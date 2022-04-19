package com.shree.maulifoods.utility;

import com.shree.maulifoods.pojo.Customer;
import com.shree.maulifoods.pojo.Delivery;
import com.shree.maulifoods.pojo.DispFrequency;
import com.shree.maulifoods.pojo.Expense;
import com.shree.maulifoods.pojo.Inward;
import com.shree.maulifoods.pojo.PayMode;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.pojo.Route;
import com.shree.maulifoods.pojo.TimeSlot;
import com.shree.maulifoods.pojo.User;
import com.shree.maulifoods.pojo.Vendor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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


    @GET("saveRequirment")
    Call<String> saveRequirment(@Query("vendor_id") String vendor_id,
                                @Query("product_id") String product_id,
                                @Query("qty") String qty,
                                @Query("extra_qty") String extra_qty,
                                @Query("uom_id") String uom_id,
                                @Query("inword_ID") String inword_ID,
                                @Query("user_ID") String user_ID,
                                @Query("outletID") String outletID);

    @GET("getPayMode")
    Call<ArrayList<PayMode>> getPayMode(@Query("paymodeID") String Paymode_ID);


    @GET("getDelivery")
    Call<ArrayList<Delivery>> getDelivery(@Query("type") String type,
                                          @Query("forDate") String forDate,
                                          @Query("empCode") String empCode,
                                          @Query("custCode") String custCode,
                                          @Query("outletID") String outletID);

    @GET("getRouteList")
    Call<ArrayList<Route>> getRouteList(@Query("outlet_id") String outlet_id);

    @GET("getProductList")
    Call<ArrayList<Product>> getProductList(@Query("vendor_id") String vendor_id,
                                            @Query("product_id") String product_id);

    @GET("getDispFrequencyList")
    Call<ArrayList<DispFrequency>> getDispFrequencyList(@Query("freq_id") String freq_id);

    @GET("getTimeSlotList")
    Call<ArrayList<TimeSlot>> getTimeSlotList(@Query("Time_Type") String Time_Type);

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
                              @Query("Sr_No") String Sr_No,
                              @Query("Product_ID") String Product_ID,
                              @Query("Qty") String Qty,
                              @Query("Start_Date") String Start_Date,
                              @Query("Freq_ID") String Freq_ID,
                              @Query("Time_Slot_ID") String Time_Slot_ID,
                              @Query("User_ID") String User_ID);

    @GET("getOTP")
    Call<String> getOTP(@Query("User_Name") String User_Name,
                        @Query("Mail_ID") String Mail_ID,
                        @Query("Mobile_No") String Mobile_No,
                        @Query("IMEI_No") String IMEI_No,
                        @Query("FCMTokenID") String FCMTokenID);

    @GET("setOTP")
    Call<String> setOTP(@Query("OTPNo") String OTPNo,
                        @Query("FCMTokenID") String FCMTokenID,
                        @Query("Mobile_No") String Mobile_No,
                        @Query("IMEI_No") String IMEI_No);

    @GET("saveInward")
    Call<String> saveInward(@Query("Save_Type") String Save_Type,
                            @Query("Vendor_ID") String Vendor_ID,
                            @Query("Bill_No") String Bill_No,
                            @Query("Bill_Date") String Bill_Date,
                            @Query("Sr_No") String Sr_No,
                            @Query("ProdID") String ProdID,
                            @Query("Challan_Qty") String Challan_Qty,
                            @Query("Qty") String Qty,
                            @Query("Rate") String Rate,
                            @Query("Previoues_Balance") String Previoues_Balance,
                            @Query("PayMode") String PayMode,
                            @Query("PayAmt") String PayAmt,
                            @Query("Outlet_ID") String Outlet_ID,
                            @Query("User_ID") String User_ID);

    @GET("getInward")
    Call<ArrayList<Inward>> getInward(@Query("type") String Type,
                                      @Query("forDate") String For_Date);

    @GET("saveDelivery")
    Call<String> saveDelivery(@Query("Subs_ID") String Sale_Dt,
                              @Query("Sale_Dt") String Subs_ID,
                              @Query("Sales_Type") String Sales_Type,
                              @Query("Prev_Balance") String Prev_Balance,
                              @Query("Sr_No") String Sr_No,
                              @Query("ProdID") String ProdID,
                              @Query("Subs_Qty") String Subs_Qty,
                              @Query("Issue_Qty") String Issue_Qty,
                              @Query("Stock_Qty") String Stock_Qty,
                              @Query("Extra_Qty") String Extra_Qty,
                              @Query("Rate") String Rate,
                              @Query("User_ID") String User_ID,
                              @Query("Outlet_ID") String Outlet_ID);

    @GET("getReceipt")
    Call<ArrayList<Receipt>> getReceipt(@Query("type") String type,
                                        @Query("fromDt") String fromDt,
                                        @Query("toDt") String toDt,
                                        @Query("empID") String empID,
                                        @Query("custID") String custID,
                                        @Query("outletID") String outletID);

    @GET("saveReceipt")
    Call<String> saveReceipt(@Query("Customer_ID") String Customer_ID,
                             @Query("Receipt_Dt") String Receipt_Dt,
                             @Query("Payment_Mode") String Payment_Mode,
                             @Query("Prev_Balance") String Prev_Balance,
                             @Query("Receipt_Amount") String Receipt_Amount,
                             @Query("Cheque_No") String Cheque_No,
                             @Query("Cheque_Date") String Cheque_Date,
                             @Query("Cheque_IssueBank") String Cheque_IssueBank,
                             @Query("Outlet_ID") String Outlet_ID,
                             @Query("User_ID") String User_ID);

    @GET("createInvoicePDF")
    Call<String> createInvoicePDF(@Query("invID") String invID
    );

    @GET("createReceiptPDF")
    Call<String> createReceiptPDF(@Query("recID") String recID
    );

    @GET("getExpense")
    Call<ArrayList<Expense>> getExpense(@Query("fromDt") String fromDt,
                                        @Query("toDt") String toDt,
                                        @Query("outletID") String outletID,
                                        @Query("type") String type);

    @GET("saveExpense")
    Call<String> saveExpense(@Query("Exp_Req_Date") String Exp_Req_Date,
                             @Query("Exp_ID") String Exp_ID,
                             @Query("Exp_Amount") String Exp_Amount,
                             @Query("PayMode_ID") String PayMode_ID,
                             @Query("Cheque_No") String Cheque_No,
                             @Query("Cheque_Date") String Cheque_Date,
                             @Query("Cheque_IssueBank") String Cheque_IssueBank,
                             @Query("Remark") String Remark,
                             @Query("User_ID") String User_ID,
                             @Query("Outlet_ID") String Outlet_ID);

    @GET("saveExpenseHead")
    Call<String> saveExpenseHead(@Query("Sys_Dt") String Sys_Dt,
                                 @Query("Exp_Name") String Exp_Name,
                                 @Query("Remark") String Remark,
                                 @Query("User_ID") String User_ID);

}

