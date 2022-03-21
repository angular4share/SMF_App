package com.shree.maulifoods.ui.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.shree.maulifoods.R;
import com.shree.maulifoods.ui.activity.CustomerReportActivity;
import com.shree.maulifoods.ui.activity.DeliveryActivity;
import com.shree.maulifoods.ui.activity.MaterialInwardReportActivity;
import com.shree.maulifoods.ui.activity.PurchaseRequirmentActivity;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.HashMap;

public class DashboardFragment extends Fragment {

    //region Description
    private String TAG = "***DashboardFragment***";
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private CardView cv_sales, cv_collection, cv_requirment, cv_inword, cv_enquiry, cv_delivery, cv_expense, cv_stock;
    SessionManagement session;
    private TextView txtUserName, txtMobile, txtEmail;
    private HashMap<String, String> user = null;
    private Intent intent = null;
    private Bundle _bundle;
    private Button editProfileB;
    //endregion

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        commonUtil = new CommonUtil();
        session = new SessionManagement(getContext());
        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        txtUserName = root.findViewById(R.id.txtUserName);
        txtUserName.setText(user.get(SessionManagement.USER_NAME));

        txtMobile = root.findViewById(R.id.txtMobile);
        txtMobile.setText(user.get(SessionManagement.MOBILENO));

        txtEmail = root.findViewById(R.id.txtEmail);
        txtEmail.setText(user.get(SessionManagement.EMAILID));

        editProfileB = root.findViewById(R.id.editProfileB);
        cv_sales = root.findViewById(R.id.cv_sales);
        cv_collection = root.findViewById(R.id.cv_collection);
        cv_requirment = root.findViewById(R.id.cv_requirment);
        cv_inword = root.findViewById(R.id.cv_inword);
        cv_enquiry = root.findViewById(R.id.cv_enquiry);
        cv_delivery = root.findViewById(R.id.cv_delivery);
        cv_expense = root.findViewById(R.id.cv_expense);
        cv_stock = root.findViewById(R.id.cv_stock);

        editProfileB.setOnClickListener(view -> commonUtil.getToast(getContext(), "Coming Soon..."));

        cv_sales.setOnClickListener(view -> commonUtil.getToast(getContext(), "Coming Soon..."));
        cv_collection.setOnClickListener(view -> commonUtil.getToast(getContext(), "Coming Soon..."));

        cv_requirment.setOnClickListener(view -> {
            //commonUtil.getToast(getContext(), "Coming Soon...");
            intent = new Intent(getContext(), PurchaseRequirmentActivity.class);
            _bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fadein, R.anim.fadeout).toBundle();
            startActivity(intent, _bundle);
        });
        cv_inword.setOnClickListener(view -> {
            //commonUtil.getToast(getContext(), "Coming Soon...");

            intent = new Intent(getContext(), MaterialInwardReportActivity.class);
            _bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fadein, R.anim.fadeout).toBundle();
            startActivity(intent, _bundle);
        });

        cv_enquiry.setOnClickListener(view -> {
            intent = new Intent(getContext(), CustomerReportActivity.class);
            _bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fadein, R.anim.fadeout).toBundle();
            startActivity(intent, _bundle);
        });

        cv_delivery.setOnClickListener(view -> {
            //getFragmentManager().beginTransaction().replace(R.id.framecontainer, new DeliveryFragment()).commit();
            //commonUtil.getToast(getContext(), "Coming Soon...");
            intent = new Intent(getContext(), DeliveryActivity.class);
            _bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fadein, R.anim.fadeout).toBundle();
            startActivity(intent, _bundle);
        });

        cv_expense.setOnClickListener(view -> commonUtil.getToast(getContext(), "Coming Soon..."));
        cv_stock.setOnClickListener(view -> commonUtil.getToast(getContext(), "Coming Soon..."));

        return root;
    }

}