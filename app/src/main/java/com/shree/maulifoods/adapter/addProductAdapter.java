package com.shree.maulifoods.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.addProduct;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class addProductAdapter extends RecyclerView.Adapter<addProductAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<addProduct>  dArrayList;
    private String TAG = "***addProductAdapter***";
    //</editor-fold>

    public addProductAdapter(Context context, ArrayList<addProduct> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
    }

    @Override
    public addProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.item_list_add_product, parent, false);
        MyViewHolder rvViewHolder = new MyViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(addProductAdapter.MyViewHolder holder, int p) {
        int position = holder.getAdapterPosition();

        holder.tv_product.setText(dArrayList.get(position).getProduct_Name());
        holder.tv_challan_qty.setText(dArrayList.get(position).getChallanQty());
        holder.tv_qty.setText(dArrayList.get(position).getAcceptedQty());
        holder.tv_rate.setText(dArrayList.get(position).getRate());
        holder.tv_amount.setText(dArrayList.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return dArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product, tv_challan_qty, tv_qty, tv_rate, tv_amount;
        ImageView removeImg;
        LinearLayout llItem;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_product = itemView.findViewById(R.id.tv_product_desc);
            removeImg = itemView.findViewById(R.id.img_remove);

            tv_challan_qty = itemView.findViewById(R.id.tv_challan_qty);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_amount = itemView.findViewById(R.id.tv_amount);

            llItem = itemView.findViewById(R.id.ll_item);
        }
    }

}