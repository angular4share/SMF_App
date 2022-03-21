package com.shree.maulifoods.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Inword;
import com.shree.maulifoods.ui.activity.CustomerReportActivity;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InwordAdapter extends RecyclerView.Adapter<InwordAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Inword> dArrayList;
    private String TAG = "***InwordAdapter***";
    //</editor-fold>

    public InwordAdapter(Context context, ArrayList<Inword> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_vendor, txt_bill_no, txt_bill_date, txt_product, txt_challan_qty, txt_acc_qty, txt_shortfall, txt_uom, txt_rate, txt_amount,
                txt_prev_balance, txt_total_balance,txt_payment_mode,txt_payment_amt;

        public MyViewHolder(View view) {
            super(view);

            txt_vendor = view.findViewById(R.id.lay_inword_listview_item_txt_vendor);
            txt_bill_no = view.findViewById(R.id.lay_inword_listview_item_txt_bill_no);
            txt_bill_date = view.findViewById(R.id.lay_inword_listview_item_txt_bill_date);

            txt_product = view.findViewById(R.id.lay_inword_listview_item_txt_product);
            txt_challan_qty = view.findViewById(R.id.lay_inword_listview_item_txt_challan_qty);
            txt_acc_qty = view.findViewById(R.id.lay_inword_listview_item_txt_acc_qty);
            txt_shortfall = view.findViewById(R.id.lay_inword_listview_item_txt_shortfall);
            txt_uom = view.findViewById(R.id.lay_inword_listview_item_txt_uom);
            txt_rate = view.findViewById(R.id.lay_inword_listview_item_txt_rate);
            txt_amount = view.findViewById(R.id.lay_inword_listview_item_txt_amt);

            txt_prev_balance = view.findViewById(R.id.lay_inword_listview_item_txt_old_amount);
            txt_total_balance= view.findViewById(R.id.lay_inword_listview_item_txt_total_balance);
            txt_payment_mode = view.findViewById(R.id.lay_inword_listview_item_txt_payment_mode);
            txt_payment_amt = view.findViewById(R.id.lay_inword_listview_item_txt_payment_amt);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inword_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();

        holder.txt_vendor.setText(dArrayList.get(position).getVendor_Name());
        holder.txt_bill_date.setText(dArrayList.get(position).getBill_Date());
        holder.txt_bill_no.setText(dArrayList.get(position).getBill_No());

        holder.txt_product.setText(dArrayList.get(position).getProduct_Desc());
        holder.txt_challan_qty.setText(dArrayList.get(position).getChallan_Qty());
        holder.txt_acc_qty.setText(dArrayList.get(position).getAccept_Qty());
        holder.txt_uom.setText(dArrayList.get(position).getUoM_Name());
        holder.txt_shortfall.setText(String.valueOf(Double.valueOf(holder.txt_challan_qty.getText().toString())-Double.valueOf(holder.txt_acc_qty.getText().toString())));
        holder.txt_rate.setText(dArrayList.get(position).getRate());
        holder.txt_amount.setText(dArrayList.get(position).getAmount());
        holder.txt_prev_balance.setText(dArrayList.get(position).getOld_Amount());
        holder.txt_total_balance.setText(dArrayList.get(position).getTotal_Amount());
        holder.txt_payment_mode.setText(dArrayList.get(position).getPayment_Mode());
        holder.txt_payment_amt.setText(dArrayList.get(position).getPayment_Amt());
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

   }
