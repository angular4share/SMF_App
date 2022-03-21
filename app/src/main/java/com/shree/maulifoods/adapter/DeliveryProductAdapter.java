package com.shree.maulifoods.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Subcribe;
import com.shree.maulifoods.pojo.TimeSlot;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryProductAdapter extends RecyclerView.Adapter<DeliveryProductAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Subcribe> dArrayList;
    private String TAG = "***DeliveryProductAdapter***";
    private DecimalFormat df = new DecimalFormat("#.##");
    //</editor-fold>

    public DeliveryProductAdapter(Context context, ArrayList<Subcribe> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
    }

    @Override
    public DeliveryProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.item_list_delivery_add, parent, false);
        MyViewHolder rvViewHolder = new MyViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(DeliveryProductAdapter.MyViewHolder holder, int p) {
        int position = holder.getAdapterPosition();

        holder.tv_product_name.setText(dArrayList.get(position).getProduct_Name());
        holder.tv_subs_Qty.setText(dArrayList.get(position).getQty());
        holder.ed_issue_qty.setText(dArrayList.get(position).getQty());
        holder.tv_sale_rate.setText(dArrayList.get(position).getRate());
        holder.tv_sale_amount.setText(dArrayList.get(position).getAmount());

        holder.tv_frequency.setText(dArrayList.get(position).getFreq_Name());
        holder.tv_time_slot.setText(dArrayList.get(position).getTime_Type() + " " + dArrayList.get(position).getTine_Slot_Name());
        holder.ed_extra_qty.setText(dArrayList.get(position).getExtraQty());

        holder.ed_issue_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Integer Qty = s.toString().trim().equals("") ? 0 : Integer.valueOf(s.toString().trim());

                Subcribe updated = dArrayList.get(position);
                updated.setQty(String.valueOf(Qty));
                updated.setRate(holder.tv_sale_rate.getText().toString());
                updated.setAmount(df.format(Qty * Double.valueOf(holder.tv_sale_rate.getText().toString())));
                dArrayList.set(position, updated);

                holder.tv_sale_amount.setText(df.format(Qty * Double.valueOf(holder.tv_sale_rate.getText().toString())));
                holder.ed_issue_qty.requestFocusFromTouch();
                holder.ed_issue_qty.setSelection(holder.ed_issue_qty.getText().length());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name, tv_subs_Qty, tv_sale_rate, tv_sale_amount, tv_frequency, tv_time_slot;
        EditText ed_issue_qty, ed_extra_qty;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_subs_Qty = itemView.findViewById(R.id.tv_subs_Qty);
            ed_issue_qty = itemView.findViewById(R.id.ed_issue_qty);
            tv_sale_rate = itemView.findViewById(R.id.tv_sale_rate);
            tv_sale_amount = itemView.findViewById(R.id.tv_sale_amount);

            tv_frequency = itemView.findViewById(R.id.tv_frequency);
            tv_time_slot = itemView.findViewById(R.id.tv_time_slot);
            ed_extra_qty = itemView.findViewById(R.id.ed_extra_qty);
        }
    }

}