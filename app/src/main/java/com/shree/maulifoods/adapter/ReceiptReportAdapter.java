package com.shree.maulifoods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Receipt;

import java.util.ArrayList;

public class ReceiptReportAdapter extends RecyclerView.Adapter<ReceiptReportAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Receipt> dArrayList;
    private String TAG = "***ReceiptReportAdapter***";

    public ReceiptReportAdapter(Context context, ArrayList<Receipt> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
    }

    public void filterList(ArrayList<Receipt> filterllist) {
        dArrayList = filterllist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_receipt_no, txt_receipt_dt,txt_srno, txt_route, txt_customer_name, txt_customer_address, txt_mobile,
                txt_old_balance, txt_paymode, txt_receipt_amt, txt_cheque_no, txt_cheque_dt, txt_cheque_issue_bank;

        public MyViewHolder(View view) {
            super(view);

            txt_receipt_no = view.findViewById(R.id.lay_receipt_txt_receipt_no);
            txt_receipt_dt = view.findViewById(R.id.lay_receipt_txt_receipt_dt);
            txt_srno = view.findViewById(R.id.lay_receipt_txt_srno);

            txt_route = view.findViewById(R.id.lay_delivery_txt_route);
            txt_customer_name = view.findViewById(R.id.lay_receipt_txt_customer_name);
            txt_customer_address = view.findViewById(R.id.lay_receipt_txt_customer_address);
            txt_mobile = view.findViewById(R.id.lay_receipt_txt_mobile);
            txt_old_balance = view.findViewById(R.id.lay_receipt_txt_old_balance);
            txt_paymode = view.findViewById(R.id.lay_receipt_txt_paymode);
            txt_receipt_amt = view.findViewById(R.id.lay_receipt_txt_receipt_amt);
            txt_cheque_no = view.findViewById(R.id.lay_receipt_txt_cheque_no);
            txt_cheque_dt = view.findViewById(R.id.lay_receipt_txt_cheque_dt);
            txt_cheque_issue_bank = view.findViewById(R.id.lay_receipt_txt_cheque_issue_bank);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_receipt_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();

        holder.txt_receipt_no.setText(dArrayList.get(position).getReceipt_No());
        holder.txt_receipt_dt.setText(dArrayList.get(position).getReceipt_Dt());
        holder.txt_srno.setText(dArrayList.get(position).getSrNo());

        holder.txt_route.setText(dArrayList.get(position).getRoute_Desc());
        holder.txt_customer_name.setText(dArrayList.get(position).getCustomer_Name());
        holder.txt_customer_address.setText(dArrayList.get(position).getCustomer_Address());
        holder.txt_old_balance.setText(dArrayList.get(position).getCls_Balance());
        if(Double.valueOf(dArrayList.get(position).getCls_Balance())>0) {
            holder.txt_old_balance.setTextColor(ContextCompat.getColor(context, R.color.darkred));
        }else {
            holder.txt_old_balance.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        holder.txt_mobile.setText(dArrayList.get(position).getMobile_No());
        holder.txt_paymode.setText(dArrayList.get(position).getPayMode_Name());
        holder.txt_receipt_amt.setText(dArrayList.get(position).getReceive_Amount());

        holder.txt_cheque_no.setText(dArrayList.get(position).getCheque_No());
        holder.txt_cheque_dt.setText(dArrayList.get(position).getCheque_Date());
        holder.txt_cheque_issue_bank.setText(dArrayList.get(position).getCheque_IssueBank());
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

}
