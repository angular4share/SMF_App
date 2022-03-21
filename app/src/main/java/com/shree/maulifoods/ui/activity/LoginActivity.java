package com.shree.maulifoods.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.User;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //region Description
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String TAG = "***LoginActivity***", CompName = "",CompAddress="", UID = "", UName = "",
            MobileNo="", EmailID="", IMEINo = "", FCMTokenID = "";
    SessionManagement session;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private PinEntryEditText pinEntry;
    private TextView txt_user_name, txt_user_email, txt_VersionNumber;
    private Bundle bundle;
    private RelativeLayout relativeLayout;
    private ApiInterface apiInterface = null;
    private Intent intent = null;
    //endregion

    @Override
    protected void onResume() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            commonUtil.buildAlertMessageNoGps(LoginActivity.this);
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        relativeLayout = (RelativeLayout) findViewById(R.id.layout_lock);
        progressInfo = new ProgressInfo(LoginActivity.this);
        session = new SessionManagement(LoginActivity.this);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        apiInterface = RESTApi.getClient().create(ApiInterface.class);

        UID = getIntent().getExtras().getString("UID");
        UName = getIntent().getExtras().getString("UName");
        MobileNo = getIntent().getExtras().getString("MobileNo");
        EmailID = getIntent().getExtras().getString("EmailID");
        IMEINo = getIntent().getExtras().getString("IMEINo");
        FCMTokenID = getIntent().getExtras().getString("FCMTokenID");

        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_user_name.setText("Welcome " + UName.trim());
        txt_user_email = (TextView) findViewById(R.id.txt_user_email);
        txt_user_email.setText(EmailID.trim());

        txt_VersionNumber = findViewById(R.id.lockVersionNumber);
        try {
            txt_VersionNumber.setText("Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName+" ("+IMEINo+")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //getNotificationCount(UID.trim());
        pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        pinEntry.requestFocus();
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(str -> chkLoginCredentials(str.toString().trim()));
        }
    }

    public void chkLoginCredentials(String Pwd) {
        if (networkUtil.getConnectivityStatus(LoginActivity.this).trim() == "false") {
            commonUtil.getToast(LoginActivity.this, "No internet connection!");
        } else {
            progressInfo.ProgressShow();
            apiInterface.loginCredintial(UID.trim(), Pwd.trim(), IMEINo.trim(), FCMTokenID.trim()).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    List<User> userInfo = response.body();
                    intent = null;
                    if (userInfo!=null) {
                        for (User list : userInfo) {
                            if (list.login_Type.trim().equals("A") || list.login_Type.trim().equals("U")) {
                                closeKeyBoard();
                                //commonUtil.getToastLongTime(LoginActivity.this, "Success");
                                session.createLoginSession(UID.trim(), UName.trim(), MobileNo.trim(), EmailID.trim(), list.login_Type.trim(),list.outlet_ID.trim());
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("IMEINo", getIntent().getExtras().getString("IMEINo"));
                                bundle = ActivityOptions.makeCustomAnimation(LoginActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                                startActivity(intent, bundle);
                            } else {
                                commonUtil.getToast(LoginActivity.this, "Something Went Wrong!");
                            }
                        }
                    } else {
                        commonUtil.getToast(LoginActivity.this, "Invalid mPIN!");
                        pinEntry.setError(true);
                        pinEntry.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pinEntry.setText(null);
                            }
                        }, 1000);
                    }
                    progressInfo.ProgressHide();
                }
                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                    progressInfo.ProgressHide();
                    commonUtil.getSnackbar(relativeLayout, "Something Went Wrong!", "OK");
                }
            });
        }
    }

    public void clickCloseActivity(View view) {
        alertMessage();
    }

    public void clickForgotMPIN(View view) {
        commonUtil.getSnackbar(relativeLayout, "Coming Soon...", "OK");
       /* intent = null;
        intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra("UID", UID.trim());
        intent.putExtra("UName", UName.trim());
        intent.putExtra("MobileNo", MobileNo.trim());
        intent.putExtra("EmailID", EmailID.trim());
        intent.putExtra("ShopName", CompName.trim());
        intent.putExtra("ShopAddress", CompAddress.trim());
        intent.putExtra("MPIN", "Forgot");
        intent.putExtra("IMEINo", IMEINo.trim());
        intent.putExtra("FCMTokenID", FCMTokenID.trim());
        bundle = ActivityOptions.makeCustomAnimation(LoginActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivity(intent, bundle);*/
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        alertMessage();
    }

    public void alertMessage() {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(LoginActivity.this);
        alertMessage.setTitle("Exit");
        alertMessage.setMessage("Are You Sure You Want To Exit");

        alertMessage.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        alertMessage.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertMessage.show();
    }

}