package com.shree.maulifoods.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.pojo.Subcribe;
import com.shree.maulifoods.ui.activity.CustomerActivity;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcriptionAdapter extends RecyclerView.Adapter<SubcriptionAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Subcribe> dArrayList;
    private Calendar c;
    private int year, month, day;
    private String TAG = "***SubcriptionAdapter***";
    private String[] productList, frequencyList, timeSlotList;
    private ArrayAdapter<String> adapter;
    public static ArrayList<Product> dRateArrayList = null;
    private HashMap<Integer, String> productListMap = null, dispFreqListMap, timeSlotListMap;
    private ApiInterface apiService = null;
    private NetworkUtil networkUtil;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private DecimalFormat df = new DecimalFormat("#.##");
    private boolean isOkayClicked = false;
    //</editor-fold>

    public SubcriptionAdapter(Context context, ArrayList<Subcribe> tempArrayList, String[] tempProductList, String[] tempFrequencyList, String[] tempTimeSlotList,
                              HashMap<Integer, String> tempProductListMap, HashMap<Integer, String> tempDispFreqListMap, HashMap<Integer, String> tempTimeSlotListMap) {
        this.context = context;
        this.dArrayList = tempArrayList;
        this.productList = tempProductList;
        this.frequencyList = tempFrequencyList;
        this.timeSlotList = tempTimeSlotList;
        this.productListMap = tempProductListMap;
        this.dispFreqListMap = tempDispFreqListMap;
        this.timeSlotListMap = tempTimeSlotListMap;

        apiService = RESTApi.getClient().create(ApiInterface.class);
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(context);
        commonUtil = new CommonUtil();

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public SubcriptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.subscribe_add_item_list, parent, false);
        MyViewHolder rvViewHolder = new MyViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(SubcriptionAdapter.MyViewHolder holder, int p) {
        int position = holder.getAdapterPosition();

        adapter = new ArrayAdapter<String>(context, R.layout.custom_spinner_items, productList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner_product_name.setAdapter(adapter);
        if (dArrayList.get(position).getProduct_Name() != null || dArrayList.get(position).getProduct_Name() != "") {
            int Sel_Position = adapter.getPosition(dArrayList.get(position).getProduct_Name());
            holder.spinner_product_name.setSelection(Sel_Position);
        }
        holder.spinner_product_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!parent.getItemAtPosition(pos).toString().equals("")) {
                    getProductList(productListMap.get(holder.spinner_product_name.getSelectedItemPosition()), holder.qty, holder.rate, holder.amount, position);
                    Subcribe updated = dArrayList.get(position);
                    updated.setProduct_ID(productListMap.get(holder.spinner_product_name.getSelectedItemPosition()));
                    updated.setProduct_Name(parent.getItemAtPosition(pos).toString());
                    dArrayList.set(position, updated);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        adapter = new ArrayAdapter<String>(context, R.layout.dropdown_small_item, frequencyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner_frequency.setAdapter(adapter);
        if (dArrayList.get(position).getFreq_Name() != null || dArrayList.get(position).getFreq_Name() != "") {
            int Sel_Position = adapter.getPosition(dArrayList.get(position).getFreq_Name());
            holder.spinner_frequency.setSelection(Sel_Position);
        }
        holder.spinner_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!parent.getItemAtPosition(pos).toString().equals("")) {
                    Subcribe updated = dArrayList.get(position);
                    updated.setFreq_ID(dispFreqListMap.get(holder.spinner_frequency.getSelectedItemPosition()));
                    updated.setFreq_Name(parent.getItemAtPosition(pos).toString());
                    dArrayList.set(position, updated);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        holder.qty.setText(dArrayList.get(position).getQty());
        holder.amount.setText(dArrayList.get(position).getAmount());
        if (dArrayList.get(position).getStart_Date() != null || dArrayList.get(position).getStart_Date() != "") {
            holder.startDate.setText(dArrayList.get(position).getStart_Date());
        }
        holder.startDate.setOnClickListener(view -> {
            setStartDate(holder.startDate, position);
        });

        if(timeSlotList!=null) {
            adapter = new ArrayAdapter<String>(context, R.layout.dropdown_small_item, timeSlotList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner_time_slot.setAdapter(adapter);
        }
        if (dArrayList.get(position).getTine_Slot_Name() != null || dArrayList.get(position).getTine_Slot_Name() != "") {
            int Sel_Position = adapter.getPosition(dArrayList.get(position).getTine_Slot_Name());
            holder.spinner_time_slot.setSelection(Sel_Position);
        }
        holder.spinner_time_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (!parent.getItemAtPosition(pos).toString().equals("")) {
                    Subcribe updated = dArrayList.get(position);
                    updated.setTine_Slot_ID(timeSlotListMap.get(holder.spinner_time_slot.getSelectedItemPosition()));
                    updated.setTine_Slot_Name(parent.getItemAtPosition(pos).toString());
                    dArrayList.set(position, updated);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        holder.removeImg.setOnClickListener(view -> {
            dArrayList.remove(position);
            notifyDataSetChanged();
        });

        holder.qty.addTextChangedListener(new TextWatcher() {
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
                updated.setRate(holder.rate.getText().toString());
                updated.setAmount(df.format(Qty * Double.valueOf(holder.rate.getText().toString())));
                dArrayList.set(position, updated);

                holder.amount.setText(df.format(Qty * Double.valueOf(holder.rate.getText().toString())));
                holder.qty.requestFocusFromTouch();
                holder.qty.setSelection(holder.qty.getText().length());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rate, amount;
        EditText qty, startDate;
        ImageView removeImg;
        LinearLayout llItem;
        Spinner spinner_product_name, spinner_frequency, spinner_time_slot;

        public MyViewHolder(View itemView) {
            super(itemView);

            spinner_product_name = itemView.findViewById(R.id.spinner_product_name);
            spinner_frequency = itemView.findViewById(R.id.spinner_frequency);
            removeImg = itemView.findViewById(R.id.img_remove);

            qty = itemView.findViewById(R.id.ed_product_qty);
            rate = itemView.findViewById(R.id.tv_product_rate);
            amount = itemView.findViewById(R.id.tv_product_amount);
            startDate = itemView.findViewById(R.id.ed_subc_start_dt);

            spinner_time_slot = itemView.findViewById(R.id.spinner_time_slot);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }

    public void setStartDate(EditText startDate, int position) {

        DatePickerDialog.OnDateSetListener datePickerListener = (view, yearOfSelect, monthOfYear, dayOfMonth) -> {
            if (isOkayClicked) {

                if (dayOfMonth < 10) {
                    startDate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + yearOfSelect);
                }
                if ((monthOfYear + 1) < 10) {
                    startDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + yearOfSelect);
                }
                if ((monthOfYear + 1) < 10 && dayOfMonth < 10) {
                    startDate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + yearOfSelect);
                } else if (dayOfMonth >= 10 && (monthOfYear + 1) >= 10) {
                    startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + yearOfSelect);
                }

                year = yearOfSelect;
                month = monthOfYear;
                day = dayOfMonth;

                Log.d(TAG, "SetDt: " + startDate.getText().toString());

                Subcribe updated = dArrayList.get(position);
                updated.setStart_Date(startDate.getText().toString());
                dArrayList.set(position, updated);
            }
            isOkayClicked = false;
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerListener, year, month, day);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialog.cancel();
                        isOkayClicked = false;
                    }
                });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        isOkayClicked = true;

                        DatePicker datePicker = datePickerDialog
                                .getDatePicker();

                        datePickerListener.onDateSet(datePicker,
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());

                    }
                });

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 15);
        datePickerDialog.getDatePicker().setMaxDate(endDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void getProductList(String product_id, EditText edt_qty, TextView tv_rate, TextView tv_amount, int position) {

        if (networkUtil.getConnectivityStatus(context).trim() == "false") {
            commonUtil.getToast(context, "No internet connection!");
            return;
        } else if (edt_qty.getText().toString().equals("") || edt_qty.getText().toString().equals("0")) {
            commonUtil.getToast(context, "Invalid Qty!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiService.getProductList(product_id).enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    dRateArrayList = response.body();
                    if (dRateArrayList.size() > 0) {
                        for (int i = 0; i < dRateArrayList.size(); i++) {
                            tv_rate.setText(dRateArrayList.get(i).getRate());

                            Subcribe updated = dArrayList.get(position);
                            updated.setQty(edt_qty.getText().toString());
                            updated.setRate(tv_rate.getText().toString());
                            updated.setAmount(df.format(Double.valueOf(edt_qty.getText().toString()) * Double.valueOf(tv_rate.getText().toString())));
                            dArrayList.set(position, updated);

                            tv_amount.setText(df.format(Double.valueOf(edt_qty.getText().toString()) * Double.valueOf(tv_rate.getText().toString())));
                        }
                    } else {
                        commonUtil.getToast(context, "No Rate Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(context, "Something Went Wrong!");
                }
            });
        }
    }

}