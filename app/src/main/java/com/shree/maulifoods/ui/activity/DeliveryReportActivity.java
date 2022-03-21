package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.InwordAdapter;
import com.shree.maulifoods.pojo.Inword;
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

public class MaterialInwardReportActivity extends AppCompatActivity {

    //region Description
    private ProgressInfo progressInfo;
    private InwordAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***MaterialInwardReportActivity***", todaydate;
    private RecyclerView recyclerView;
    public static ArrayList<Inword> dArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private FloatingActionButton fab;
    private Bundle bundle;
    private Intent intent = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_inword_report);

        getSupportActionBar().setTitle("Material Inward");
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
        progressInfo = new ProgressInfo(MaterialInwardReportActivity.this);
        networkUtil = new NetworkUtil();

        //Log.d(TAG, "Login Status " + session.isLoggedIn());
        //session.checkLogin();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MaterialInwardReportActivity.this, MaterialInwardActivity.class);
                intent.putExtra("Save_Type", "0");
                bundle = ActivityOptions.makeCustomAnimation(MaterialInwardReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                startActivityForResult(intent, 1001, bundle);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        txt_no_record_found = (TextView) findViewById(R.id.txt_no_record_found);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                getInwordList(todaydate);
            }
        });

        todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
        Log.d(TAG, "todaydate " + todaydate);
        getInwordList(todaydate);
        recyclerView.setLayoutManager(new LinearLayoutManager(MaterialInwardReportActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    public void getInwordList(String forDt) {

        if (networkUtil.getConnectivityStatus(MaterialInwardReportActivity.this).trim() == "false") {
            commonUtil.getToast(MaterialInwardReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getInward("Summary", forDt).enqueue(new Callback<ArrayList<Inword>>() {
                @Override
                public void onResponse(Call<ArrayList<Inword>> call, Response<ArrayList<Inword>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dArrayList = response.body();
                    if (dArrayList.size()>0) {
                        dAdapter = new InwordAdapter(MaterialInwardReportActivity.this, dArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(MaterialInwardReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Inword>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(MaterialInwardReportActivity.this, "Something Went Wrong!");
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