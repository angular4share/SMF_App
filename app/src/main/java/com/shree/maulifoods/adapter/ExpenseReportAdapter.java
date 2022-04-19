package com.shree.maulifoods.adapter;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Expense;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.ui.activity.ExpenseReportActivity;
import com.shree.maulifoods.ui.activity.ReceiptReportActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ExpenseReportAdapter extends RecyclerView.Adapter<ExpenseReportAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Expense> dArrayList;
    private ArrayList<Expense> arrayList;
    private String TAG = "***ExpenseReportAdapter***";

    public ExpenseReportAdapter(Context context, ArrayList<Expense> tempArrayList) {

        this.context = context;
        this.dArrayList = tempArrayList;
        this.arrayList = new ArrayList<Expense>();
        this.arrayList.addAll(ExpenseReportActivity.dexpenseArrayList);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_expense_dt, txt_entry_by, txt_srno, txt_expense_name,txt_expense_amount,txt_remark;

        public MyViewHolder(View view) {
            super(view);

            txt_expense_dt = view.findViewById(R.id.lay_expense_txt_dt);
            txt_entry_by = view.findViewById(R.id.lay_expense_txt_entry_by);
            txt_srno = view.findViewById(R.id.lay_expense_txt_srno);

            txt_expense_name = view.findViewById(R.id.lay_expense_txt_expense_name);
            txt_expense_amount = view.findViewById(R.id.lay_expense_txt_expense_amount);
            txt_remark = view.findViewById(R.id.lay_expense_txt_remark);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_list_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();

        holder.txt_expense_dt.setText(dArrayList.get(position).getExpense_Dt());
        holder.txt_entry_by.setText(dArrayList.get(position).getUser_Name());
        holder.txt_srno.setText(dArrayList.get(position).getSrNo());

        holder.txt_expense_name.setText(dArrayList.get(position).getExpense_Name());
        holder.txt_expense_amount.setText(dArrayList.get(position).getExpense_Amount());
        holder.txt_remark.setText(dArrayList.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ExpenseReportActivity.dexpenseArrayList.clear();
        if (charText.length() == 0) {
            ExpenseReportActivity.dexpenseArrayList.addAll(arrayList);
        } else {
            for (Expense objFilter : arrayList) {
                if (objFilter.getExpense_Name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ExpenseReportActivity.dexpenseArrayList.add(objFilter);
                }
            }
        }
        notifyDataSetChanged();
    }

}
