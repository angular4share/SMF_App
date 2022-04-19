package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Expense;
import com.shree.maulifoods.pojo.PayMode;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseActivity extends AppCompatActivity {

    //<editor-fold desc="Description">
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***ExpenseActivity***", Selected_Expense = "", Selected_PayMode = "";
    private TextInputEditText edit_remark, edit_cheque_no, edit_rec_amount, edit_issue_bank, edit_cheque_date;
    private ApiInterface apiInterface;
    private Intent intent = null;
    private AutoCompleteTextView auto_txt_paymode, auto_txt_expense_head;
    private Calendar c;
    private int year, month, day;
    private HashMap<Integer, String> payModeMap;
    public ArrayList<Expense> dExpenseArrayList = null;
    public ArrayList<PayMode> dPayModeArrayList = null;
    private ArrayAdapter<String> adapter = null;
    private String[] spinnerpayModeArrary, spinnerExpenseHeadArrary;
    private HashMap<String, String> spinnerExpenseHeadMap = null;
    private Bundle _bundle;
    private AlertDialog dialog;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        getSupportActionBar().setTitle("Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        progressInfo = new ProgressInfo(ExpenseActivity.this);
        networkUtil = new NetworkUtil();
        session = new SessionManagement(getApplicationContext());

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        auto_txt_expense_head = (AutoCompleteTextView) findViewById(R.id.auto_txt_expense);
        auto_txt_expense_head.setOnItemClickListener((parent, view, position, id) -> {
            if (position != -1) {
                Selected_Expense = spinnerExpenseHeadMap.get((String) parent.getItemAtPosition(position));
                Log.d(TAG, "Item: " + parent.getItemAtPosition(position) + ", Value : " + Selected_Expense);
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

        edit_remark = findViewById(R.id.edit_remark);
        edit_rec_amount = findViewById(R.id.edit_rec_amount);
        edit_cheque_no = findViewById(R.id.edit_cheque_no);
        edit_issue_bank = findViewById(R.id.edit_issue_bank);
        edit_cheque_date = findViewById(R.id.edit_cheque_date);
        edit_cheque_date.setText(commonUtil.getCurrentedate(0));
        edit_cheque_date.setOnClickListener(arg0 -> setChequeDate());

        //Load After dialog load
        getPaymode();
        getExpenseHead();
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

        Calendar minDays = (Calendar) c.clone();
        minDays.add(Calendar.DATE, -100);

        dpd.getDatePicker().setMinDate(minDays.getTimeInMillis());
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    public void getPaymode() {

        if (networkUtil.getConnectivityStatus(ExpenseActivity.this).trim() == "false") {
            commonUtil.getToast(ExpenseActivity.this, "No internet connection!");
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
                        adapter = new ArrayAdapter<>(ExpenseActivity.this, R.layout.dropdown_menu_popup_item, spinnerpayModeArrary);
                        auto_txt_paymode.setThreshold(1);
                        auto_txt_paymode.setAdapter(adapter);
                    } else {
                        commonUtil.getToast(ExpenseActivity.this, "No Product Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<PayMode>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ExpenseActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getExpenseHead() {

        if (networkUtil.getConnectivityStatus(ExpenseActivity.this).trim() == "false") {
            commonUtil.getToast(ExpenseActivity.this, "No internet connection!");
            return;
        } else {
            Log.d(TAG, "USER_ID: " + user.get(SessionManagement.USER_ID) + ",COMPANY_ID: " + user.get(SessionManagement.COMPANY_ID));
            progressInfo.ProgressShow();
            apiInterface.getExpense("0", "0", user.get(SessionManagement.COMPANY_ID), "Master").enqueue(
                    new Callback<ArrayList<Expense>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Expense>> call, Response<ArrayList<Expense>> response) {
                            Log.d(TAG, "response: " + response.body());
                            dExpenseArrayList = response.body();
                            if (dExpenseArrayList.size() > 0) {
                                spinnerExpenseHeadMap = new HashMap<String, String>();
                                spinnerExpenseHeadArrary = new String[dExpenseArrayList.size()];
                                for (int i = 0; i < dExpenseArrayList.size(); i++) {
                                    spinnerExpenseHeadArrary[i] = dExpenseArrayList.get(i).getExpense_Name();
                                    spinnerExpenseHeadMap.put(dExpenseArrayList.get(i).getExpense_Name(), dExpenseArrayList.get(i).getExpense_ID());
                                }
                                adapter = null;
                                adapter = new ArrayAdapter<>(ExpenseActivity.this, R.layout.dropdown_menu_popup_item, spinnerExpenseHeadArrary);
                                auto_txt_expense_head.setThreshold(1);
                                auto_txt_expense_head.setAdapter(adapter);
                            } else {
                                commonUtil.getToast(ExpenseActivity.this, "No Expense Found!");
                            }
                            progressInfo.ProgressHide();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Expense>> call, Throwable t) {
                            progressInfo.ProgressHide();
                            Log.d(TAG, "Error: " + t.getMessage());
                            call.cancel();
                            commonUtil.getToast(ExpenseActivity.this, "Something Went Wrong!");
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
        if (Selected_Expense.equals("") || auto_txt_expense_head.getText().toString().trim().equals("")) {
            Toast.makeText(ExpenseActivity.this, "Select the Expense!", Toast.LENGTH_LONG).show();
        } else if (Selected_PayMode.equals("") || auto_txt_paymode.getText().toString().trim().equals("")) {
            Toast.makeText(ExpenseActivity.this, "Select the PayMode!", Toast.LENGTH_LONG).show();
        } else if (!auto_txt_paymode.getText().toString().trim().equals("") && (edit_rec_amount.getText().toString().trim().equals("0") || edit_rec_amount.getText().toString().trim().equals("") || edit_rec_amount.getText() == null)) {
            commonUtil.getToast(ExpenseActivity.this, "Enter Valid Rec Amount!");
        } else if (auto_txt_paymode.getText().toString().trim().equals("Cheque") && edit_cheque_no.getText().toString().length() < 6) {
            commonUtil.getToast(ExpenseActivity.this, "Enter Valid Cheque No!");
        } else if (auto_txt_paymode.getText().toString().trim().equals("Cheque") && (edit_issue_bank.getText().toString().trim().equals("0") || edit_issue_bank.getText().toString().trim().equals("") || edit_issue_bank.getText() == null)) {
            commonUtil.getToast(ExpenseActivity.this, "Enter Valid Cheque Issued Bank!");
        } else {
            saveConfirm();
        }
    }

    private void saveRecord() {

        Log.d(TAG, "saveRecord: Req_Date: " + commonUtil.getdateyyyymmdd(commonUtil.getCurrentedate(0)) +
                ", Selected_Expense: " + Selected_Expense + ", Rec Amount: " + edit_rec_amount.getText().toString() +
                ", Selected_PayMode: " + Selected_PayMode + ", Cheque No: " + edit_cheque_no.getText().toString() +
                ", Cheque Date: " + commonUtil.getdateyyyymmdd(edit_cheque_date.getText().toString()) +
                ", Issue Bank: " + edit_issue_bank.getText().toString() +", Remark: " + edit_remark.getText().toString() +
                ", USER ID: " + user.get(SessionManagement.USER_ID));

        progressInfo.ProgressShow();
        apiInterface.saveExpense(commonUtil.getdateyyyymmdd(commonUtil.getCurrentedate(0)), Selected_Expense,
                edit_rec_amount.getText().toString(), Selected_PayMode,edit_cheque_no.getText().toString(),
                commonUtil.getdateyyyymmdd(edit_cheque_date.getText().toString()),
                edit_issue_bank.getText().toString(),edit_remark.getText().toString(),
                user.get(SessionManagement.USER_ID),user.get(SessionManagement.COMPANY_ID)).enqueue(
                new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "message: " + response.message());
                        Log.d(TAG, "body: " + response.body());
                        if (response.body().equals("Success")) {
                            progressInfo.ProgressHide();
                            Toast.makeText(ExpenseActivity.this, "Record Saved Successfully", Toast.LENGTH_LONG).show();
                            intent = new Intent(ExpenseActivity.this, ExpenseReportActivity.class);
                            _bundle = ActivityOptions.makeCustomAnimation(ExpenseActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                            startActivity(intent, _bundle);
                        } else if (response.body().trim().equals("AlreadyExist")) {
                            Toast.makeText(ExpenseActivity.this, "Already Saved, Try After 5 Minuts", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ExpenseActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
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

    private void addExpenseHead(String ExpName,String Remark) {

        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(ExpenseActivity.this);
        alertConfirm.setTitle("Add New");
        alertConfirm.setMessage("Are You Sure You Want Add?");
        alertConfirm.setPositiveButton("YES", (d, which) -> {

            progressInfo.ProgressShow();
            apiInterface.saveExpenseHead(commonUtil.getdateyyyymmdd(commonUtil.getCurrentedate(0)),ExpName,Remark, user.get(SessionManagement.USER_ID)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "message: " + response.message());
                    Log.d(TAG, "body: " + response.body());
                    if (response.body().equals("Success")) {
                        commonUtil.getToast(getApplicationContext(), "Expense Head Added Successfully");
                        dialog.dismiss();
                        getExpenseHead();
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                    progressInfo.ProgressHide();
                    commonUtil.getToast(getApplicationContext(), "Record Added Failed!");
                }
            });

        });
        alertConfirm.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        alertConfirm.show();
    }

    public void addNewExpense(View vw) {

        View customLayout = getLayoutInflater().inflate(R.layout.dialog_expense_head, null);
        ((TextView) customLayout.findViewById(R.id.txt_head)).setText(Html.fromHtml("<small>New Expense Head</small>"));

        TextInputEditText edit_expense_head = customLayout.findViewById(R.id.edit_dialoge_expense_head);
        TextInputEditText edit_expense_remark = customLayout.findViewById(R.id.edit_dialoge_expense_remark);

        AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
        builder.setView(customLayout);
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", null);
        builder.setCancelable(false);
        builder.create();

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setOnShowListener(d -> {
            Button b = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> {
                if (edit_expense_head.getText().toString().trim().equals("0") || edit_expense_head.getText().toString().trim().equals("") || edit_expense_head.getText() == null) {
                    commonUtil.getToast(ExpenseActivity.this, "Enter Expense Head");
                }else if (edit_expense_remark.getText().toString().trim().equals("0") || edit_expense_remark.getText().toString().trim().equals("") || edit_expense_remark.getText() == null) {
                    commonUtil.getToast(ExpenseActivity.this, "Enter Remark");
                }
                else {
                    addExpenseHead(edit_expense_head.getText().toString(),edit_expense_remark.getText().toString());
                }
            });
            Button btnNegative = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
            btnNegative.setOnClickListener(view -> {
                dialog.dismiss();
            });
        });
        dialog.show();
    }

}
