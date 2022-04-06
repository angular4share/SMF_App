package com.shree.maulifoods.ui.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.PayMode;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity {

    //<editor-fold desc="Description">
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***ReceiptActivity***", Selected_Customer = "", Selected_PayMode = "", file_name_path = "";
    private TextView txt_cust_address, lay_customer_area, txt_old_balance, txt_customer_type, txt_mobile;
    private TextInputEditText edit_cheque_no, edit_rec_amount, edit_issue_bank, edit_cheque_date;
    private ApiInterface apiInterface;
    private Intent intent = null;
    private AutoCompleteTextView auto_txt_paymode, auto_txt_customer;
    private Calendar c;
    private int year, month, day;
    private HashMap<Integer, String> payModeMap;
    public ArrayList<Receipt> dCustomerArrayList = null;
    public ArrayList<PayMode> dPayModeArrayList = null;
    private ArrayAdapter<String> adapter = null;
    private String[] spinnerpayModeArrary, spinnerCustomerArrary;
    private HashMap<String, String> spinnerCustomerMap = null;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setTitle("Payment Receipt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        progressInfo = new ProgressInfo(ReceiptActivity.this);
        networkUtil = new NetworkUtil();
        session = new SessionManagement(getApplicationContext());

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txt_cust_address = ((TextView) findViewById(R.id.txt_cust_address));
        lay_customer_area = ((TextView) findViewById(R.id.txt_customer_area));
        txt_customer_type = ((TextView) findViewById(R.id.lay_receipt_txt_customer_type));
        txt_mobile = ((TextView) findViewById(R.id.lay_receipt_txt_mobile));
        txt_old_balance = ((TextView) findViewById(R.id.txt_old_balance));

        auto_txt_customer = (AutoCompleteTextView) findViewById(R.id.auto_txt_customer);
        auto_txt_customer.setOnItemClickListener((parent, view, position, id) -> {
            if (position != -1) {
                Selected_Customer = spinnerCustomerMap.get((String) parent.getItemAtPosition(position));
                Log.d(TAG, "Item: " + parent.getItemAtPosition(position) + ", Value : " + Selected_Customer);
                getActiveCustomerDetails(Selected_Customer);
            }
        });

        auto_txt_paymode = (AutoCompleteTextView) findViewById(R.id.auto_txt_paymode);
        auto_txt_paymode.setOnItemClickListener((parent, view, position, id) -> {
            if (position != -1) {
                Selected_PayMode = payModeMap.get(position);
                Log.d(TAG, "Selected_PayMode: " + Selected_PayMode);
                if (auto_txt_paymode.getAdapter().getItem(position).toString().trim().equals("Cheque")) {
                    ((LinearLayout) findViewById(R.id.lay_check_details)).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) findViewById(R.id.lay_check_details)).setVisibility(View.GONE);
                }
            }
        });

        edit_rec_amount = findViewById(R.id.edit_rec_amount);
        edit_cheque_no = findViewById(R.id.edit_cheque_no);
        edit_issue_bank = findViewById(R.id.edit_issue_bank);
        edit_cheque_date = findViewById(R.id.edit_cheque_date);
        edit_cheque_date.setText(commonUtil.getCurrentedate(0));
        edit_cheque_date.setOnClickListener(arg0 -> setChequeDate());

        //Load After dialog load
        getPaymode();
        getActiveCustomer();
    }

    private void setChequeDate() {
        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    if (dayOfMonth < 10) {
                        edit_cheque_date.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                    if ((monthOfYear + 1) < 10) {
                        edit_cheque_date.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                    }
                    if ((monthOfYear + 1) < 10 && dayOfMonth < 10) {
                        edit_cheque_date.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                    } else if (dayOfMonth >= 10 && (monthOfYear + 1) >= 10) {
                        edit_cheque_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis()-1000 * 60 * 60 * 24 * 31);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        dpd.show();
    }

    public void getPaymode() {

        if (networkUtil.getConnectivityStatus(ReceiptActivity.this).trim() == "false") {
            commonUtil.getToast(ReceiptActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getPayMode("0").enqueue(new Callback<ArrayList<PayMode>>() {
                @Override
                public void onResponse(Call<ArrayList<PayMode>> call, Response<ArrayList<PayMode>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dPayModeArrayList = response.body();
                    if (dPayModeArrayList.size() > 0) {
                        payModeMap = new HashMap<Integer, String>();
                        spinnerpayModeArrary = new String[dPayModeArrayList.size()];
                        for (int i = 0; i < dPayModeArrayList.size(); i++) {
                            payModeMap.put(i, dPayModeArrayList.get(i).getPayMode_ID());
                            spinnerpayModeArrary[i] = dPayModeArrayList.get(i).getPayMode_Name();
                        }
                        adapter = null;
                        adapter = new ArrayAdapter<>(ReceiptActivity.this, R.layout.dropdown_menu_popup_item, spinnerpayModeArrary);
                        auto_txt_paymode.setThreshold(1);
                        auto_txt_paymode.setAdapter(adapter);
                    } else {
                        commonUtil.getToast(ReceiptActivity.this, "No Product Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<PayMode>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ReceiptActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getActiveCustomer() {

        if (networkUtil.getConnectivityStatus(ReceiptActivity.this).trim() == "false") {
            commonUtil.getToast(ReceiptActivity.this, "No internet connection!");
            return;
        } else {
            Log.d(TAG, "USER_ID: " + user.get(SessionManagement.USER_ID) + ",OUTLET_ID: " + user.get(SessionManagement.OUTLET_ID));
            progressInfo.ProgressShow();
            apiInterface.getReceipt("Customer", "0", "0", user.get(SessionManagement.USER_ID), "0", user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<ArrayList<Receipt>>() {
                @Override
                public void onResponse(Call<ArrayList<Receipt>> call, Response<ArrayList<Receipt>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dCustomerArrayList = response.body();
                    if (dCustomerArrayList.size() > 0) {
                        spinnerCustomerMap = new HashMap<String, String>();
                        spinnerCustomerArrary = new String[dCustomerArrayList.size()];
                        for (int i = 0; i < dCustomerArrayList.size(); i++) {
                            spinnerCustomerArrary[i] = dCustomerArrayList.get(i).getCustomer_Name();
                            spinnerCustomerMap.put(dCustomerArrayList.get(i).getCustomer_Name(), dCustomerArrayList.get(i).getCustomer_ID());
                        }
                        adapter = null;
                        adapter = new ArrayAdapter<>(ReceiptActivity.this, R.layout.dropdown_menu_popup_item, spinnerCustomerArrary);
                        auto_txt_customer.setThreshold(1);
                        auto_txt_customer.setAdapter(adapter);
                    } else {
                        commonUtil.getToast(ReceiptActivity.this, "No Customer Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Receipt>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ReceiptActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getActiveCustomerDetails(String custCode) {
        txt_cust_address.setText("");
        lay_customer_area.setText("");
        txt_old_balance.setText("");
        txt_mobile.setText("");
        txt_customer_type.setText("");
        if (networkUtil.getConnectivityStatus(ReceiptActivity.this).trim() == "false") {
            commonUtil.getToast(ReceiptActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getReceipt("Customer", "0", "0", user.get(SessionManagement.USER_ID), custCode, user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<ArrayList<Receipt>>() {
                @Override
                public void onResponse(Call<ArrayList<Receipt>> call, Response<ArrayList<Receipt>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dCustomerArrayList = response.body();
                    if (dCustomerArrayList.size() > 0) {
                        txt_cust_address.setText(dCustomerArrayList.get(0).getCustomer_Address());
                        lay_customer_area.setText(dCustomerArrayList.get(0).getRoute_Desc());
                        txt_old_balance.setText(dCustomerArrayList.get(0).getCls_Balance());
                        txt_mobile.setText(dCustomerArrayList.get(0).getMobile_No());
                        txt_customer_type.setText(dCustomerArrayList.get(0).getCustomer_Type());
                        if (Double.valueOf(dCustomerArrayList.get(0).getCls_Balance()) > 0) {
                            txt_old_balance.setTextColor(getResources().getColor(R.color.darkred));
                        } else {
                            txt_old_balance.setTextColor(getResources().getColor(R.color.green));
                        }
                    } else {
                        commonUtil.getToast(ReceiptActivity.this, "No Customer Details Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Receipt>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ReceiptActivity.this, "Something Went Wrong!");
                }
            });
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

    public void saveValidation(View view) {
        if (Selected_Customer.equals("") || auto_txt_customer.getText().toString().trim().equals("")) {
            Toast.makeText(ReceiptActivity.this, "Select the Customer!", Toast.LENGTH_LONG).show();
        } else if (Selected_PayMode.equals("") || auto_txt_paymode.getText().toString().trim().equals("")) {
            Toast.makeText(ReceiptActivity.this, "Select the PayMode!", Toast.LENGTH_LONG).show();
        } else if (!auto_txt_paymode.getText().toString().trim().equals("") && (edit_rec_amount.getText().toString().trim().equals("0") || edit_rec_amount.getText().toString().trim().equals("") || edit_rec_amount.getText() == null)) {
            commonUtil.getToast(ReceiptActivity.this, "Enter Valid Rec Amount!");
        } else if (auto_txt_paymode.getText().toString().trim().equals("Cheque") && edit_cheque_no.getText().toString().length() < 6) {
            commonUtil.getToast(ReceiptActivity.this, "Enter Valid Cheque No!");
        } else if (auto_txt_paymode.getText().toString().trim().equals("Cheque") && (edit_issue_bank.getText().toString().trim().equals("0") || edit_issue_bank.getText().toString().trim().equals("") || edit_issue_bank.getText() == null)) {
            commonUtil.getToast(ReceiptActivity.this, "Enter Valid Cheque Issued Bank!");
        } else {
            saveConfirm();
        }
    }

    private void saveRecord() {

        Log.d(TAG, "saveRecord: Cust ID: " + Selected_Customer.trim() + ", Rec Dt: " + commonUtil.getdateyyyymmdd(commonUtil.getCurrentedate(0)) +
                ", PayMode: " + Selected_PayMode + ", Prev Balance: " + txt_old_balance.getText().toString() +
                ", Rec Amount: " + edit_rec_amount.getText().toString() + ", Cheque No: " + edit_cheque_no.getText().toString() +
                ", Cheque Date: " + commonUtil.getdateyyyymmdd(edit_cheque_date.getText().toString()) +
                ", Issue Bank: " + edit_issue_bank.getText().toString() + ", USER ID: " + user.get(SessionManagement.USER_ID));

        progressInfo.ProgressShow();
        apiInterface.saveReceipt(Selected_Customer.trim(), commonUtil.getdateyyyymmdd(commonUtil.getCurrentedate(0)),
                Selected_PayMode, txt_old_balance.getText().toString(), edit_rec_amount.getText().toString(),
                edit_cheque_no.getText().toString(), commonUtil.getdateyyyymmdd(edit_cheque_date.getText().toString()),
                edit_issue_bank.getText().toString(), user.get(SessionManagement.USER_ID), user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "message: " + response.message());
                Log.d(TAG, "body: " + response.body());
                if (response.body().equals("Success")) {
                    progressInfo.ProgressHide();
                    Toast.makeText(ReceiptActivity.this, "Record Saved Successfully", Toast.LENGTH_LONG).show();
                    intent = new Intent(ReceiptActivity.this, ReceiptReportActivity.class);
                    Bundle _bundle = ActivityOptions.makeCustomAnimation(ReceiptActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                    startActivity(intent, _bundle);
                } else if (response.body().trim().equals("AlreadyExist")) {
                    Toast.makeText(ReceiptActivity.this, "Already Saved", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReceiptActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
                progressInfo.ProgressHide();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
