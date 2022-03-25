package com.shree.maulifoods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

   /*     holder.txt_route_name.setText(dArrayList.get(position).getRoute_Desc());
        holder.txt_customer.setText(dArrayList.get(position).getCustomer_Name());
        holder.txt_customer_address.setText(dArrayList.get(position).getCustomer_Address());
        holder.txt_city.setText(dArrayList.get(position).getCustomer_City());

        holder.txt_que.setText(dArrayList.get(position).getSequence());
        holder.txt_prv_balance.setText(dArrayList.get(position).getPrv_Bal());
*/
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

}
