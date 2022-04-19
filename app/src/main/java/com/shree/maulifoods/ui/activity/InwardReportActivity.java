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
import com.shree.maulifoods.adapter.InwardAdapter;
import com.shree.maulifoods.pojo.Inward;
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

public class InwardReportActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    //region Description
    private ProgressInfo progressInfo;
    private InwardAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***MaterialInwardReportActivity***", todaydate;
    private RecyclerView recyclerView;
    public static ArrayList<Inward> dinwardArrayList = null;
    private TextView txt_no_record_found;
    private ApiInterface apiInterface;
    private FloatingActionButton fab;
    private Bundle bundle;
    private SearchView editsearch;
    private Intent intent = null;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inword_report);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

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
        progressInfo = new ProgressInfo(InwardReportActivity.this);
        networkUtil = new NetworkUtil();

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Find Bill No</small>"));
        editsearch.setOnQueryTextListener(this);

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(InwardReportActivity.this, InwardActivity.class);
                intent.putExtra("Save_Type", "0");
                bundle = ActivityOptions.makeCustomAnimation(InwardReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
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
        getInwordList(todaydate);
        recyclerView.setLayoutManager(new LinearLayoutManager(InwardReportActivity.this, LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(animation);

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

    public void getInwordList(String forDt) {

        if (networkUtil.getConnectivityStatus(InwardReportActivity.this).trim() == "false") {
            commonUtil.getToast(InwardReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getInward("Summary", forDt).enqueue(new Callback<ArrayList<Inward>>() {
                @Override
                public void onResponse(Call<ArrayList<Inward>> call, Response<ArrayList<Inward>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dinwardArrayList = response.body();
                    if (dinwardArrayList.size()>0) {
                        double Tot_Acc_Qty=0.0,Tot_Bill_Qty=0.0;
                        for(int i = 0; i < dinwardArrayList.size(); i++){
                            Tot_Bill_Qty+= Double.valueOf(dinwardArrayList.get(i).getChallan_Qty());
                            Tot_Acc_Qty+= Double.valueOf(dinwardArrayList.get(i).getAccept_Qty());
                        }
                        ((TextView)findViewById(R.id.txt_total_challan_qty)).setText(String.valueOf(Tot_Bill_Qty));
                        ((TextView)findViewById(R.id.txt_total_accepted_qty)).setText(String.valueOf(Tot_Acc_Qty));

                        getSupportActionBar().setTitle("Inward("+dinwardArrayList.size()+")");
                        dAdapter = new InwardAdapter(InwardReportActivity.this, dinwardArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(InwardReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Inward>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(InwardReportActivity.this, "Something Went Wrong!");
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
            getInwordList(todaydate);
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