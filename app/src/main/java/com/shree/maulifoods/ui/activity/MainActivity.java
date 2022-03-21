package com.shree.maulifoods.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.CustomerReportAdapter;
import com.shree.maulifoods.ui.fragments.AccountFragment;
import com.shree.maulifoods.ui.fragments.BottomSheetNavigationFragment;
import com.shree.maulifoods.ui.fragments.DashboardFragment;
import com.shree.maulifoods.ui.fragments.NotificationsFragment;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //<editor-fold desc="Description">
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private CommonUtil commonUtil;
    private String TAG = "***MainActivity***";
    private ProgressInfo progressInfo;
    private CustomerReportAdapter dAdapter;
    private NetworkUtil networkUtil;
    private ApiInterface apiInterface;
    SessionManagement session;
    private HashMap<String, String> user = null;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        session = new SessionManagement(getApplicationContext());
        progressInfo = new ProgressInfo(MainActivity.this);
        networkUtil = new NetworkUtil();

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shree Mauli Foods");
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationbar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new DashboardFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.action_dashboard:
                        toolbar.setTitle("Shree Mauli Foods");
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new DashboardFragment()).commit();
                        break;
                    case R.id.action_notification :
                        toolbar.setTitle("Notifications");
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new NotificationsFragment()).commit();
                        break;
                    case R.id.action_account:
                        toolbar.setTitle("Account");
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new AccountFragment()).commit();
                        break;
                    case R.id.action_settings:
                        toolbar.setTitle("Settings");
                        /*BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");*/
                        commonUtil.getSnackbar(coordinatorLayout,"Coming Soon...","OK");
                        break;
                }
                return true;
            }
        });

        //click event over FAB
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                bottomNavigationView.setSelectedItemId(R.id.mplaceholder);
                toolbar.setTitle("Shree Mauli Foods");
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });
    }

    @Override
    public void onBackPressed() {
        commonUtil.logout(MainActivity.this);
    }

}