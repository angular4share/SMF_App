package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.DeliveryProductAdapter;
import com.shree.maulifoods.adapter.DeliveryReportAdapter;
import com.shree.maulifoods.pojo.Delivery;
import com.shree.maulifoods.pojo.Subcribe;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity {

    //region Description
    private RelativeLayout relativeLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String TAG = "***DeliveryActivity***", Sr_No_D = "", ProdID_D = "", Issue_Qty_D = "", Stock_Qty_D = "",
            Subs_Qty_D = "", Extra_Qty_D = "", Rate_D = "";
    private Bundle bundle;
    private ApiInterface apiInterface = null;
    private Intent intent = null;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private ArrayList<Delivery> dArrayList = new ArrayList<Delivery>();
    private RecyclerView recyclerView;
    private DeliveryProductAdapter rvAdapter;
    private TextView txt_seqno, txt_route, txt_customer, txt_customer_address;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery);

        getSupportActionBar().setTitle("Product Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        relativeLayout = findViewById(R.id.layout_main);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(DeliveryActivity.this);
        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        session = new SessionManagement(getApplicationContext());

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        txt_seqno = findViewById(R.id.lay_delivery_item_txt_seqno);
        txt_seqno.setText(getIntent().getExtras().getString("Seq_No").trim());
        txt_route = findViewById(R.id.lay_delivery_item_txt_route);
        txt_route.setText(getIntent().getExtras().getString("Route_Desc").trim());
        txt_customer = findViewById(R.id.lay_delivery_item_txt_customer);
        txt_customer.setText(getIntent().getExtras().getString("Customer_Name").trim());
        txt_customer_address = findViewById(R.id.lay_idelivery_listview_item_txt_customer_address);
        txt_customer_address.setText(getIntent().getExtras().getString("Customer_Address").trim());

        recyclerView = findViewById(R.id.rv_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (getIntent().getExtras().getString("Delivery_Status").trim().equals("Delivered")) {
            ((TextView) findViewById(R.id.lay_delivery_listview_item_txt_save)).setEnabled(false);
            ((TextView) findViewById(R.id.lay_delivery_listview_item_txt_save)).setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray));
            ((TextView) findViewById(R.id.lay_delivery_listview_item_txt_save)).setTextColor(ContextCompat.getColor(this, R.color.darkgray));
        }
        getDelivery(getIntent().getExtras().getString("Subs_Date").trim(), getIntent().getExtras().getString("Subs_ID").trim());
    }

    public void getDelivery(String subs_Date, String subs_ID) {

        if (networkUtil.getConnectivityStatus(DeliveryActivity.this).trim() == "false") {
            commonUtil.getToast(DeliveryActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getDelivery("Details", commonUtil.getdateyyyymmdd(subs_Date), user.get(SessionManagement.USER_ID),
                    subs_ID.trim(), user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<ArrayList<Delivery>>() {
                @Override
                public void onResponse(Call<ArrayList<Delivery>> call, Response<ArrayList<Delivery>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dArrayList = response.body();
                    if (dArrayList.size() > 0) {
                        rvAdapter = new DeliveryProductAdapter(dArrayList);
                    } else {
                        rvAdapter = null;
                        commonUtil.getToast(DeliveryActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(rvAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Delivery>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(DeliveryActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void saveValidation(View view) {

        int cnt = 0;
        Sr_No_D = "";
        ProdID_D = "";
        Issue_Qty_D = "";
        Stock_Qty_D = "";
        Subs_Qty_D = "";
        Extra_Qty_D = "";
        Rate_D = "";
        if (dArrayList.size() > 0) {
            for (int i = 0; i < dArrayList.size(); i++) {
                if (i == 0) {
                    Sr_No_D = String.valueOf((i + 1));
                    ProdID_D = dArrayList.get(i).getProduct_ID();
                    Issue_Qty_D = dArrayList.get(i).getIssue_Qty();
                    Extra_Qty_D = dArrayList.get(i).getExtra_Qty();
                    Subs_Qty_D = dArrayList.get(i).getSubs_Qty();
                    Stock_Qty_D = dArrayList.get(i).getStock_Qty();
                    Rate_D = dArrayList.get(i).getSale_Rate();
                    if (Double.valueOf(dArrayList.get(i).getSubs_Qty()) > 0) {
                        cnt = 1;
                    }
                } else {
                    Sr_No_D = Sr_No_D + "#" + (i + 1);
                    ProdID_D = ProdID_D + "#" + dArrayList.get(i).getProduct_ID();
                    Issue_Qty_D = Issue_Qty_D + "#" + dArrayList.get(i).getIssue_Qty();
                    Extra_Qty_D = Extra_Qty_D + "#" + dArrayList.get(i).getExtra_Qty();
                    Subs_Qty_D = Subs_Qty_D + "#" + dArrayList.get(i).getSubs_Qty();
                    Stock_Qty_D = Stock_Qty_D + "#" + dArrayList.get(i).getStock_Qty();
                    Rate_D = Rate_D + "#" + dArrayList.get(i).getSale_Rate();
                    if (Double.valueOf(dArrayList.get(i).getSubs_Qty()) > 0) {
                        cnt += 1;
                    }
                }
            }
        }

        if (dArrayList.size() < 0) {
            Toast.makeText(DeliveryActivity.this, "Record Not Found!", Toast.LENGTH_LONG).show();
        } else if (cnt == 0) {
            Toast.makeText(DeliveryActivity.this, "Enter the Issue Qty!", Toast.LENGTH_LONG).show();
        } else {
            saveConfirm();
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

    private void saveRecord() {

        Log.d(TAG, "Subs_ID: " + getIntent().getExtras().getString("Subs_ID").trim() + ", Subs_Date: " + commonUtil.getdateyyyymmdd(getIntent().getExtras().getString("Subs_Date").trim()) +
                ", Sr_No_D: " + Sr_No_D + ", ProdID_D: " + ProdID_D + ", Issue_Qty_D: " + Issue_Qty_D + ", Extra_Qty_D: " + Extra_Qty_D + ", Stock_Qty_D: " + Stock_Qty_D + ", Subs_Qty_D: " + Subs_Qty_D +
                ", Stock_Qty_D: " + Stock_Qty_D + ", Rate_D " + Rate_D + ", OUTLET_ID: " + user.get(SessionManagement.OUTLET_ID) + ", User_Id: " + user.get(SessionManagement.USER_ID));

        progressInfo.ProgressShow();
        apiInterface.saveDelivery(getIntent().getExtras().getString("Subs_ID").trim(),
                commonUtil.getdateyyyymmdd(getIntent().getExtras().getString("Subs_Date").trim()), "Subscribe",
                getIntent().getExtras().getString("Prv_Balance").trim(), Sr_No_D, ProdID_D, Subs_Qty_D, Issue_Qty_D,
                Stock_Qty_D, Extra_Qty_D, Rate_D, user.get(SessionManagement.USER_ID), user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "message: " + response.message());
                Log.d(TAG, "body: " + response.body());
                if (response.body().equals("Success")) {
                    progressInfo.ProgressHide();
                    Toast.makeText(DeliveryActivity.this, "Record Saved Successfully", Toast.LENGTH_LONG).show();
                    intent = new Intent(DeliveryActivity.this, DeliveryReportActivity.class);
                    Bundle _bundle = ActivityOptions.makeCustomAnimation(DeliveryActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                    startActivity(intent, _bundle);
                } else if (response.body().trim().equals("AlreadyExist")) {
                    Toast.makeText(DeliveryActivity.this, "Already Delivered", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DeliveryActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
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