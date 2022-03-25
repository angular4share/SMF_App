package com.shree.maulifoods.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Delivery;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DeliveryProductAdapter extends RecyclerView.Adapter<DeliveryProductAdapter.MyViewHolder> {

    private ArrayList<Delivery> dArrayList;
    private String TAG = "***DeliveryProductAdapter***";
    private DecimalFormat df = new DecimalFormat("#.##");

    public DeliveryProductAdapter(ArrayList<Delivery> tempArrayList) {
        this.dArrayList = tempArrayList;
        df.setDecimalSeparatorAlwaysShown(false);
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

        holder.tv_product_name.setText(dArrayList.get(position).getProduct_Desc());
        holder.tv_stock_qty.setText(dArrayList.get(position).getStock_Qty());
        holder.tv_subs_qty.setText(dArrayList.get(position).getSubs_Qty());
        holder.ed_issue_qty.setText(dArrayList.get(position).getIssue_Qty());
        holder.ed_extra_qty.setText(dArrayList.get(position).getExtra_Qty());
        holder.tv_sale_rate.setText(dArrayList.get(position).getSale_Rate());
        holder.tv_sale_amount.setText(df.format(Double.valueOf(dArrayList.get(position).getSubs_Qty()) * Double.valueOf(dArrayList.get(position).getSale_Rate())));
        holder.tv_frequency.setText(dArrayList.get(position).getFreq_Name());
        holder.tv_time_slot.setText(dArrayList.get(position).getTime_Type() + " " + dArrayList.get(position).getTime_Slot());

        //Event
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

                Delivery updated = dArrayList.get(position);
                updated.setIssue_Qty(String.valueOf(Qty));
                updated.setSale_Rate(holder.tv_sale_rate.getText().toString());
                updated.setStock_Qty(holder.tv_stock_qty.getText().toString());
                dArrayList.set(position, updated);
                Log.d(TAG,"position: " + Qty);
                holder.tv_sale_amount.setText(df.format(Qty * Double.valueOf(holder.tv_sale_rate.getText().toString())));
                holder.ed_issue_qty.requestFocusFromTouch();
                holder.ed_issue_qty.setSelection(holder.ed_issue_qty.getText().length());
            }
        });

        holder.ed_extra_qty.addTextChangedListener(new TextWatcher() {
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

                Delivery updated = dArrayList.get(position);
                updated.setExtra_Qty(String.valueOf(Qty));
                updated.setSale_Rate(holder.tv_sale_rate.getText().toString());
                updated.setStock_Qty(holder.tv_stock_qty.getText().toString());
                dArrayList.set(position, updated);
                Log.d(TAG,"position: " + Qty);
                holder.ed_extra_qty.requestFocusFromTouch();
                holder.ed_extra_qty.setSelection(holder.ed_extra_qty.getText().length());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name, tv_stock_qty,tv_subs_qty, tv_sale_rate, tv_sale_amount, tv_frequency, tv_time_slot;
        EditText ed_issue_qty, ed_extra_qty;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_stock_qty = itemView.findViewById(R.id.tv_stock_qty);
            tv_subs_qty = itemView.findViewById(R.id.tv_subs_qty);
            ed_issue_qty = itemView.findViewById(R.id.ed_issue_qty);
            tv_sale_rate = itemView.findViewById(R.id.tv_sale_rate);
            tv_sale_amount = itemView.findViewById(R.id.tv_sale_amount);

            tv_frequency = itemView.findViewById(R.id.tv_frequency);
            tv_time_slot = itemView.findViewById(R.id.tv_time_slot);
            ed_extra_qty = itemView.findViewById(R.id.ed_extra_qty);
        }
    }

}