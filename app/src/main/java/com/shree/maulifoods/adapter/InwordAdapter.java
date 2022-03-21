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
    private CommonUtil commonUtil;
    private ProgressInfo progressInfo;
    private ApiInterface apiInterface;
    private Calendar c;
    private int year, month, day;
     SessionManagement session;
    private HashMap<String, String> user = null;
    //</editor-fold>

    public InwordAdapter(Context context, ArrayList<Inword> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        commonUtil = new CommonUtil();
        progressInfo = new ProgressInfo(context);
        apiInterface = RESTApi.getClient().create(ApiInterface.class);

        session = new SessionManagement(context);
        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

    }

    public void filterList(ArrayList<Inword> filterllist) {
        dArrayList = filterllist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_vendor, txt_req_date, txt_product, txt_uom, txt_total_qty, txt_save;
        public EditText ed_qty, ed_extra_qty, ed_bill_no, ed_bill_date;

        public MyViewHolder(View view) {
            super(view);

            txt_product = view.findViewById(R.id.lay_inword_listview_item_txt_product);
            ed_qty = view.findViewById(R.id.lay_inword_listview_item_ed_qty);
            //txt_uom = view.findViewById(R.id.lay_inword_listview_item_txt_uom);

            txt_req_date = view.findViewById(R.id.lay_inword_listview_item_txt_req_date);
            //ed_extra_qty = view.findViewById(R.id.lay_inword_listview_item_et_extra_qty);

            txt_total_qty = view.findViewById(R.id.lay_inword_listview_item_txt_total_qty);
            txt_vendor = view.findViewById(R.id.lay_inword_listview_item_txt_vendor);
            ed_bill_no = view.findViewById(R.id.lay_inword_listview_item_ed_bill_no);
            txt_save = view.findViewById(R.id.lay_inword_listview_item_txt_save);

            ed_bill_date = view.findViewById(R.id.lay_inword_listview_item_et_bill_date);
            ed_bill_date.setOnClickListener(arg0 -> setNxtFlwDate(ed_bill_date));
        }
    }

    private void setNxtFlwDate(EditText ed_bill_date) {
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (dayOfMonth < 10) {
                            ed_bill_date.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        if ((monthOfYear + 1) < 10) {
                            ed_bill_date.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        }
                        if ((monthOfYear + 1) < 10 && dayOfMonth < 10) {
                            ed_bill_date.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                        } else if (dayOfMonth >= 10 && (monthOfYear + 1) >= 10) {
                            ed_bill_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }
                }, year, month, day);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
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
        holder.txt_product.setText(dArrayList.get(position).getmProduct_Name());
        holder.txt_uom.setText(dArrayList.get(position).getUoM_Name());
        holder.txt_req_date.setText(dArrayList.get(position).getRequirment_Date());

        holder.ed_qty.setText(dArrayList.get(position).getOrder_Qty());
        holder.ed_extra_qty.setText(dArrayList.get(position).getExtra_Qty());
        holder.txt_total_qty.setText(dArrayList.get(position).getTotal_Qty());

        //Log.d(TAG, "Status: " + dArrayList.get(position).getAlready().trim());
        if(dArrayList.get(position).getAlready().trim().equals("Yes")) {
            holder.txt_save.setEnabled(false);
            holder.txt_save.setTextColor(ContextCompat.getColor(context, R.color.gray));
        }

        holder.ed_bill_date.setText(dArrayList.get(position).getBill_Date());
        holder.ed_bill_no.setText(dArrayList.get(position).getBill_No());

        holder.ed_qty.addTextChangedListener(new TextWatcher() {
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
                Double Qty = s.toString().trim().equals("") ? 0.0 : Double.valueOf(s.toString().trim());
                Double Extra_Qty = holder.ed_extra_qty.getText().toString().trim().equals("") ? 0.0 : Double.valueOf(holder.ed_extra_qty.getText().toString().trim());
                Double TotalQty = (Qty + Extra_Qty);
                holder.txt_total_qty.setText(String.valueOf(TotalQty));

                Inword updated = dArrayList.get(holder.getAdapterPosition());
                updated.setOrder_Qty(String.valueOf(Qty));
                updated.setExtra_Qty(String.valueOf(Extra_Qty));
                updated.setTotal_Qty(String.valueOf(TotalQty));
                dArrayList.set(holder.getAdapterPosition(), updated);

                holder.ed_qty.requestFocusFromTouch();
                holder.ed_qty.setSelection(holder.ed_qty.getText().length());
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
                Double Extra_Qty = s.toString().trim().equals("") ? 0.0 : Double.valueOf(s.toString().trim());
                Double Qty = holder.ed_qty.getText().toString().trim().equals("") ? 0.0 : Double.valueOf(holder.ed_qty.getText().toString().trim());
                Double TotalQty = (Qty + Extra_Qty);
                holder.txt_total_qty.setText(String.valueOf(TotalQty));

                Inword updated = dArrayList.get(holder.getAdapterPosition());
                updated.setOrder_Qty(String.valueOf(Qty));
                updated.setExtra_Qty(String.valueOf(Extra_Qty));
                updated.setTotal_Qty(String.valueOf(TotalQty));
                dArrayList.set(holder.getAdapterPosition(), updated);

                holder.ed_extra_qty.requestFocusFromTouch();
                holder.ed_extra_qty.setSelection(holder.ed_extra_qty.getText().length());
            }
        });

        holder.txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertConfirm(position, holder.ed_bill_date.getText().toString().trim(),holder.txt_save);
            }
        });

        holder.ed_bill_no.addTextChangedListener(new TextWatcher() {
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
                String BillNo = s.toString().trim().equals("") ? "0" : s.toString().trim();

                Inword updated = dArrayList.get(holder.getAdapterPosition());
                updated.setBill_No(BillNo);
                dArrayList.set(holder.getAdapterPosition(), updated);

                holder.ed_bill_no.requestFocusFromTouch();
                holder.ed_bill_no.setSelection(holder.ed_bill_no.getText().length());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

    public void alertConfirm(int position, String bill_dt,TextView btn_save) {
        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(context);
        alertConfirm.setTitle("Save Record");
        alertConfirm.setMessage("Are You Sure You Want Save Record?");
        alertConfirm.setPositiveButton("YES", (dialog, which) -> {
            saveRecord(position, bill_dt, btn_save);
        });
        alertConfirm.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        alertConfirm.show();
    }

    private void saveRecord(int position, String bill_dt,TextView btn_save) {

        Inword selected_row = dArrayList.get(position);

        Log.d(TAG, "Inword_ID " + selected_row.getInword_ID() + ", Product_ID " + selected_row.getmProduct_ID()
                + ", Vendor_Id " + selected_row.getVendor_ID() + ", Challan_Qty " + selected_row.getOrder_Qty()
                + ", Challan_Extra_Qty " + selected_row.getExtra_Qty() + ", Bill_No " + selected_row.getBill_No() + ", Bill_Date " + commonUtil.getdateyyyymmdd(bill_dt));

        if (selected_row.getOrder_Qty().equals("0") || selected_row.getOrder_Qty().equals("") || selected_row.getOrder_Qty().equals(null)) {
            Toast.makeText(context, "Challan Qty Never Zero", Toast.LENGTH_LONG).show();
        } else if (selected_row.getBill_No().equals("0") || selected_row.getBill_No().equals("") || selected_row.getBill_No().equals(null)) {
            Toast.makeText(context, "Enter Challan No", Toast.LENGTH_LONG).show();
        } else {

            progressInfo.ProgressShow();
            apiInterface.saveInword(selected_row.getInword_ID(), selected_row.getmProduct_ID(),
                    selected_row.getVendor_ID(), selected_row.getOrder_Qty(), selected_row.getExtra_Qty(),
                    selected_row.getBill_No(), commonUtil.getdateyyyymmdd(bill_dt),user.get(SessionManagement.USER_ID)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "message: " + response.message());
                    Log.d(TAG, "body: " + response.body());
                    if (response.body().trim().equals("Success")) {
                        Toast.makeText(context, "Record Saved Successfully", Toast.LENGTH_LONG).show();
                        btn_save.setEnabled(false);
                        btn_save.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    } else if (response.body().trim().equals("Error")) {
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                    progressInfo.ProgressHide();
                }
            });
        }

    }

}
