package com.shree.maulifoods.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.DeliveryProductAdapter;
import com.shree.maulifoods.adapter.DeliveryReportAdapter;
import com.shree.maulifoods.pojo.Delivery;
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

public class DeliveryReportActivity extends AppCompatActivity {

    //region Description
    private ProgressInfo progressInfo;
    private DeliveryReportAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***DeliveryReportActivity***", todaydate = "", Selected_Type = "Pending";
    private RecyclerView recyclerView;
    public static ArrayList<Delivery> dArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private Bundle bundle;
    private Intent intent = null;
    private SearchView editsearch;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_report);

        getSupportActionBar().setTitle("Delivery Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -15);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 0);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        session = new SessionManagement(getApplicationContext());
        progressInfo = new ProgressInfo(DeliveryReportActivity.this);
        networkUtil = new NetworkUtil();

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        radioGroup = (RadioGroup) findViewById(R.id.radioTypeGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                Selected_Type = radioButton.getText().toString();
                filter("Type", Selected_Type);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        txt_no_record_found = (TextView) findViewById(R.id.txt_no_record_found);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                getDeliveryList(todaydate);
            }
        });

        todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addOnItemTouchListener(new CustomerReportActivity.RecyclerTouchListener(this, recyclerView, new CustomerReportActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        getDeliveryList(todaydate);

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Search Customer Name</small>"));
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter("Name", newText);
                return false;
            }
        });
    }

    public void getDeliveryList(String forDt) {

        if (networkUtil.getConnectivityStatus(DeliveryReportActivity.this).trim() == "false") {
            commonUtil.getToast(DeliveryReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getDelivery("Summary", forDt, user.get(SessionManagement.USER_ID), "0").enqueue(new Callback<ArrayList<Delivery>>() {
                @Override
                public void onResponse(Call<ArrayList<Delivery>> call, Response<ArrayList<Delivery>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dArrayList = response.body();
                    if (dArrayList.size() > 0) {
                        dAdapter = new DeliveryReportAdapter(DeliveryReportActivity.this, dArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(DeliveryReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Delivery>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(DeliveryReportActivity.this, "Something Went Wrong!");
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

    private void filter(String type, String text) {
        ArrayList<Delivery> filteredlist = new ArrayList<>();
        for (Delivery item : dArrayList) {
            if (text.trim().equals("Pending Delivery")) {
                if (item.getDelivery_Status().toLowerCase().contains("pending")) {
                    filteredlist.add(item);
                }
            } else if (text.trim().equals("Completed Delivery")) {
                if (item.getDelivery_Status().toLowerCase().contains("delivered")) {
                    filteredlist.add(item);
                }
            } else if (text.trim().equals("All")) {
                filteredlist.add(item);
            }
            if (type.trim().equals("Name")) {
                if (item.getCustomer_Name().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        if (filteredlist.isEmpty()) {
            txt_no_record_found.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            txt_no_record_found.setVisibility(View.GONE);
            dAdapter.filterList(filteredlist);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            editsearch.setQuery("", false);
            todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
            getDeliveryList(todaydate);
        }
    }

}