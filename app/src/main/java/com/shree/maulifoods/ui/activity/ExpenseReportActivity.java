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
import com.shree.maulifoods.adapter.ExpenseReportAdapter;
import com.shree.maulifoods.pojo.Expense;
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

public class ExpenseReportActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    //region Description
    private ProgressInfo progressInfo;
    private ExpenseReportAdapter dAdapter;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private String TAG = "***ExpenseReportActivity***", todaydate = "";
    private RecyclerView recyclerView;
    public static ArrayList<Expense> dexpenseArrayList = null;
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
        setContentView(R.layout.activity_expense_report);

        getSupportActionBar().setTitle("Expense");
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
        progressInfo = new ProgressInfo(ExpenseReportActivity.this);
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
                getExpenseList(todaydate);
            }
        });

        todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", Calendar.getInstance().getTime()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getExpenseList(todaydate);

        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setQueryHint(Html.fromHtml("<small>Search Expense Head</small>"));
        editsearch.setOnQueryTextListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExpenseActivity();
            }
        });
    }

    private void openExpenseActivity() {
        intent = new Intent(ExpenseReportActivity.this, ExpenseActivity.class);
        bundle = ActivityOptions.makeCustomAnimation(ExpenseReportActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivityForResult(intent, 1001, bundle);
    }

    public void getExpenseList(String forDt) {

        if (networkUtil.getConnectivityStatus(ExpenseReportActivity.this).trim() == "false") {
            commonUtil.getToast(ExpenseReportActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiInterface.getExpense( forDt, forDt , user.get(SessionManagement.COMPANY_ID),"Summary").enqueue(
                    new Callback<ArrayList<Expense>>() {
                @Override
                public void onResponse(Call<ArrayList<Expense>> call, Response<ArrayList<Expense>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dexpenseArrayList = response.body();
                    if (dexpenseArrayList.size() > 0) {

                        double Tot_Exps_Amt = 0.0;
                        getSupportActionBar().setTitle("Expense(" + dexpenseArrayList.size() + ")");
                        for (int i = 0; i < dexpenseArrayList.size(); i++) {
                            Tot_Exps_Amt += Double.valueOf(dexpenseArrayList.get(i).getExpense_Amount());
                        }
                        ((TextView) findViewById(R.id.txt_total_expense_amount)).setText(String.valueOf(Tot_Exps_Amt));

                        dAdapter = new ExpenseReportAdapter(ExpenseReportActivity.this, dexpenseArrayList);
                        txt_no_record_found.setVisibility(View.GONE);
                    } else {
                        dAdapter = null;
                        txt_no_record_found.setVisibility(View.VISIBLE);
                        commonUtil.getToast(ExpenseReportActivity.this, "No Record Found!");
                    }
                    recyclerView.setAdapter(dAdapter);
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
                    recyclerView.setLayoutAnimation(animation);
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Expense>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(ExpenseReportActivity.this, "Something Went Wrong!");
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
            getExpenseList(todaydate);
        }
    }

}