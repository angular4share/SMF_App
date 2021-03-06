package com.shree.maulifoods.ui.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class ReceiptReportActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //region Description
    private ProgressInfo progressInfo;
    private ReceiptReportAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private RecyclerView recyclerView;
    public static ArrayList<Receipt> dreceiptArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private Bundle bundle;
    private Intent intent = null;
    private SearchView editsearch;
    private FloatingActionButton fab;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = "***ReceiptReportActivity***", todaydate = "";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_report);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

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
        getReceiptList(todaydate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Search Customer Name</small>"));
        editsearch.setOnQueryTextListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReceiptActivity();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }

    private void openReceiptActivity() {
        intent = new Intent(ReceiptReportActivity.this, ReceiptActivity.class);
        intent.putExtra("Save_Type", "S");
        bundle = ActivityOptions.makeCustomAnimation(ReceiptReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivityForResult(intent, 1001, bundle);
    }

    public void getReceiptList(String forDt) {

        if (networkUtil.getConnectivityStatus(ReceiptReportActivity.this).trim() == "false") {
            commonUtil.getToast(ReceiptReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getReceipt("Receipt", forDt + " 00:00:00", forDt + " 23:59:59", user.get(SessionManagement.USER_ID), "0", user.get(SessionManagement.COMPANY_ID)).enqueue(new Callback<ArrayList<Receipt>>() {
                @Override
                public void onResponse(Call<ArrayList<Receipt>> call, Response<ArrayList<Receipt>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dreceiptArrayList = response.body();
                    if (dreceiptArrayList.size() > 0) {
                        double Tot_Rec_Amt = 0.0;
                        getSupportActionBar().setTitle("Payment Receipt(" + dreceiptArrayList.size() + ")");
                        for (int i = 0; i < dreceiptArrayList.size(); i++) {
                            Tot_Rec_Amt += Double.valueOf(dreceiptArrayList.get(i).getReceive_Amount());
                        }
                        ((TextView) findViewById(R.id.txt_total_rec_amount)).setText(String.valueOf(Tot_Rec_Amt));
                        dAdapter = new ReceiptReportAdapter(ReceiptReportActivity.this, dreceiptArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        ((TextView) findViewById(R.id.txt_total_rec_amount)).setText("0/-");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            editsearch.setQuery("", false);
            todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
            getReceiptList(todaydate);
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}