package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.User;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppStartActivity extends AppCompatActivity {

    //region Description
    private TextView mVersionNumber;
    private RelativeLayout relativeLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String IMEINo = "", currentVersion, FCMTokenID = "0", TAG = "***AppStartActivity***";
    private Bundle bundle;
    private ApiInterface apiService = null;
    private Intent intent = null;
    private ArrayList<String> _mst = new ArrayList<>();
    //endregion

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_start);

        relativeLayout = findViewById(R.id.layout_app_start);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(AppStartActivity.this);
        apiService = RESTApi.getClient().create(ApiInterface.class);

        //getDeviceToken();
        IMEINo = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "IMEI_NO " + IMEINo);
        mVersionNumber = findViewById(R.id.mainVersionNumber);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mVersionNumber.setText("Version: " + currentVersion + " (" + IMEINo + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!networkUtil.getConnectivityStatus(AppStartActivity.this).trim().equals("false")) {
            checkDeviceRegister();
        } else {
            getSnackbar(relativeLayout, "No internet connection!", "RETRY");
        }
    }

    private void redictActivity(String type, String user_ID, String user_Name, String mobile_No, String eMail_ID) {
        intent = null;
        if (type.trim().equals("Login")) {
            intent = new Intent(AppStartActivity.this, LoginActivity.class);
        } else if (type.trim().equals("Pending")) {
            intent = new Intent(AppStartActivity.this, UserRegistrationActivity.class);
        }
        if (intent != null) {
            intent.putExtra("UID", user_ID);
            intent.putExtra("UName", user_Name);
            intent.putExtra("MobileNo", mobile_No);
            intent.putExtra("EmailID", eMail_ID);
            intent.putExtra("IMEINo", IMEINo.trim());
            intent.putExtra("FCMTokenID", FCMTokenID.trim());
            bundle = ActivityOptions.makeCustomAnimation(AppStartActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
            startActivity(intent, bundle);
        }
    }

    private void checkDeviceRegister() {

        if (networkUtil.getConnectivityStatus(AppStartActivity.this).trim().equals("false")) {
            getSnackbar(relativeLayout, "No internet connection!", "RETRY");
        } else {
            progressInfo.ProgressShow();
            apiService.deviceRegistered(IMEINo.trim()).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    List<User> user = response.body();
                    if (user.size() > 0) {
                        for (User list : user) {
                            if (list.active.trim().equals("No")) {
                                commonUtil.getSnackbar(relativeLayout, "Your Login ID is Disabled!", "OK");
                            } else if (list.active.trim().equals("Yes")) {
                                redictActivity("Login", list.user_ID, list.user_Name, list.mobile_No, list.eMail_ID);
                            }
                        }
                    } else {
                        redictActivity("Pending", "", "", "", "");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d(TAG, "Upload Error: " + t.getMessage());
                    call.cancel();
                    progressInfo.ProgressHide();
                    commonUtil.getSnackbar(relativeLayout, "Something Went Wrong!", "OK");
                    alertInternalError();
                }
            });
        }
    }

    public void closeActivity(View view) {
        alertMessage();
    }

    public void getSnackbar(RelativeLayout layout, String text_heading, String
            button_heading) {
        Snackbar snackbar = Snackbar
                .make(layout, text_heading, Snackbar.LENGTH_INDEFINITE)
                .setAction(button_heading, view -> checkDeviceRegister());

        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

       /*ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
        if (params instanceof CoordinatorLayout.LayoutParams) {
            ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
        } else {
            ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
        }
        snackbar.getView().setLayoutParams(params);*/

        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        alertMessage();
    }

    public void alertMessage() {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(AppStartActivity.this);
        alertMessage.setTitle("Exit");
        alertMessage.setMessage("Are You Sure You Want To Exit");

        alertMessage.setPositiveButton("Confirm", (dialog, which) -> {
            finish();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
        alertMessage.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        alertMessage.show();
    }

    public void alertInternalError() {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(AppStartActivity.this);
        alertMessage.setTitle("Exit");
        alertMessage.setMessage("Some maintenance activity is going on to our end, Please try after some times...");

        alertMessage.setPositiveButton("OK", (dialog, which) -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
        alertMessage.setCancelable(false);
        alertMessage.show();
    }

}