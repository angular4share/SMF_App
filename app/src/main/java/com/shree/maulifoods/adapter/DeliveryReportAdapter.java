package com.shree.maulifoods.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Delivery;
import com.shree.maulifoods.ui.activity.DeliveryActivity;

import java.util.ArrayList;

public class DeliveryReportAdapter extends RecyclerView.Adapter<DeliveryReportAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Delivery> dArrayList;
    private String TAG = "***DeliveryReportAdapter***";
    //</editor-fold>

    public DeliveryReportAdapter(Context context, ArrayList<Delivery> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
    }

    public void filterList(ArrayList<Delivery> filterllist) {
        dArrayList = filterllist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_route_name,txt_customer, txt_customer_address, txt_city, txt_que, txt_prv_balance,
                txt_delivery, txt_collection;

        public MyViewHolder(View view) {
            super(view);

            txt_route_name = view.findViewById(R.id.lay_delivery_listview_item_txt_route_name);
            txt_customer = view.findViewById(R.id.lay_delivery_listview_item_txt_customer);
            txt_customer_address = view.findViewById(R.id.lay_delivery_listview_item_txt_customer_address);
            txt_city = view.findViewById(R.id.lay_delivery_listview_item_txt_city);

            txt_que = view.findViewById(R.id.lay_delivery_listview_item_txt_que);
            txt_prv_balance = view.findViewById(R.id.lay_delivery_listview_item_txt_prv_balance);

            txt_collection = view.findViewById(R.id.lay_delivery_listview_item_txt_collection);
            txt_delivery = view.findViewById(R.id.lay_delivery_listview_item_txt_delivery);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_delivery_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();

        holder.txt_route_name.setText(dArrayList.get(position).getRoute_Desc());
        holder.txt_customer.setText(dArrayList.get(position).getCustomer_Name());
        holder.txt_customer_address.setText(dArrayList.get(position).getCustomer_Address());
        holder.txt_city.setText(dArrayList.get(position).getCustomer_City());

        holder.txt_que.setText(dArrayList.get(position).getSequence());
        holder.txt_prv_balance.setText(dArrayList.get(position).getPrv_Bal());

        if (dArrayList.get(position).getDelivery_Status().trim().equals("Delivered")) {
            holder.txt_delivery.setEnabled(false);
            holder.txt_delivery.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.txt_delivery.setText("Delivered");
        }

        holder.txt_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertConfirm(position, holder.ed_bill_date.getText().toString().trim(),holder.txt_save);
            }
        });

        holder.txt_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeliveryActivity.class);
                intent.putExtra("Subs_ID", dArrayList.get(position).getSubs_ID());
                intent.putExtra("Subs_Date",dArrayList.get(position).getRequirment_Date());
                intent.putExtra("Customer_Name", dArrayList.get(position).getCustomer_Name());
                intent.putExtra("Customer_Address", dArrayList.get(position).getCustomer_Address());
                intent.putExtra("Route_Desc", dArrayList.get(position).getRoute_Desc());
                intent.putExtra("Seq_No", dArrayList.get(position).getSequence());
                intent.putExtra("Prv_Balance", dArrayList.get(position).getPrv_Bal());
                Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.fadein, R.anim.fadeout).toBundle();
                context.startActivity(intent, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

}
