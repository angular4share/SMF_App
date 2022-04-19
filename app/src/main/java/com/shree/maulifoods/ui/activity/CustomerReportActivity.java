package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.CustomerReportAdapter;
import com.shree.maulifoods.adapter.RequisitionAdapter;
import com.shree.maulifoods.pojo.Customer;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerReportActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //<editor-fold desc="Description">
    private FloatingActionButton fab;
    private LinearLayout linearLayout;
    private String TAG = "***CustomerReportActivity***";
    private SearchView editsearch;
    private ProgressInfo progressInfo;
    private CustomerReportAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private ApiInterface apiInterface;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private RecyclerView recyclerView;
    public static ArrayList<Customer> dcustomerArrayList = null;
    private TextView txt_no_record_found;
    private Bundle bundle;
    private Intent intent = null;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        session = new SessionManagement(getApplicationContext());
        progressInfo = new ProgressInfo(CustomerReportActivity.this);
        networkUtil = new NetworkUtil();

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        linearLayout = findViewById(R.id.linearLayout);
        txt_no_record_found = findViewById(R.id.txt_no_record_found);

        recyclerView = findViewById(R.id.recycler_view);

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Enter Customer</small>"));
        editsearch.setOnQueryTextListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomerActivity("S", 0);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openCustomerActivity("U", position);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        getCustomer(user.get(SessionManagement.COMPANY_ID), "0", "Summary");
    }

    private void openCustomerActivity(String type, int pos) {
        intent = new Intent(CustomerReportActivity.this, CustomerActivity.class);
        intent.putExtra("Save_Type", type.trim());
        if (type.trim().equals("S")) {
            intent.putExtra("Customer_ID", "0");
            intent.putExtra("Customer_Name", "");
            intent.putExtra("Customer_Type", "");
            intent.putExtra("EMail_ID", "");
            intent.putExtra("Mobile_No1", "");
            intent.putExtra("Mobile_No2", "");
            intent.putExtra("Customer_Address", "");
            intent.putExtra("Customer_City", "");
            intent.putExtra("PinCode", "");
            intent.putExtra("Route_ID", "");
            intent.putExtra("Route_Name", "");
            intent.putExtra("Route_Desc", "");
            intent.putExtra("Product_ID", "");
            intent.putExtra("Product_Desc", "");
            intent.putExtra("Product_Name", "");
            intent.putExtra("Qty", "");
            intent.putExtra("Start_Date", "");
            intent.putExtra("Freq_ID", "");
            intent.putExtra("Freq_Name", "");
            intent.putExtra("Freq_Days", "");
            intent.putExtra("Time_Type", "");
            intent.putExtra("Time_Slot_ID", "");
            intent.putExtra("Time_Slot_Name", "");
            intent.putExtra("Sequence", "");
        } else if (type.trim().equals("U")) {
            intent.putExtra("Customer_ID", dcustomerArrayList.get(pos).getCustomer_ID());
            intent.putExtra("Customer_Name", dcustomerArrayList.get(pos).getCustomer_Name());
            intent.putExtra("Customer_Type", dcustomerArrayList.get(pos).getCustomer_Type());
            intent.putExtra("EMail_ID", dcustomerArrayList.get(pos).geteMail_ID());
            intent.putExtra("Mobile_No1", dcustomerArrayList.get(pos).getMobile_No());
            intent.putExtra("Mobile_No2", dcustomerArrayList.get(pos).getMobile_No2());
            intent.putExtra("Customer_Address", dcustomerArrayList.get(pos).getCustomer_Address());
            intent.putExtra("Customer_City", dcustomerArrayList.get(pos).getCustomer_City());
            intent.putExtra("PinCode", dcustomerArrayList.get(pos).getPinCode());
            intent.putExtra("Route_ID", dcustomerArrayList.get(pos).getRoute_ID());
            intent.putExtra("Route_Name", dcustomerArrayList.get(pos).getRoute_Name());
            intent.putExtra("Route_Desc", dcustomerArrayList.get(pos).getRoute_Desc());
            intent.putExtra("Product_ID", dcustomerArrayList.get(pos).getProduct_ID());
            intent.putExtra("Product_Desc", dcustomerArrayList.get(pos).getProduct_Desc());
            intent.putExtra("Qty", dcustomerArrayList.get(pos).getQty());
            intent.putExtra("Start_Date", dcustomerArrayList.get(pos).getStart_Date());
            intent.putExtra("Freq_ID", dcustomerArrayList.get(pos).getFreq_ID());
            intent.putExtra("Freq_Name", dcustomerArrayList.get(pos).getFreq_Name());
            intent.putExtra("Freq_Days", dcustomerArrayList.get(pos).getFreq_Days());
            intent.putExtra("Time_Type", dcustomerArrayList.get(pos).getTime_Type());
            intent.putExtra("Time_Slot_ID", dcustomerArrayList.get(pos).getTime_Slot_ID());
            intent.putExtra("Time_Slot_Name", dcustomerArrayList.get(pos).getTime_Slot_Name());
            intent.putExtra("Sequence", dcustomerArrayList.get(pos).getSequence());
        }
        bundle = ActivityOptions.makeCustomAnimation(CustomerReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivityForResult(intent, 1001, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            editsearch.setQuery("", false);
            getCustomer(user.get(SessionManagement.COMPANY_ID), "0", "Summary");
        }
    }

    public void getCustomer(String outlet_id, String customer_id, String type) {
        if (networkUtil.getConnectivityStatus(CustomerReportActivity.this).trim() == "false") {
            commonUtil.getToast(CustomerReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            Log.d(TAG, "outlet_id: " + outlet_id + ", customer_id: " + customer_id + ", type: " + type);
            apiInterface.getCustomerList(outlet_id, customer_id, type).enqueue(new Callback<ArrayList<Customer>>() {
                @Override
                public void onResponse(Call<ArrayList<Customer>> call, Response<ArrayList<Customer>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dcustomerArrayList = response.body();
                    getSupportActionBar().setTitle("Customer Creation (" + dcustomerArrayList.size() +")");
                    if (dcustomerArrayList.size()>0) {
                        Log.d(TAG, "getTime_Type: " +dcustomerArrayList.get(0).getTime_Type());
                        dAdapter = new CustomerReportAdapter(dcustomerArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(CustomerReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(CustomerReportActivity.this, R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(CustomerReportActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        if (dAdapter == null) {
            return false;
        } else {
            dAdapter.filter(text);
        }
        return false;
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

}