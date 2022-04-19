package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.InwardAdapter;
import com.shree.maulifoods.adapter.addProductAdapter;
import com.shree.maulifoods.pojo.Inward;
import com.shree.maulifoods.pojo.PayMode;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Vendor;
import com.shree.maulifoods.pojo.addProduct;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InwardActivity extends AppCompatActivity implements View.OnClickListener {

    //region Description
    private ProgressInfo progressInfo;
    private InwardAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***MaterialInwardActivity***", Selected_Vendor = "", Selected_Product = "", Selected_Paymode = "", Sr_No_D = "",
            ProdID_D = "", Challan_Qty_D = "", Qty_D = "", Rate_D = "";
    private RecyclerView recyclerView;
    public static ArrayList<Inward> dArrayList = null;
    private ApiInterface apiInterface;
    private static HashMap<Integer, String> vendorList, paymodeList, productList;
    public static ArrayList<Vendor> dVendorArrayList = null;
    public static ArrayList<PayMode> dPaymodeArrayList = null;
    private String[] spinnerVendeorArray, spinnerPaymodeArray, spinnerProductArrary;
    private Calendar c;
    private int year, month, day;
    private TextInputEditText edt_inward_date, edt_challan_no, edt_challan_qty, edt_accepted_qty, edt_rate, edt_amount,
            edt_payment, edt_total_qty, edt_sub_amount, edt_prev_balance, edt_total_balance;
    private AutoCompleteTextView auto_txt_product, auto_txt_vendor, auto_txt_payment_mode;
    private ArrayAdapter<String> adapter = null;
    public static ArrayList<Product> dProductArrayList = null;
    private DecimalFormat df = new DecimalFormat("#.##");
    private ArrayList<addProduct> add_Product_Items = new ArrayList<addProduct>();
    private addProductAdapter rvAdapter;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inword);

        getSupportActionBar().setTitle("Material Inward");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        session = new SessionManagement(getApplicationContext());
        progressInfo = new ProgressInfo(InwardActivity.this);
        networkUtil = new NetworkUtil();

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        recyclerView = findViewById(R.id.rv_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvAdapter = new addProductAdapter(InwardActivity.this, add_Product_Items);
        recyclerView.setAdapter(rvAdapter);

        auto_txt_product = (AutoCompleteTextView) findViewById(R.id.auto_txt_product);
        auto_txt_payment_mode = (AutoCompleteTextView) findViewById(R.id.auto_txt_payment_mode);
        auto_txt_vendor = (AutoCompleteTextView) findViewById(R.id.auto_txt_vendor);
        edt_inward_date = (TextInputEditText) findViewById(R.id.edt_challan_date);
        edt_inward_date.setText(commonUtil.getCurrentedate(0));
        edt_inward_date.setOnClickListener(arg0 -> setChallanDate());

        edt_challan_no = (TextInputEditText) findViewById(R.id.edt_challan_no);
        edt_challan_qty = (TextInputEditText) findViewById(R.id.edt_challan_qty);
        edt_accepted_qty = (TextInputEditText) findViewById(R.id.edt_accepted_qty);
        edt_rate = (TextInputEditText) findViewById(R.id.edt_rate);
        edt_amount = (TextInputEditText) findViewById(R.id.edt_amount);
        edt_payment = (TextInputEditText) findViewById(R.id.edt_payment);
        edt_total_qty = (TextInputEditText) findViewById(R.id.edt_total_qty);
        edt_sub_amount = (TextInputEditText) findViewById(R.id.edt_sub_amount);
        edt_prev_balance = (TextInputEditText) findViewById(R.id.edt_prev_balance);
        edt_total_balance = (TextInputEditText) findViewById(R.id.edt_total_balance);


        getVendorList();
        getPayModeList();

        Selected_Vendor = "";
        auto_txt_vendor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Selected_Vendor = vendorList.get(position);
                    getProductList(Selected_Vendor);
                }
            }
        });
        Selected_Product = "";
        auto_txt_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Selected_Product = productList.get(position);
                }
            }
        });
        Selected_Paymode = "";
        auto_txt_payment_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Selected_Paymode = paymodeList.get(position);
                }
            }
        });

        edt_accepted_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double Qty = s.toString().trim().equals("") ? 0.00 : Double.valueOf(s.toString().trim());
                Double Rate = edt_rate.getText().toString().equals("") ? 0.00 : Double.valueOf(edt_rate.getText().toString().trim());

                edt_amount.setText(df.format(Qty * Rate));
                edt_accepted_qty.requestFocusFromTouch();
                edt_accepted_qty.setSelection(edt_accepted_qty.getText().length());
            }
        });

        edt_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double Rate = s.toString().trim().equals("") ? 0.00 : Double.valueOf(s.toString().trim());
                Double Qty = edt_accepted_qty.getText().toString().equals("") ? 0.00 : Double.valueOf(edt_accepted_qty.getText().toString().trim());

                edt_amount.setText(df.format(Qty * Rate));
                edt_rate.requestFocusFromTouch();
                edt_rate.setSelection(edt_rate.getText().length());
            }
        });

        findViewById(R.id.tv_add_items).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_items:

                if (Selected_Product.equals("")) {
                    Toast.makeText(this, "Select Product!", Toast.LENGTH_LONG).show();
                } else if (edt_challan_qty.getText().toString().equals("") || edt_challan_qty.getText().toString().equals("0")) {
                    Toast.makeText(this, "Enter Challan Qty!", Toast.LENGTH_LONG).show();
                } else if (edt_accepted_qty.getText().toString().equals("") || edt_accepted_qty.getText().toString().equals("0")) {
                    Toast.makeText(this, "Enter Accepted Qty!", Toast.LENGTH_LONG).show();
                } else if (Double.valueOf(edt_accepted_qty.getText().toString()) > Double.valueOf(edt_challan_qty.getText().toString())) {
                    Toast.makeText(this, "Accept Qty Must be Less than Challan Qty!", Toast.LENGTH_LONG).show();
                } else if (edt_rate.getText().toString().equals("") || edt_rate.getText().toString().equals("0")) {
                    Toast.makeText(this, "Enter Rate!", Toast.LENGTH_LONG).show();
                } else {
                    insertMethod(Selected_Product, auto_txt_product.getText().toString(), edt_challan_qty.getText().toString(),
                            edt_accepted_qty.getText().toString(), edt_rate.getText().toString(), edt_amount.getText().toString());

                    edt_total_qty.setText(df.format(Double.valueOf(edt_total_qty.getText().toString()) + Double.valueOf(edt_accepted_qty.getText().toString())));
                    edt_sub_amount.setText(df.format(Double.valueOf(edt_sub_amount.getText().toString()) + Double.valueOf(edt_amount.getText().toString())));
                    edt_prev_balance.setText("0");
                    edt_total_balance.setText(df.format(Double.valueOf(edt_prev_balance.getText().toString()) + Double.valueOf(edt_sub_amount.getText().toString())));

                    Selected_Product = "";
                    auto_txt_product.setText("");
                    edt_challan_qty.setText("");
                    edt_accepted_qty.setText("");
                    edt_rate.setText("");
                    edt_amount.setText("");
                }
                break;
            case R.id.btn_save:
                if (add_Product_Items.size() < 1) {
                    Toast.makeText(this, "Add Products!", Toast.LENGTH_LONG).show();
                } else if (Selected_Vendor.equals("")) {
                    Toast.makeText(this, "Select Vendor!", Toast.LENGTH_LONG).show();
                } else if (edt_challan_no.getText().toString().equals("")) {
                    Toast.makeText(this, "Enter Valid Challan No!", Toast.LENGTH_LONG).show();
                } else if (Selected_Paymode.equals("")) {
                    Toast.makeText(this, "Select Payment!", Toast.LENGTH_LONG).show();
                } else {
                    saveConfirm();
                }
                break;
        }
    }

    public void saveConfirm() {
        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
        alertConfirm.setTitle("Save");
        alertConfirm.setMessage("Are You Sure You Want Save?");
        alertConfirm.setPositiveButton("YES", (dialog, which) -> {
            saveRecord();
        });
        alertConfirm.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        alertConfirm.show();
    }

    private void insertMethod(String ProdID, String ProdName, String ChallanQty, String AcceptedQty, String Rate, String Amount) {
        addProduct product = new addProduct();
        product.setSrNo(String.valueOf(add_Product_Items.size() + 1));
        product.setProduct_ID(ProdID);
        product.setProduct_Name(ProdName);
        product.setChallanQty(ChallanQty);
        product.setAcceptedQty(AcceptedQty);
        product.setRate(Rate);
        product.setAmount(Amount);
        add_Product_Items.add(product);
        rvAdapter.notifyDataSetChanged();

        if (add_Product_Items.size() > 0) {
            ((TextView) findViewById(R.id.txt_no_record_found)).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.txt_no_record_found)).setVisibility(View.VISIBLE);
        }
    }

    private void setChallanDate() {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (dayOfMonth < 10) {
                            edt_inward_date.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        if ((monthOfYear + 1) < 10) {
                            edt_inward_date.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }
                        if ((monthOfYear + 1) < 10 && dayOfMonth < 10) {
                            edt_inward_date.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        } else if (dayOfMonth >= 10 && (monthOfYear + 1) >= 10) {
                            edt_inward_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    public void getVendorList() {

        if (networkUtil.getConnectivityStatus(InwardActivity.this).trim() == "false") {
            commonUtil.getToast(InwardActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getVendor("0").enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(Call<ArrayList<Vendor>> call, Response<ArrayList<Vendor>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dVendorArrayList = response.body();
                    vendorList = new HashMap<Integer, String>();
                    spinnerVendeorArray = new String[dVendorArrayList.size()];
                    if (dVendorArrayList.size() > 0) {
                        for (int i = 0; i < dVendorArrayList.size(); i++) {
                            vendorList.put(i, dVendorArrayList.get(i).getVendor_ID());
                            spinnerVendeorArray[i] = dVendorArrayList.get(i).getVendor_Name();
                        }
                        adapter = null;
                        adapter = new ArrayAdapter<>(InwardActivity.this, R.layout.dropdown_menu_popup_item, spinnerVendeorArray);
                        auto_txt_vendor.setAdapter(adapter);
                        if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
                            auto_txt_vendor.setText(getIntent().getExtras().getString("Vendor_Name").trim(), false);
                        }
                    } else {
                        commonUtil.getToast(InwardActivity.this, "No Vendor Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Vendor>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(InwardActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getPayModeList() {

        if (networkUtil.getConnectivityStatus(InwardActivity.this).trim() == "false") {
            commonUtil.getToast(InwardActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getPayMode("0").enqueue(new Callback<ArrayList<PayMode>>() {
                @Override
                public void onResponse(Call<ArrayList<PayMode>> call, Response<ArrayList<PayMode>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dPaymodeArrayList = response.body();
                    paymodeList = new HashMap<Integer, String>();
                    spinnerPaymodeArray = new String[dPaymodeArrayList.size()];
                    if (dVendorArrayList.size() > 0) {
                        for (int i = 0; i < dPaymodeArrayList.size(); i++) {
                            paymodeList.put(i, dPaymodeArrayList.get(i).getPayMode_ID());
                            spinnerPaymodeArray[i] = dPaymodeArrayList.get(i).getPayMode_Name();
                        }
                        adapter = null;
                        adapter = new ArrayAdapter<>(InwardActivity.this, R.layout.dropdown_menu_popup_item, spinnerPaymodeArray);
                        auto_txt_payment_mode.setAdapter(adapter);
                        if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
                            auto_txt_payment_mode.setText(getIntent().getExtras().getString("PayMode_Name").trim(), false);
                        }
                    } else {
                        commonUtil.getToast(InwardActivity.this, "No PayMode Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<PayMode>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(InwardActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getProductList(String vendorID) {

        if (networkUtil.getConnectivityStatus(InwardActivity.this).trim() == "false") {
            commonUtil.getToast(InwardActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getProductList(vendorID, "0").enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dProductArrayList = response.body();
                    productList = new HashMap<Integer, String>();
                    spinnerProductArrary = new String[dProductArrayList.size()];
                    if (dProductArrayList.size() > 0) {
                        edt_prev_balance.setText(dProductArrayList.get(0).getVendorClsAmt());
                        for (int i = 0; i < dProductArrayList.size(); i++) {
                            productList.put(i, dProductArrayList.get(i).getProduct_ID());
                            spinnerProductArrary[i] = dProductArrayList.get(i).getProduct_Name();
                        }
                        adapter = null;
                        adapter = new ArrayAdapter<>(InwardActivity.this, R.layout.dropdown_menu_popup_item, spinnerProductArrary);
                        auto_txt_product.setAdapter(adapter);
                        if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
                            auto_txt_product.setText(getIntent().getExtras().getString("Product_Name").trim(), false);
                        }
                    } else {
                        commonUtil.getToast(InwardActivity.this, "No Product Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(InwardActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    private void saveRecord() {
        Sr_No_D = "";
        ProdID_D = "";
        Challan_Qty_D = "";
        Qty_D = "";
        Rate_D = "";
        if (add_Product_Items.size() > 0) {
            for (int i = 0; i < add_Product_Items.size(); i++) {
                if (i == 0) {
                    Sr_No_D = String.valueOf((i + 1));
                    ProdID_D = add_Product_Items.get(i).getProduct_ID();
                    Challan_Qty_D = add_Product_Items.get(i).getChallanQty();
                    Qty_D = add_Product_Items.get(i).getAcceptedQty();
                    Rate_D = add_Product_Items.get(i).getRate();
                } else {
                    Sr_No_D = Sr_No_D + "#" + (i + 1);
                    ProdID_D = ProdID_D + "#" + add_Product_Items.get(i).getProduct_ID();
                    Challan_Qty_D = Challan_Qty_D + "#" + add_Product_Items.get(i).getChallanQty();
                    Qty_D = Qty_D + "#" + add_Product_Items.get(i).getAcceptedQty();
                    Rate_D = Rate_D + "#" + add_Product_Items.get(i).getRate();
                }
            }
        }

        Log.d(TAG, "Selected_Vendor: " + Selected_Vendor + ", challan_no: " + edt_challan_no.getText().toString() +
                ", challan_Dt: " + commonUtil.getdateyyyymmdd(edt_inward_date.getText().toString()) + ", Sr_No: " + Sr_No_D +
                ", ProdID_D: " + ProdID_D + ", Challan_Qty_D: " + Challan_Qty_D + ", Qty_D: " + Qty_D + ", Rate_D: " + Rate_D +
                ", Selected_Paymode: " + Selected_Paymode + ", edt_payment.getText().toString(): " + edt_payment.getText().toString() +
                ", OUTLET_ID: " + user.get(SessionManagement.COMPANY_ID) + ", User_Id: " + user.get(SessionManagement.USER_ID));

        progressInfo.ProgressShow();
        apiInterface.saveInward("S", Selected_Vendor, edt_challan_no.getText().toString(),
                commonUtil.getdateyyyymmdd(edt_inward_date.getText().toString()), Sr_No_D, ProdID_D, Challan_Qty_D, Qty_D, Rate_D,
                edt_prev_balance.getText().toString(), Selected_Paymode, edt_payment.getText().toString(),
                user.get(SessionManagement.COMPANY_ID), user.get(SessionManagement.USER_ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "message: " + response.message());
                Log.d(TAG, "body: " + response.body());
                if (response.body().equals("Success")) {
                    Toast.makeText(InwardActivity.this, "Record Saved Successfully", Toast.LENGTH_LONG).show();
                } else if (response.body().trim().equals("AlreadyExist")) {
                    Toast.makeText(InwardActivity.this, "Bill No Already Present", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InwardActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
                progressInfo.ProgressHide();

                Intent intent = new Intent(InwardActivity.this, InwardReportActivity.class);
                Bundle _bundle = ActivityOptions.makeCustomAnimation(InwardActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                startActivity(intent, _bundle);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                progressInfo.ProgressHide();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        getIntent().putExtra("Type", "Ok");
        setResult(1001, getIntent());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

}