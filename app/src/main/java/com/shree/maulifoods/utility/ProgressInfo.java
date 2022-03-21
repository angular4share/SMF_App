package com.shree.maulifoods.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class ProgressInfo {

    private ProgressDialog progressDialog;

    public ProgressInfo(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //progressDialog.setContentView(R.drawable.progress_animation);
        //progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress_animation, null));
    }

    public void ProgressShow() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void ProgressHide() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
