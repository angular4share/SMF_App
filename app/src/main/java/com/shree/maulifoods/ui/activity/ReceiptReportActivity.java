package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.ReceiptReportAdapter;
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
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptReportActivity extends AppCompatActivity {

    //region Description
    private ProgressInfo progressInfo;
    private ReceiptReportAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***ReceiptReportActivity***", todaydate = "";
    private RecyclerView recyclerView;
    public static ArrayList<Receipt> dArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private Bundle bundle;
    private Intent intent = null;
    private SearchView editsearch;
    private FloatingActionButton fab;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_report);

        getSupportActionBar().setTitle("Payment Receipt");
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
        progressInfo = new ProgressInfo(ReceiptReportActivity.this);
        networkUtil = new NetworkUtil();

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        recyclerView = findViewById(R.id.recycler_view);
        txt_no_record_found = (TextView) findViewById(R.id.txt_no_record_found);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                getReceiptList(todaydate);
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
        getReceiptList(todaydate);

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Search Customer Name</small>"));
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReceiptActivity("S");
            }
        });
    }

    private void openReceiptActivity(String type) {
        intent = new Intent(ReceiptReportActivity.this, ReceiptActivity.class);
        intent.putExtra("Save_Type", type.trim());
        if (type.trim().equals("S")) {
            intent.putExtra("Customer_ID", "0");
            intent.putExtra("Customer_Name", "");
            intent.putExtra("Customer_Type", "");
        } else if (type.trim().equals("U")) {
            /*intent.putExtra("Customer_ID", dcustomerArrayList.get(pos).getCustomer_ID());
            intent.putExtra("Customer_Name", dcustomerArrayList.get(pos).getCustomer_Name());
            intent.putExtra("Customer_Type", dcustomerArrayList.get(pos).getCustomer_Type());    */
        }
        bundle = ActivityOptions.makeCustomAnimation(ReceiptReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivityForResult(intent, 1001, bundle);
    }

    public void getReceiptList(String forDt) {

        if (networkUtil.getConnectivityStatus(ReceiptReportActivity.this).trim() == "false") {
            commonUtil.getToast(ReceiptReportActivity.this, "No internet connection!");
            return;
        } else {
           progressInfo.ProgressShow();
            apiInterface.getReceipt("Receipt", forDt+" 00:00:00", forDt+" 23:59:59", user.get(SessionManagement.USER_ID), "0", user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<ArrayList<Receipt>>() {
                @Override
                public void onResponse(Call<ArrayList<Receipt>> call, Response<ArrayList<Receipt>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dArrayList = response.body();
                    if (dArrayList.size() > 0) {
                        dAdapter = new ReceiptReportAdapter(ReceiptReportActivity.this, dArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(ReceiptReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Receipt>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ReceiptReportActivity.this, "Something Went Wrong!");
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

    private void filter( String text) {
       /* ArrayList<Delivery> filteredlist = new ArrayList<>();
        for (Delivery item : dArrayList) {
            if (item.getCustomer_Name().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            txt_no_record_found.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            txt_no_record_found.setVisibility(View.GONE);
            dAdapter.filterList(filteredlist);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            editsearch.setQuery("", false);
            todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
            getReceiptList(todaydate);
        }
    }

}