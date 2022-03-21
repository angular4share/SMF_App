package com.shree.maulifoods.ui.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.SubcriptionAdapter;
import com.shree.maulifoods.pojo.DispFrequency;
import com.shree.maulifoods.pojo.Product;
import com.shree.maulifoods.pojo.Requirment;
import com.shree.maulifoods.pojo.Route;
import com.shree.maulifoods.pojo.Subcribe;
import com.shree.maulifoods.pojo.TimeSlot;
import com.shree.maulifoods.pojo.Vendor;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    //region Description
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_CODE = 22;
    private LinearLayout linearLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String TAG = "***CustomerActivity***", Customer_ID = "", Customer_Type = "", Selected_Route = "", Subs_ID_D = "", Sr_No_D = "", ProdID_D = "",
            ProdDesc_D = "", Qty_D = "", FreqID_D = "", FreqName_D = "", Rate_D = "", Amount_D = "", StartDt_D = "", TimeSlotID_D = "", TimeSlotName_D = "";
    private Bundle bundle;
    private ApiInterface apiService = null;
    private Intent intent = null;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private ArrayList<Subcribe> add_Product_Items = new ArrayList<Subcribe>();
    private RecyclerView recyclerView;
    private SubcriptionAdapter rvAdapter;
    private EditText edit_text_fullname, edit_text_mobile_no1, edit_text_mobile_no2, edit_text_email_id, edit_text_address, edit_text_city, edit_text_pincode;
    public static ArrayList<Route> dRouteArrayList = null;
    public static ArrayList<Product> dProductArrayList = null;
    public static ArrayList<DispFrequency> dDispFreqArrayList = null;
    public static ArrayList<TimeSlot> dTimeSlotArrayList = null;
    private static HashMap<Integer, String> routeListMap, productListMap, dispFreqListMap, timeSlotListMap;
    private String[] spinnerRouteArray, spinnerProductArrary, spinnerDispFreqArray, spinnerTimeslotArrary;
    private AutoCompleteTextView editTextFilledExposedDropdown;
    private ArrayAdapter<String> adapter = null;
    //endregion

    @Override
    protected void onResume() {
        if (!checkPermission()) {
            requestPermission();
        }
        super.onResume();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer);

        if (getIntent().getExtras().getString("Save_Type").trim().equals("S")) {
            getSupportActionBar().setTitle("Customer Creation");
        } else {
            getSupportActionBar().setTitle("Customer Modification");
            ((TextView) findViewById(R.id.btn_save)).setText("Modify Customer");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        linearLayout = findViewById(R.id.layout_main);
        apiService = RESTApi.getClient().create(ApiInterface.class);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        session = new SessionManagement(CustomerActivity.this);
        progressInfo = new ProgressInfo(CustomerActivity.this);

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        editTextFilledExposedDropdown = findViewById(R.id.auto_txt_input_lay_route);
        recyclerView = findViewById(R.id.rv_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        edit_text_fullname = findViewById(R.id.edit_text_fullname);
        edit_text_mobile_no1 = findViewById(R.id.edit_text_mobile_no1);
        edit_text_mobile_no2 = findViewById(R.id.edit_text_mobile_no2);
        edit_text_email_id = findViewById(R.id.edit_text_email_id);
        edit_text_address = findViewById(R.id.edit_text_address);
        edit_text_city = findViewById(R.id.edit_text_city);
        edit_text_pincode = findViewById(R.id.edit_text_pincode);

        findViewById(R.id.add_address).setOnClickListener(CustomerActivity.this);
        findViewById(R.id.tv_add_items).setOnClickListener(CustomerActivity.this);
        findViewById(R.id.btn_save).setOnClickListener(CustomerActivity.this);

        getRouteList();
        getDispFrequencyList();
        getTimeSlotList();
        getProductList();

        Selected_Route = "";
        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    Selected_Route = routeListMap.get(position);
                }
            }
        });

        if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
            Customer_ID = getIntent().getExtras().getString("Customer_ID").trim();
            edit_text_fullname.setText(getIntent().getExtras().getString("Customer_Name").trim());
            Customer_Type = getIntent().getExtras().getString("Customer_Type").trim();
            edit_text_email_id.setText(getIntent().getExtras().getString("EMail_ID").trim());
            edit_text_mobile_no1.setText(getIntent().getExtras().getString("Mobile_No1").trim());
            edit_text_mobile_no2.setText(getIntent().getExtras().getString("Mobile_No2").trim());
            edit_text_address.setText(getIntent().getExtras().getString("Customer_Address").trim());
            edit_text_city.setText(getIntent().getExtras().getString("Customer_City").trim());
            edit_text_pincode.setText(getIntent().getExtras().getString("PinCode").trim());
            Selected_Route = getIntent().getExtras().getString("Route_ID").trim();
            Subs_ID_D = getIntent().getExtras().getString("Subs_ID").trim();
            ProdID_D = getIntent().getExtras().getString("Product_ID").trim();
            ProdDesc_D = getIntent().getExtras().getString("Product_Desc").trim();
            Qty_D = getIntent().getExtras().getString("Qty").trim();
            StartDt_D = getIntent().getExtras().getString("Start_Date").trim();
            FreqID_D = getIntent().getExtras().getString("Freq_ID").trim();
            FreqName_D = getIntent().getExtras().getString("Freq_Name").trim();
            TimeSlotID_D = getIntent().getExtras().getString("Time_Slot_ID").trim();
            TimeSlotName_D = getIntent().getExtras().getString("Time_Slot_Name").trim();
            //Sequence = getIntent().getExtras().getString("Sequence").trim();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address:
                bundle = ActivityOptions.makeCustomAnimation(CustomerActivity.this, R.anim.fadein, R.anim.fadeout).toBundle();
                intent = new Intent(CustomerActivity.this, AddressLocationActivity.class);
                startActivityForResult(intent, REQUEST_CODE, bundle);
                break;
            case R.id.tv_add_items:
                insertMethod("", "", "", "", "1", commonUtil.getCurrentedate(1), "", "");
                break;
            case R.id.btn_save:
                if (add_Product_Items.size() < 1) {
                    Toast.makeText(CustomerActivity.this, "Select Subscribe Products", Toast.LENGTH_LONG).show();
                } else if (edit_text_fullname.getText().toString().length() < 2) {
                    Toast.makeText(CustomerActivity.this, "Enter the Full Name", Toast.LENGTH_LONG).show();
                } else if (edit_text_mobile_no1.getText().toString().length() < 10) {
                    Toast.makeText(CustomerActivity.this, "Enter 10 digit MobileNo", Toast.LENGTH_LONG).show();
                } else if (edit_text_address.getText().toString().length() < 5) {
                    Toast.makeText(CustomerActivity.this, "Enter Valid Address", Toast.LENGTH_LONG).show();
                } else if (edit_text_city.getText().toString().length() < 1) {
                    Toast.makeText(CustomerActivity.this, "Enter Valid City", Toast.LENGTH_LONG).show();
                } else if (edit_text_pincode.getText().toString().length() < 6) {
                    Toast.makeText(CustomerActivity.this, "Enter 6 digit Pincode", Toast.LENGTH_LONG).show();
                } else if (editTextFilledExposedDropdown.getText().toString().length() < 1 || Selected_Route.equals("")) {
                    Toast.makeText(CustomerActivity.this, "Select the Route", Toast.LENGTH_LONG).show();
                } else {
                    saveConfirm();
                }
                break;
        }
    }

    public void saveConfirm() {
        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(CustomerActivity.this);
        alertConfirm.setTitle("Create/Modify Customer");
        alertConfirm.setMessage("Are You Sure You Want Create/Modify Customer?");
        alertConfirm.setPositiveButton("YES", (dialog, which) -> {
            saveRecord();
        });
        alertConfirm.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        alertConfirm.show();
    }

    private void insertMethod(String ProdID, String ProdName, String FreqID, String FreqName, String Qty, String StartDt, String TimeSlotID, String TimeSlotName) {
        Subcribe model = new Subcribe();
        model.setSrNo(String.valueOf(add_Product_Items.size() + 1));
        model.setProduct_ID(ProdID);
        model.setProduct_Name(ProdName);
        model.setFreq_ID(FreqID);
        model.setFreq_Name(FreqName);
        model.setQty(Qty);
        model.setRate("");
        model.setAmount("");
        model.setStart_Date(StartDt);
        model.setTine_Slot_ID(TimeSlotID);
        model.setTine_Slot_Name(TimeSlotName);
        add_Product_Items.add(model);

        rvAdapter.notifyDataSetChanged();
    }

    private boolean checkPermission() {
        int result_location1 = ContextCompat.checkSelfPermission(CustomerActivity.this, ACCESS_FINE_LOCATION);
        int result_location2 = ContextCompat.checkSelfPermission(CustomerActivity.this, ACCESS_COARSE_LOCATION);
        return result_location1 == PackageManager.PERMISSION_GRANTED && result_location2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean locationAccepted2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted1 && locationAccepted2) {
                        Toast.makeText(CustomerActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(CustomerActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access all the permissions", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 22) {
            edit_text_pincode.setText(data.getExtras().getString("PinCode"));
            edit_text_city.setText(data.getExtras().getString("City"));
            edit_text_address.setText(data.getExtras().getString("Address"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getRouteList() {

        if (networkUtil.getConnectivityStatus(CustomerActivity.this).trim() == "false") {
            commonUtil.getToast(CustomerActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiService.getRouteList(user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<ArrayList<Route>>() {
                @Override
                public void onResponse(Call<ArrayList<Route>> call, Response<ArrayList<Route>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dRouteArrayList = response.body();
                    routeListMap = new HashMap<Integer, String>();
                    spinnerRouteArray = new String[dRouteArrayList.size()];
                    if (dRouteArrayList.size() > 0) {
                        for (int i = 0; i < dRouteArrayList.size(); i++) {
                            routeListMap.put(i, dRouteArrayList.get(i).getRoute_ID());
                            spinnerRouteArray[i] = dRouteArrayList.get(i).getRoute_Name();
                        }
                        adapter = new ArrayAdapter<>(CustomerActivity.this, R.layout.dropdown_menu_popup_item, spinnerRouteArray);
                        editTextFilledExposedDropdown.setAdapter(adapter);
                        if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
                            editTextFilledExposedDropdown.setText(getIntent().getExtras().getString("Route_Name").trim(), false);
                        }
                    } else {
                        commonUtil.getToast(CustomerActivity.this, "No Route Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<Route>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(CustomerActivity.this, "Something Went Wrong!");
                }
            });
        }

    }

    public void getDispFrequencyList() {

        if (networkUtil.getConnectivityStatus(CustomerActivity.this).trim() == "false") {
            commonUtil.getToast(CustomerActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiService.getDispFrequencyList("0").enqueue(new Callback<ArrayList<DispFrequency>>() {
                @Override
                public void onResponse(Call<ArrayList<DispFrequency>> call, Response<ArrayList<DispFrequency>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dDispFreqArrayList = response.body();
                    dispFreqListMap = new HashMap<Integer, String>();
                    spinnerDispFreqArray = new String[dDispFreqArrayList.size()];
                    if (dDispFreqArrayList.size() > 0) {
                        for (int i = 0; i < dDispFreqArrayList.size(); i++) {
                            dispFreqListMap.put(i, dDispFreqArrayList.get(i).getFreq_ID());
                            spinnerDispFreqArray[i] = dDispFreqArrayList.get(i).getFreq_Name();
                        }
                    } else {
                        commonUtil.getToast(CustomerActivity.this, "No Dispatch Frequency Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<DispFrequency>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(CustomerActivity.this, "Something Went Wrong!");
                }
            });
        }

    }

    public void getTimeSlotList() {

        if (networkUtil.getConnectivityStatus(CustomerActivity.this).trim() == "false") {
            commonUtil.getToast(CustomerActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiService.getTimeSlotList("0").enqueue(new Callback<ArrayList<TimeSlot>>() {
                @Override
                public void onResponse(Call<ArrayList<TimeSlot>> call, Response<ArrayList<TimeSlot>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dTimeSlotArrayList = response.body();
                    timeSlotListMap = new HashMap<Integer, String>();
                    spinnerTimeslotArrary = new String[dTimeSlotArrayList.size()];
                    if (dTimeSlotArrayList.size() > 0) {
                        for (int i = 0; i < dTimeSlotArrayList.size(); i++) {
                            timeSlotListMap.put(i, dTimeSlotArrayList.get(i).getSlot_ID());
                            spinnerTimeslotArrary[i] = dTimeSlotArrayList.get(i).getSlot_Time();
                        }

                    } else {
                        commonUtil.getToast(CustomerActivity.this, "No TimeSlot Found!");
                    }
                    progressInfo.ProgressHide();
                }

                @Override
                public void onFailure(Call<ArrayList<TimeSlot>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(CustomerActivity.this, "Something Went Wrong!");
                }
            });
        }

    }

    public void getProductList() {

        if (networkUtil.getConnectivityStatus(CustomerActivity.this).trim() == "false") {
            commonUtil.getToast(CustomerActivity.this, "No internet connection!");
            return;
        } else {
            progressInfo.ProgressShow();
            apiService.getProductList("0").enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    Log.d(TAG, "response: " + response.body());
                    dProductArrayList = response.body();
                    productListMap = new HashMap<Integer, String>();
                    spinnerProductArrary = new String[dProductArrayList.size()];
                    if (dProductArrayList.size() > 0) {
                        for (int i = 0; i < dProductArrayList.size(); i++) {
                            productListMap.put(i, dProductArrayList.get(i).getProduct_ID());
                            spinnerProductArrary[i] = dProductArrayList.get(i).getProduct_Name();
                        }
                    } else {
                        commonUtil.getToast(CustomerActivity.this, "No Product Found!");
                    }
                    progressInfo.ProgressHide();
                    rvAdapter = new SubcriptionAdapter(CustomerActivity.this, add_Product_Items, spinnerProductArrary, spinnerDispFreqArray, spinnerTimeslotArrary, productListMap, dispFreqListMap, timeSlotListMap);
                    recyclerView.setAdapter(rvAdapter);

                    if (getIntent().getExtras().getString("Save_Type").trim().equals("U")) {
                        String[] ProdID_Items = ProdID_D.split("#");
                        String[] ProdDesc_Items = ProdDesc_D.split("#");
                        String[] FreqID_Items = FreqID_D.split("#");
                        String[] FreqDesc_Items = FreqName_D.split("#");
                        String[] Qty_Items = Qty_D.split("#");
                        String[] StartDt_Items = StartDt_D.split("#");
                        String[] TimeSlotID_Items = TimeSlotID_D.split("#");
                        String[] TimeSlotDesc_Items = TimeSlotName_D.split("#");
                        if (ProdID_Items.length > 0) {
                            for (int i = 0; i < ProdID_Items.length; i++) {
                                insertMethod(ProdID_Items[i].trim(), ProdDesc_Items[i].trim(), FreqID_Items[i].trim(), FreqDesc_Items[i].trim(),
                                        Qty_Items[i].trim(), StartDt_Items[i].trim(), TimeSlotID_Items[i].trim(), TimeSlotDesc_Items[i].trim());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    progressInfo.ProgressHide();
                    Log.d(TAG, "Error: " + t.getMessage());
                    call.cancel();
                    commonUtil.getToast(CustomerActivity.this, "Something Went Wrong!");
                }
            });
        }
    }

    private void saveRecord() {
        Sr_No_D = "";
        ProdID_D = "";
        ProdDesc_D = "";
        Qty_D = "";
        FreqID_D = "";
        FreqName_D = "";
        Rate_D = "";
        Amount_D = "";
        StartDt_D = "";
        TimeSlotID_D = "";
        TimeSlotName_D = "";
        if (add_Product_Items.size() > 0) {
            for (int i = 0; i < add_Product_Items.size(); i++) {
                if (i == 0) {
                    Sr_No_D = String.valueOf((i + 1));
                    ProdID_D = add_Product_Items.get(i).getProduct_ID();
                    ProdDesc_D = add_Product_Items.get(i).getProduct_Name();
                    Qty_D = add_Product_Items.get(i).getQty();
                    FreqID_D = add_Product_Items.get(i).getFreq_ID();
                    FreqName_D = add_Product_Items.get(i).getFreq_Name();
                    Rate_D = add_Product_Items.get(i).getRate();
                    Amount_D = add_Product_Items.get(i).getAmount();
                    StartDt_D = commonUtil.getdateyyyymmdd(add_Product_Items.get(i).getStart_Date());
                    TimeSlotID_D = add_Product_Items.get(i).getTine_Slot_ID();
                    TimeSlotName_D = add_Product_Items.get(i).getTine_Slot_Name();
                } else {
                    Sr_No_D = Sr_No_D + "#" + (i + 1);
                    ProdID_D = ProdID_D + "#" + add_Product_Items.get(i).getProduct_ID();
                    ProdDesc_D = ProdDesc_D + "#" + add_Product_Items.get(i).getProduct_Name();
                    Qty_D = Qty_D + "#" + add_Product_Items.get(i).getQty();
                    FreqID_D = FreqID_D + "#" + add_Product_Items.get(i).getFreq_ID();
                    FreqName_D = FreqName_D + "#" + add_Product_Items.get(i).getFreq_Name();
                    Rate_D = Rate_D + "#" + add_Product_Items.get(i).getRate();
                    Amount_D = Amount_D + "#" + add_Product_Items.get(i).getAmount();
                    StartDt_D = StartDt_D + "#" + commonUtil.getdateyyyymmdd(add_Product_Items.get(i).getStart_Date());
                    TimeSlotID_D = TimeSlotID_D + "#" + add_Product_Items.get(i).getTine_Slot_ID();
                    TimeSlotName_D = TimeSlotName_D + "#" + add_Product_Items.get(i).getTine_Slot_Name();
                }
            }
        }

        Log.d(TAG, "CustID: " + Customer_ID + ", CustName: " + edit_text_fullname.getText() + ", Address: " + edit_text_address.getText() + ", City: " + edit_text_city.getText()
                + ", Mobile1: " + edit_text_mobile_no1.getText() + ", Mobile2: " + edit_text_mobile_no2.getText().toString() + ", EMail: " + edit_text_email_id.getText().toString()
                + ", Pincode: " + edit_text_pincode.getText() + ", RouteID: " + Selected_Route + ", RouteName: " + editTextFilledExposedDropdown.getText() + ", Subs_ID_D: " + Subs_ID_D
                + ", ProdID_D: " + ProdID_D + ", ProdDesc_D: " + ProdDesc_D + ", Qty_D: " + Qty_D + ", FreqID_D: " + FreqID_D + ", FreqName_D: " + FreqName_D
                + ", Rate: " + Rate_D + ", Amount_D: " + Amount_D + ", StartDt_D: " + StartDt_D + ", TimeSlotID_D: " + TimeSlotID_D + ", TimeSlotName_D: " + TimeSlotName_D);

        progressInfo.ProgressShow();
        apiService.saveCustomer(Customer_ID, edit_text_fullname.getText().toString(), getIntent().getExtras().getString("Save_Type").trim(), "Subsribe",
                edit_text_address.getText().toString(), edit_text_city.getText().toString(), edit_text_pincode.getText().toString(), edit_text_mobile_no1.getText().toString(),
                edit_text_mobile_no2.getText().toString().equals("") ? "NA" : edit_text_mobile_no2.getText().toString(), edit_text_email_id.getText().toString().equals("") ? "NA" : edit_text_email_id.getText().toString(),
                Selected_Route, Subs_ID_D, Sr_No_D, ProdID_D, Qty_D, StartDt_D, FreqID_D,TimeSlotID_D,user.get(SessionManagement.OUTLET_ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "message: " + response.message());
                Log.d(TAG, "body: " + response.body());
                if (response.body().equals("Success")) {
                    if (getIntent().getExtras().getString("Save_Type").trim().equals("S")) {
                        Toast.makeText(CustomerActivity.this, "Customer Saved Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerActivity.this, "Customer Modified Successfully", Toast.LENGTH_LONG).show();
                    }
                } else if (response.body().trim().equals("Name_Exist")) {
                    Toast.makeText(CustomerActivity.this, "Customer Already Present", Toast.LENGTH_LONG).show();
                } else if (response.body().trim().equals("Mobile_Exist")) {
                    Toast.makeText(CustomerActivity.this, "Mobile No Already Present", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CustomerActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
                progressInfo.ProgressHide();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                progressInfo.ProgressHide();
            }
        });

    }

    @Override
    public void onBackPressed() {

        getIntent().putExtra("Type", "Ok");
        setResult(1001, getIntent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

}