package com.shree.maulifoods.ui.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.RequisitionAdapter;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.pojo.Vendor;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirmentActivity extends AppCompatActivity {

    //region Description
    private ProgressInfo progressInfo;
    private RequisitionAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***RequirmentActivity***", todaydate;
    private RecyclerView recyclerView;
    public static ArrayList<Requirment> dPurReqArrayList = null;
    public static ArrayList<Vendor> dVendorArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private static HashMap<Integer, String> vendorList;
    private String[] spinnerArray;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_requirment);

        getSupportActionBar().setTitle("Requirment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        session = new SessionManagement(getApplicationContext());
        progressInfo = new ProgressInfo(RequirmentActivity.this);
        networkUtil = new NetworkUtil();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -8);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 7);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();

        txt_no_record_found = (TextView) findViewById(R.id.txt_no_record_found);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(RequirmentActivity.this, LinearLayoutManager.VERTICAL, false));

        getVendorList();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                getPurchaseRequisitionList(todaydate);
            }
        });
    }

    public void getPurchaseRequisitionList(String forDt) {

        if (networkUtil.getConnectivityStatus(RequirmentActivity.this).trim() == "false") {
            commonUtil.getToast(RequirmentActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            Log.d(TAG, "forDt: " + forDt);
            apiInterface.getRequirment("Summary", forDt).enqueue(new Callback<ArrayList<Requirment>>() {
                @Override
                public void onResponse(Call<ArrayList<Requirment>> call, Response<ArrayList<Requirment>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dPurReqArrayList = response.body();
                    if (dPurReqArrayList.size()>0) {
                        double Tot_Req_Qry= 0.0;
                        getSupportActionBar().setTitle("Requirment(" + dPurReqArrayList.size() + ")");
                        for (int i = 0; i < dPurReqArrayList.size(); i++) {
                            Tot_Req_Qry += Double.valueOf(dPurReqArrayList.get(i).getTotal_Qty());
                        }
                        ((TextView) findViewById(R.id.txt_total_requirment_qty)).setText(String.valueOf(Tot_Req_Qry));
                        dAdapter = new RequisitionAdapter(RequirmentActivity.this, dPurReqArrayList, spinnerArray, vendorList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(RequirmentActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Requirment>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(RequirmentActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    public void getVendorList() {

        if (networkUtil.getConnectivityStatus(RequirmentActivity.this).trim() == "false") {
            commonUtil.getToast(RequirmentActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getVendor("0").enqueue(new Callback<ArrayList<Vendor>>() {
                @Override
                public void onResponse(Call<ArrayList<Vendor>> call, Response<ArrayList<Vendor>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dVendorArrayList = response.body();
                    vendorList = new HashMap<Integer, String>();
                    spinnerArray = new String[dVendorArrayList.size()];
                    if (dVendorArrayList.size() > 0) {
                        for (int i = 0; i < dVendorArrayList.size(); i++) {
                            vendorList.put(i, dVendorArrayList.get(i).getVendor_ID());
                            spinnerArray[i] = dVendorArrayList.get(i).getVendor_Name();
                        }
                    } else {
                        commonUtil.getToast(RequirmentActivity.this, "No Vendor Found!");
                    }
                    progressInfo.ProgressHide();

                    todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
                    Log.d(TAG, "todaydate " + todaydate);
                    getPurchaseRequisitionList(todaydate);
                }

                @Override
                public void onFailure(Call<ArrayList<Vendor>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(RequirmentActivity.this, "Something Went Wrong!");
                }
            });
        }
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