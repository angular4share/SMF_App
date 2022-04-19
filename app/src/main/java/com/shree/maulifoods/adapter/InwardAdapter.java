package com.shree.maulifoods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Inward;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.ui.activity.InwardReportActivity;

import java.util.ArrayList;
import java.util.Locale;

public class InwardAdapter extends RecyclerView.Adapter<InwardAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Inward> dArrayList;
    private ArrayList<Inward> arrayList;
    private String TAG = "***InwordAdapter***";
    //</editor-fold>

    public InwardAdapter(Context context, ArrayList<Inward> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
        this.arrayList = new ArrayList<Inward>();
        this.arrayList.addAll(InwardReportActivity.dinwardArrayList);
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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        InwardReportActivity.dinwardArrayList.clear();
        if (charText.length() == 0) {
            InwardReportActivity.dinwardArrayList.addAll(arrayList);
        } else {
            for (Inward objFilter : arrayList) {
                if (objFilter.getBill_No().toLowerCase(Locale.getDefault()).contains(charText)) {
                    InwardReportActivity.dinwardArrayList.add(objFilter);
                }
            }
        }
        notifyDataSetChanged();
    }

   }
