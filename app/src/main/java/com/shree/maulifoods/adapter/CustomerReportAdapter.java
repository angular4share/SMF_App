package com.shree.maulifoods.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Customer;
import com.shree.maulifoods.ui.activity.CustomerReportActivity;

import java.util.ArrayList;
import java.util.Locale;

public class CustomerReportAdapter extends RecyclerView.Adapter<CustomerReportAdapter.MyViewHolder> {

    private ArrayList<Customer> dArrayList;
    private ArrayList<Customer> arrayList;

    public CustomerReportAdapter(ArrayList<Customer> tempArrayList) {
        this.dArrayList = tempArrayList;
        this.arrayList = new ArrayList<Customer>();
        this.arrayList.addAll(CustomerReportActivity.dcustomerArrayList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_customer_name, txv_mobile_no, txv_customer_address, txv_srno,txv_pincode,txv_route;

        public MyViewHolder(View view) {
            super(view);

            txv_customer_name = view.findViewById(R.id.txv_customer_name);
            txv_mobile_no = view.findViewById(R.id.txv_mobile_no);
            txv_customer_address = view.findViewById(R.id.txv_customer_address);
            txv_srno = view.findViewById(R.id.txv_srno);
            txv_pincode = view.findViewById(R.id.txv_pincode);
            txv_route = view.findViewById(R.id.txv_route);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_customer_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txv_customer_name.setText(dArrayList.get(position).getCustomer_Name());
        holder.txv_mobile_no.setText(dArrayList.get(position).getMobile_No());
        holder.txv_customer_address.setText(dArrayList.get(position).getCustomer_Address());
        holder.txv_srno.setText(dArrayList.get(position).getSr_No());
        holder.txv_pincode.setText(dArrayList.get(position).getPinCode());
        holder.txv_route.setText(dArrayList.get(position).getRoute_Name());
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        CustomerReportActivity.dcustomerArrayList.clear();
        if (charText.length() == 0) {
            CustomerReportActivity.dcustomerArrayList.addAll(arrayList);
        } else {
            for (Customer objFilter : arrayList) {
                if (objFilter.getCustomer_Name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    CustomerReportActivity.dcustomerArrayList.add(objFilter);
                }
            }
        }
        notifyDataSetChanged();
    }

}