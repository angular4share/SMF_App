package com.shree.maulifoods.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.shree.maulifoods.R;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.HashMap;

public class DeliveryActivity extends AppCompatActivity {

    //region Description
    private RelativeLayout relativeLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String TAG = "***DeliveryActivity***";
    private Bundle bundle;
    private ApiInterface apiService = null;
    private Intent intent = null;
    SessionManagement session;
    private HashMap<String, String> user = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery);

        relativeLayout = findViewById(R.id.layout_main);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(DeliveryActivity.this);
        apiService = RESTApi.getClient().create(ApiInterface.class);

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();
    }
}