package com.shree.maulifoods.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.shree.maulifoods.R;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtil {

    //SessionManagement session;

    public void getSnackbar(RelativeLayout layout, String text_heading, String button_heading) {
        Snackbar snackbar = Snackbar
                .make(layout, text_heading, Snackbar.LENGTH_LONG)
                .setAction(button_heading, view -> {
                });

        if (button_heading.trim().equals("OK")) {
            snackbar.setActionTextColor(Color.WHITE);
        } else {
            snackbar.setActionTextColor(Color.RED);
        }

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void getSnackbar(LinearLayout layout, String text_heading, String button_heading) {
        Snackbar snackbar = Snackbar
                .make(layout, text_heading, Snackbar.LENGTH_LONG)
                .setAction(button_heading, view -> {
                });

        if (button_heading.trim().equals("OK")) {
            snackbar.setActionTextColor(Color.WHITE);
        } else {
            snackbar.setActionTextColor(Color.RED);
        }

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void getSnackbar(CoordinatorLayout layout, String text_heading, String button_heading) {
        Snackbar snackbar = Snackbar
                .make(layout, text_heading, Snackbar.LENGTH_LONG)
                .setAction(button_heading, view -> {
                });

        if (button_heading.trim().equals("OK")) {
            snackbar.setActionTextColor(Color.WHITE);
        } else {
            snackbar.setActionTextColor(Color.RED);
        }

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void buildAlertMessageNoGps(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.d("***AppError---" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });
    }

    public void getToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void getToastLongTime(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        new CountDownTimer(30000, 1000)
        {
            public void onTick(long millisUntilFinished) {toast.show();}
            public void onFinish() {toast.cancel();}
        }.start();
    }

    public String getCurrentedate(int adddays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, adddays);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getCurrenteYear() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String yr = df.format(c.getTime());
        return yr;
    }

    public String getCurrenteMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("M");
        String mon = df.format(c.getTime());
        return mon;
    }

    public String getdateyyyymmdd(String dt) {
        if (dt == "")
            return "1900-01-01";
        else
            return dt.substring(6, 10) + "-" + dt.substring(3, 5) + "-" + dt.substring(0, 2);
    }

    public void logout(Context context) {
        //session = new SessionManagement(context);
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(context);
        alertMessage.setTitle("Logout");
        alertMessage.setMessage("Are You Sure You Want To Logout");

        alertMessage.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //session.logoutUser();
            }
        });
        alertMessage.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertMessage.show();
    }

    public void backActivity(final Context context) {

        AlertDialog.Builder alertMessage = new AlertDialog.Builder(context);
        alertMessage.setTitle("Previous Activity");
        alertMessage.setMessage("Are you sure you want to go back");

        alertMessage.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
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
