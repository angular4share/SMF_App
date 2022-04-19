package com.shree.maulifoods.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Requirment> dArrayList;
    private ArrayAdapter<String> adapter;
    private HashMap<Integer, String> spinnerMap;
    private String Vendor_Id = "0", TAG = "***RequisitionAdapter***";
    private CommonUtil commonUtil;
    private ProgressInfo progressInfo;
    private ApiInterface apiInterface;
    SessionManagement session;
    private HashMap<String, String> user = null;
    //</editor-fold>

    public RequisitionAdapter(Context context, ArrayList<Requirment> tempArrayList, String[] spinnerArray, HashMap<Integer, String> tempSpinnerMap) {
        this.context = context;
        this.dArrayList = tempArrayList;
        this.spinnerMap = tempSpinnerMap;

        adapter = new ArrayAdapter<String>(context, R.layout.custom_spinner_items, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        commonUtil = new CommonUtil();
        progressInfo = new ProgressInfo(context);
        apiInterface = RESTApi.getClient().create(ApiInterface.class);

        session = new SessionManagement(context);
        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_srno, txt_product, txt_qty, et_extra_qty, txt_uom, txt_total_qty, txt_raise_reqirment, txt_order_status;
        Spinner spinner_vendor;

        public MyViewHolder(View view) {
            super(view);

            txt_srno = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_srno);
            txt_product = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_product);
            txt_qty = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_qty);
            txt_uom = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_uom);
            et_extra_qty = view.findViewById(R.id.lay_purchase_requisition_listview_item_et_extra_qty);

            txt_total_qty = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_total_qty);
            spinner_vendor = view.findViewById(R.id.lay_purchase_requisition_listview_item_spinner_vendor);
            txt_raise_reqirment = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_raise_reqirment);
            txt_order_status = view.findViewById(R.id.lay_purchase_requisition_listview_item_txt_order_status);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requisition_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int p) {

        int position = holder.getAdapterPosition();

        holder.txt_srno.setText(dArrayList.get(position).getSr_No());
        holder.txt_product.setText(dArrayList.get(position).getmProduct_Name());
        holder.txt_qty.setText(dArrayList.get(position).getOrder_Qty());
        holder.txt_uom.setText(dArrayList.get(position).getUoM_Name());
        holder.et_extra_qty.setText(dArrayList.get(position).getExtra_Qty());

        holder.txt_total_qty.setText(dArrayList.get(position).getTotal_Qty());
        holder.txt_order_status.setText(dArrayList.get(position).getOrder_Status());
        holder.spinner_vendor.setAdapter(adapter);

        if (dArrayList.get(position).getOrder_Status().equals("Pending for Raise")) {
            holder.txt_order_status.setTextColor(ContextCompat.getColor(context, R.color.darkred));
        } else if (dArrayList.get(position).getOrder_Status().equals("Pending for Inward")) {
            holder.txt_raise_reqirment.setText("MODIFY");
            holder.txt_order_status.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            holder.spinner_vendor.setEnabled(false);
        } else if (dArrayList.get(position).getOrder_Status().equals("Order Completed")) {
            holder.txt_order_status.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.txt_raise_reqirment.setEnabled(false);
            holder.et_extra_qty.setEnabled(false);
            holder.spinner_vendor.setEnabled(false);
        }

        holder.spinner_vendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int selected_position, long id) {
                Vendor_Id = spinnerMap.get(selected_position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.txt_raise_reqirment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Vendor_Id.equals("0")) {
                    Toast.makeText(context, "Select Vendor Name", Toast.LENGTH_LONG).show();
                } else {
                    alertConfirm(position, holder.txt_raise_reqirment.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

    public void alertConfirm(int position, String caption) {
        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(context);
        alertConfirm.setTitle(caption + " Order");
        alertConfirm.setMessage("Are You Sure You Want " + caption + " Order?");
        alertConfirm.setPositiveButton("YES", (dialog, which) -> {
            saveRecord(position);
        });
        alertConfirm.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        alertConfirm.show();
    }

    private void saveRecord(int position) {

        Requirment selected_row = dArrayList.get(position);

        Log.d(TAG, "Vendor_Id " + Vendor_Id + ", Product_ID " + selected_row.getmProduct_ID() + ", Order_Qty " + selected_row.getOrder_Qty()
                + ", Extra_Qty " + selected_row.getExtra_Qty() + ", UoM_ID " + selected_row.getUoM_ID() + ", Inword_ID " + selected_row.getInword_ID());

        progressInfo.ProgressShow();
        apiInterface.saveRequirment(Vendor_Id, selected_row.getmProduct_ID(), selected_row.getOrder_Qty(), selected_row.getExtra_Qty(),
                selected_row.getUoM_ID(), selected_row.getInword_ID(), user.get(SessionManagement.USER_ID), user.get(SessionManagement.COMPANY_ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "message: " + response.message());
                Log.d(TAG, "body: " + response.body());
                if (response.body().trim().equals("Success")) {
                    Toast.makeText(context, "Requirment Saved Successfully", Toast.LENGTH_LONG).show();
                } else if (response.body().trim().equals("Updated")) {
                    Toast.makeText(context, "Requirment Modified Successfully", Toast.LENGTH_LONG).show();
                } else if (response.body().trim().equals("Error")) {
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
                } else if (response.body().trim().equals("Already")) {
                    Toast.makeText(context, "Requirment Already Raised", Toast.LENGTH_LONG).show();
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