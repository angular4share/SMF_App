package com.shree.maulifoods.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.shree.maulifoods.ui.activity.AppStartActivity;
import com.shree.maulifoods.ui.activity.LoginActivity;

import java.util.HashMap;

public class SessionManagement {

    //region Description
    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "User_Info";
    private static final String IS_LOGIN = "IsLogin";
    public static final String USER_ID = "UserId";
    public static final String USER_NAME = "UserName";
    public static final String MOBILENO = "MobileNo";
    public static final String EMAILID = "EMaildID";
    public static final String LOGIN_TYPE = "LoginType";
    public static final String COMPANY_ID = "CompanyID";
    public static final String COMPANY_NAME = "CompanyName";
    public static final String COMPANY_ADDRESS = "CompanyAddress";
    public static final String USER_LAT = "Lattitude";
    public static final String USER_LANG = "Longitude";
    public static final String USER_CITY = "City";
    //endregion

    public SessionManagement(Context context) {
        super();
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String user_id, String username, String mobileno, String emailid, String login_type,
                                   String outlet_id,String outlet_name,String company_address) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.putString(USER_NAME, username);
        editor.putString(MOBILENO, mobileno);
        editor.putString(EMAILID, emailid);
        editor.putString(LOGIN_TYPE, login_type);
        editor.putString(COMPANY_ID, outlet_id);
        editor.putString(COMPANY_NAME, outlet_name);
        editor.putString(COMPANY_ADDRESS, company_address);
        editor.commit();
    }

    public void setLocationPref(String lat, String lang) {
        if (this.editor == null) {
            return;
        }
        this.editor.putString(USER_LAT, lat);
        this.editor.putString(USER_LANG, lang);
        editor.apply();
    }

    public String getLocationCity() {
        return pref.getString(USER_CITY, "");
    }

    public void setLocationCity(String city) {
        editor.putString(USER_CITY, city);
    }

    public String getLatPref() {
        return pref.getString(USER_LAT, "");
    }

    public String getLangPref() {
        return pref.getString(USER_LANG, "");
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(MOBILENO, pref.getString(MOBILENO, null));
        user.put(EMAILID, pref.getString(EMAILID, null));
        user.put(LOGIN_TYPE, pref.getString(LOGIN_TYPE, null));
        user.put(COMPANY_ID, pref.getString(COMPANY_ID, null));
        user.put(COMPANY_NAME, pref.getString(COMPANY_NAME, null));
        user.put(COMPANY_ADDRESS, pref.getString(COMPANY_ADDRESS, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Toast.makeText(context, "Logout Successfully", Toast.LENGTH_LONG).show();
        Intent i = new Intent(context, AppStartActivity.class);
        i.putExtra("log_out", "Y");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
