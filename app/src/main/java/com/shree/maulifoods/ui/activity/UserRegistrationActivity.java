package com.shree.maulifoods.ui.activity;

//region Description
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shree.maulifoods.R;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import retrofit2.Call;
import retrofit2.Callback;
//endregion

public class UserRegistrationActivity extends AppCompatActivity {

    //region Description
    private TextInputEditText edit_text_fullname, edit_text_mobile_no, edit_text_email_id, edit_text_mpin, edit_text_confirm_mpin, edit_text_register_otp;
    private TextInputLayout txt_input_lay_fullname, txt_input_lay_mobile_no, txt_input_lay_email_id, txt_input_lay_mpin, txt_input_lay_confirm_mpin;
    private TextView txt_VersionNumber, txt_registration_member_id, txt_resend_otp, txt_registration_sms;
    private EditText edit_text_otp;
    private RelativeLayout relativeLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String url = "", TAG = "***UserRegistrationActivity***", IMEINo = "", UID = "", UName = "", MobileNo = "", EmailID = "", FCMTokenID = "";//MPIN = "";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Button btn_Register;
    private Bundle bundle;
    private ApiInterface apiInterface;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        requestQueue = Volley.newRequestQueue(UserRegistrationActivity.this);
        relativeLayout = (RelativeLayout) findViewById(R.id.linLay_Main);

        apiInterface = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(UserRegistrationActivity.this);

        UID = getIntent().getExtras().getString("UID");
        UName = getIntent().getExtras().getString("UName");
        MobileNo = getIntent().getExtras().getString("MobileNo");
        EmailID = getIntent().getExtras().getString("EmailID");
        IMEINo = getIntent().getExtras().getString("IMEINo");
        FCMTokenID = getIntent().getExtras().getString("FCMTokenID");

        Log.d(TAG, "UID: " + UID.trim() + ", UName: " + UName.trim()
                + ", MobileNo: " + MobileNo.trim() + ", EmailID: " + EmailID.trim() + " IMEINo: " + IMEINo.trim()
                + ", FCMTokenID: " + FCMTokenID.trim());

        txt_VersionNumber = findViewById(R.id.regiVersionNumber);
        try {
            txt_VersionNumber.setText("Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        edit_text_fullname = (TextInputEditText) findViewById(R.id.edit_text_fullname);
        edit_text_fullname.setText(UName);
        txt_input_lay_fullname = (TextInputLayout) findViewById(R.id.txt_input_lay_fullname);

        edit_text_mobile_no = (TextInputEditText) findViewById(R.id.edit_text_mobile_no);
        edit_text_mobile_no.setText(MobileNo);
        txt_input_lay_mobile_no = (TextInputLayout) findViewById(R.id.txt_input_lay_mobile_no);

        edit_text_email_id = (TextInputEditText) findViewById(R.id.edit_text_email_id);
        edit_text_email_id.setText(EmailID);
        txt_input_lay_email_id = (TextInputLayout) findViewById(R.id.txt_input_lay_email_id);

        edit_text_mpin = (TextInputEditText) findViewById(R.id.edit_text_mpin);
        txt_input_lay_mpin = (TextInputLayout) findViewById(R.id.txt_input_lay_mpin);

        edit_text_confirm_mpin = (TextInputEditText) findViewById(R.id.edit_text_confirm_mpin);
        txt_input_lay_confirm_mpin = (TextInputLayout) findViewById(R.id.txt_input_lay_confirm_mpin);
        edit_text_register_otp = (TextInputEditText) findViewById(R.id.edit_text_register_otp);

        txt_resend_otp = (TextView) findViewById(R.id.txt_resend_otp);

        edit_text_otp = (EditText) findViewById(R.id.edit_text_register_otp);
        btn_Register = (Button) findViewById(R.id.btn_Register);

        if (!UID.trim().isEmpty()) {
            edit_text_fullname.setEnabled(false);
            edit_text_mobile_no.setEnabled(false);
            edit_text_email_id.setEnabled(false);
            if (!getIntent().getExtras().getString("MPIN").trim().equals("Forgot")) {
                edit_text_mpin.setText(getIntent().getExtras().getString("MPIN").trim());
                edit_text_confirm_mpin.setText(getIntent().getExtras().getString("MPIN").trim());
            }

            findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
            txt_resend_otp.setTextColor(ContextCompat.getColor(UserRegistrationActivity.this, R.color.colorPrimary));
            btn_Register.setText("Validate");
            txt_resend_otp.setText("Resend OTP");
        }

        //region Description
        edit_text_mpin.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit_text_mpin.getText().toString().length() == 6)     //size as per your requirement
                {
                    edit_text_confirm_mpin.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        edit_text_confirm_mpin.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit_text_confirm_mpin.getText().toString().length() == 6)     //size as per your requirement
                {
                    if (!edit_text_mpin.getText().toString().trim().equals(edit_text_confirm_mpin.getText().toString().trim())) {
                        commonUtil.getSnackbar(relativeLayout, "MPin do not match", "OK");
                    } else {
                        btn_Register.requestFocus();
                    }
                    closeKeyBoard();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        edit_text_register_otp.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (edit_text_register_otp.getText().toString().length() == 4)     //size as per your requirement
                {
                    btn_Register.requestFocus();
                    closeKeyBoard();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        //endregion

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {

                if (btn_Register.getText().toString().trim().equals("Register")) {
                    clickRegistration();
                } else if (btn_Register.getText().toString().trim().equals("Validate")) {
                    clickValidate();
                }
            }
        });
    }

    public void clickCloseActivity(View view) {
        alertMessage();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateFullName() {
        if (edit_text_fullname.getText().toString().trim().isEmpty()) {
            txt_input_lay_fullname.setError("FullName is required");
            requestFocus(edit_text_fullname);
            return false;
        } else if (edit_text_fullname.getText().toString().length() < 5) {
            txt_input_lay_fullname.setError("Enter atleat 5 digit FullName");
            requestFocus(edit_text_fullname);
            return false;
        } else {
            txt_input_lay_fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobileNo() {
        if (edit_text_mobile_no.getText().toString().trim().isEmpty()) {
            txt_input_lay_mobile_no.setError("Mobile No is required");
            requestFocus(edit_text_mobile_no);
            return false;
        } else if (edit_text_mobile_no.getText().toString().length() < 10) {
            txt_input_lay_mobile_no.setError("Mobile No can't be less than 10 digit");
            requestFocus(edit_text_mobile_no);
            return false;
        } else if (!Patterns.PHONE.matcher(edit_text_mobile_no.getText().toString()).matches()) {
            txt_input_lay_mobile_no.setError("Enter valid 10 digit mobile no");
            requestFocus(edit_text_mobile_no);
            return false;
        } else {
            txt_input_lay_mobile_no.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEMailID() {
        if (edit_text_email_id.getText().toString().trim().isEmpty()) {
            txt_input_lay_email_id.setError("Enter valid email");
            requestFocus(edit_text_email_id);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_text_email_id.getText().toString()).matches()) {
            txt_input_lay_email_id.setError("Enter valid email, e.g. abc@example.com");
            requestFocus(edit_text_email_id);
            return false;
        } else {
            txt_input_lay_email_id.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMPin() {
        if (edit_text_mpin.getText().toString().trim().isEmpty()) {
            txt_input_lay_mpin.setError("MPin is required");
            requestFocus(edit_text_mpin);
            return false;
        } else if (edit_text_mpin.getText().toString().length() < 6) {
            txt_input_lay_mpin.setError("Enter 6 digit MPin");
            requestFocus(edit_text_mpin);
            return false;
        } else {
            txt_input_lay_mpin.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmMPin() {
        if (edit_text_confirm_mpin.getText().toString().trim().isEmpty()) {
            txt_input_lay_confirm_mpin.setError("MPin is required");
            requestFocus(edit_text_confirm_mpin);
            return false;
        } else if (edit_text_confirm_mpin.getText().toString().length() < 6) {
            txt_input_lay_confirm_mpin.setError("Enter 6 digit MPin");
            requestFocus(edit_text_confirm_mpin);
            return false;
        } else {
            txt_input_lay_confirm_mpin.setErrorEnabled(false);
            return true;
        }
    }

    public void clickResendOTP(View view) {
        if (!validateFullName()) {
            return;
        } else if (!validateMobileNo()) {
            return;
        } else if (!validateEMailID()) {
            return;
        } else if (!validateMPin()) {
            return;
        } else if (!validateConfirmMPin()) {
            return;
        } else if (!edit_text_mpin.getText().toString().trim().equals(edit_text_confirm_mpin.getText().toString().trim())) {
            commonUtil.getSnackbar(relativeLayout, "Confirm MPin not match with MPin", "OK");
            return;
        } else if (networkUtil.getConnectivityStatus(UserRegistrationActivity.this).trim() == "false") {
            commonUtil.getSnackbar(relativeLayout, "No internet connection!", "RETRY");
        } else {
            alertConfirmResend();
        }
    }

    public void clickRegistration() {

        if (!validateFullName()) {
            return;
        } else if (!validateMobileNo()) {
            return;
        } else if (!validateEMailID()) {
            return;
        } else if (!validateMPin()) {
            return;
        } else if (!validateConfirmMPin()) {
            return;
        } else if (!edit_text_mpin.getText().toString().trim().equals(edit_text_confirm_mpin.getText().toString().trim())) {
            commonUtil.getSnackbar(relativeLayout, "Confirm MPin not match with MPin", "OK");
            return;
        } else if (networkUtil.getConnectivityStatus(UserRegistrationActivity.this).trim() == "false") {
            commonUtil.getSnackbar(relativeLayout, "No internet connection!", "RETRY");
        } else {
            Log.d(TAG, "CustName: " + edit_text_fullname.getText().toString() + ", EMail: " + edit_text_email_id.getText().toString()
                    + ", MobileNo: " + edit_text_mobile_no.getText().toString() + ", IMEINo: " + IMEINo);

            progressInfo.ProgressShow();
            apiInterface.getOTP(edit_text_fullname.getText().toString(), edit_text_email_id.getText().toString(), edit_text_mobile_no.getText().toString(), IMEINo, FCMTokenID).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Log.d(TAG, "message: " + response.message());
                    Log.d(TAG, "body: " + response.body());
                    if (response.body().trim().equals("NotFound")) {
                        Toast.makeText(UserRegistrationActivity.this, "Mobile No Not Found", Toast.LENGTH_LONG).show();
                    } else if (response.body().equals("Success")) {
                        commonUtil.getToast(UserRegistrationActivity.this, "OTP Send");
                        findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
                        edit_text_fullname.setEnabled(false);
                        edit_text_mobile_no.setEnabled(false);
                        edit_text_email_id.setEnabled(false);
                        edit_text_mpin.setEnabled(false);
                        edit_text_confirm_mpin.setEnabled(false);
                        btn_Register.setText("Validate");
                        edit_text_otp.requestFocus();
                        new CountDownTimer(30000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                progressInfo.ProgressShow();
                                txt_resend_otp.setText("Resend in " + millisUntilFinished / 1000);

                                if (!txt_registration_sms.getText().toString().trim().isEmpty()) {
                                    edit_text_otp.setText(txt_registration_sms.getText().toString().trim().substring(0, 4));
                                }
                            }

                            public void onFinish() {
                                progressInfo.ProgressHide();
                                txt_resend_otp.setText("Resend OTP");
                                txt_resend_otp.setTextColor(ContextCompat.getColor(UserRegistrationActivity.this, R.color.colorPrimary));

                                if (!txt_registration_sms.getText().toString().trim().isEmpty()) {
                                    if (edit_text_otp.getText().toString().trim().equals(txt_registration_sms.getText().toString().trim().substring(0, 4))) {
                                        clickValidate();
                                    }
                                }
                            }
                        }.start();
                    } else {
                        Toast.makeText(UserRegistrationActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                    progressInfo.ProgressHide();
                }
            });
        }
    }

    public void callLoginActivity() {
        Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
        intent.putExtra("UName", edit_text_fullname.getText().toString().trim());
        intent.putExtra("MobileNo", edit_text_mobile_no.getText().toString().trim());
        intent.putExtra("EmailID", edit_text_email_id.getText().toString().trim());
        intent.putExtra("MPIN", "");
        intent.putExtra("IMEINo", IMEINo.trim());
        intent.putExtra("FCMTokenID", FCMTokenID.trim());
        bundle = ActivityOptions.makeCustomAnimation(UserRegistrationActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
        startActivity(intent, bundle);
    }

    public void clickValidate() {

        if (networkUtil.getConnectivityStatus(UserRegistrationActivity.this).trim() == "false") {
            commonUtil.getSnackbar(relativeLayout, "No internet connection!", "RETRY");
        } else if (edit_text_register_otp.getText().toString().trim().length() != 4) {
            commonUtil.getSnackbar(relativeLayout, "Enter 4 digit Verification Code!", "OK");
        } else {
            Log.d(TAG, "OTPNo: " + edit_text_register_otp.getText().toString() + ", FCMTokenID: " + FCMTokenID
                    + ", MobileNo: " + edit_text_mobile_no.getText().toString() + ", IMEINo: " + IMEINo);
            progressInfo.ProgressShow();
            apiInterface.setOTP(edit_text_register_otp.getText().toString(), FCMTokenID, edit_text_mobile_no.getText().toString(), IMEINo).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Log.d(TAG, "message: " + response.message());
                    Log.d(TAG, "body: " + response.body());
                    if (response.body().trim().equals("Expired")) {
                        commonUtil.getSnackbar(relativeLayout, "OTP Expired!", "OK");
                    } else if (response.body().equals("Invalid")) {
                        commonUtil.getSnackbar(relativeLayout, "Invalid OTP!", "OK");
                    } else if (response.body().equals("Success")) {
                        commonUtil.getToast(UserRegistrationActivity.this, "Success");
                        progressInfo.ProgressHide();
                        callLoginActivity();
                    } else {
                        Toast.makeText(UserRegistrationActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                    progressInfo.ProgressHide();
                    onBackPressed();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                    progressInfo.ProgressHide();
                }
            });
        }
    }

    public void alertConfirmResend() {
        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(UserRegistrationActivity.this);
        alertConfirm.setTitle("Confirmation");
        alertConfirm.setMessage("Are You Sure you want to Resend");

        alertConfirm.setPositiveButton("YES", (dialog, which) -> {

            if (btn_Register.getText().toString().trim().equals("Validate") && txt_resend_otp.getText().toString().trim().equals("Resend OTP")) {
                closeKeyBoard();

                Log.d(TAG, "CustName: " + edit_text_fullname.getText().toString() + ", EMail: " + edit_text_email_id.getText().toString()
                        + ", MobileNo: " + edit_text_mobile_no.getText().toString() + ", IMEINo: " + IMEINo);

                progressInfo.ProgressShow();
                apiInterface.getOTP(edit_text_fullname.getText().toString(), edit_text_email_id.getText().toString(), edit_text_mobile_no.getText().toString(), IMEINo, FCMTokenID).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        Log.d(TAG, "message: " + response.message());
                        Log.d(TAG, "body: " + response.body());
                        if (response.body().trim().equals("NotFound")) {
                            Toast.makeText(UserRegistrationActivity.this, "Mobile No Not Found", Toast.LENGTH_LONG).show();
                        } else if (response.body().equals("Success")) {
                            commonUtil.getToast(UserRegistrationActivity.this, "OTP Send");
                            findViewById(R.id.layout_otp).setVisibility(View.VISIBLE);
                            edit_text_fullname.setEnabled(false);
                            edit_text_mobile_no.setEnabled(false);
                            edit_text_email_id.setEnabled(false);
                            edit_text_mpin.setEnabled(false);
                            edit_text_confirm_mpin.setEnabled(false);
                            btn_Register.setText("Validate");
                            edit_text_otp.requestFocus();
                            new CountDownTimer(30000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    progressInfo.ProgressShow();
                                    txt_resend_otp.setText("Resend in " + millisUntilFinished / 1000);

                                    if (!txt_registration_sms.getText().toString().trim().isEmpty()) {
                                        edit_text_otp.setText(txt_registration_sms.getText().toString().trim().substring(0, 4));
                                    }
                                }

                                public void onFinish() {
                                    progressInfo.ProgressHide();
                                    txt_resend_otp.setText("Resend OTP");
                                    txt_resend_otp.setTextColor(ContextCompat.getColor(UserRegistrationActivity.this, R.color.colorPrimary));

                                    if (!txt_registration_sms.getText().toString().trim().isEmpty()) {
                                        if (edit_text_otp.getText().toString().trim().equals(txt_registration_sms.getText().toString().trim().substring(0, 4))) {
                                            clickValidate();
                                        }
                                    }
                                }
                            }.start();
                        } else {
                            Toast.makeText(UserRegistrationActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                        progressInfo.ProgressHide();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "Error: " + t.getMessage());
                        progressInfo.ProgressHide();
                    }
                });

            }

        });

        alertConfirm.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertConfirm.show();
    }

    @Override
    public void onBackPressed() {
        alertMessage();
    }

    public void alertMessage() {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(UserRegistrationActivity.this);
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

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
