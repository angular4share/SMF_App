package com.shree.maulifoods.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.adapter.DeliveryProductAdapter;
import com.shree.maulifoods.pojo.Subcribe;
import com.shree.maulifoods.utility.ApiInterface;
import com.shree.maulifoods.utility.CommonUtil;
import com.shree.maulifoods.utility.NetworkUtil;
import com.shree.maulifoods.utility.ProgressInfo;
import com.shree.maulifoods.utility.RESTApi;
import com.shree.maulifoods.utility.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryActivity extends AppCompatActivity {

    //region Description
    private RelativeLayout relativeLayout;
    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private String TAG = "***DeliveryActivity***";
    private Bundle bundle;
    private ApiInterface apiService = null;
    private Intent intent = null;
    SessionManagement session;
    private HashMap<String, String> user = null;
    private ArrayList<Subcribe> add_Product_Items = new ArrayList<Subcribe>();
    private RecyclerView recyclerView;
    private DeliveryProductAdapter rvAdapter;
    private TextView txt_seqno, txt_route, txt_customer, txt_customer_address;
    private DecimalFormat df = new DecimalFormat("#.##");
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery);

        getSupportActionBar().setTitle("Product Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        relativeLayout = findViewById(R.id.layout_main);
        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(DeliveryActivity.this);
        apiService = RESTApi.getClient().create(ApiInterface.class);
        session = new SessionManagement(getApplicationContext());

        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();

        txt_seqno = findViewById(R.id.lay_delivery_item_txt_seqno);
        txt_seqno.setText(getIntent().getExtras().getString("Seq_No").trim());
        txt_route = findViewById(R.id.lay_delivery_item_txt_route);
        txt_route.setText(getIntent().getExtras().getString("Route_Desc").trim());
        txt_customer = findViewById(R.id.lay_delivery_item_txt_customer);
        txt_customer.setText(getIntent().getExtras().getString("Customer_Name").trim());
        txt_customer_address = findViewById(R.id.lay_idelivery_listview_item_txt_customer_address);
        txt_customer_address.setText(getIntent().getExtras().getString("Customer_Address").trim());

        recyclerView = findViewById(R.id.rv_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvAdapter = new DeliveryProductAdapter(DeliveryActivity.this, add_Product_Items);
        recyclerView.setAdapter(rvAdapter);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(animation);

        String[] ProdID_Items = getIntent().getExtras().getString("Product_ID").trim().split("#");
        String[] ProdDesc_Items = getIntent().getExtras().getString("Product_Desc").trim().split("#");
        String[] Subs_Qty_Items = getIntent().getExtras().getString("Subs_Qty").trim().split("#");
        String[] Sale_Rate_Items = getIntent().getExtras().getString("Sale_Rate").trim().split("#");
        String[] Freq_Name_Items = getIntent().getExtras().getString("Freq_Name").trim().split("#");
        String[] Time_Type_Items = getIntent().getExtras().getString("Time_Type").trim().split("#");
        String[] Time_Slot_Items = getIntent().getExtras().getString("Time_Slot").trim().split("#");
        //String[] Extra_Qty_Items = getIntent().getExtras().getString("Extra_Qty").trim().split("#");
        if (ProdID_Items.length > 0) {
            for (int i = 0; i < ProdID_Items.length; i++) {
                insertMethod(ProdID_Items[i].trim(), ProdDesc_Items[i].trim(), Freq_Name_Items[i].trim(),Subs_Qty_Items[i].trim(), Sale_Rate_Items[i].trim(),
                        Time_Type_Items[i].trim(), Time_Slot_Items[i].trim(),"");
            }
        }

    }

    private void insertMethod(String ProdID, String ProdDesc, String FreqName, String Qty, String SaleRate,
                              String TimeType, String TimeSlotName,String ExtraQty) {
        Subcribe model = new Subcribe();
        model.setSrNo(String.valueOf(add_Product_Items.size() + 1));
        model.setProduct_ID(ProdID);
        model.setProduct_Name(ProdDesc);
        model.setFreq_Name(FreqName);
        model.setQty(Qty);
        model.setRate(SaleRate);
        model.setAmount(df.format(Double.valueOf(Qty) * Double.valueOf(SaleRate)));
        model.setTime_Type(TimeType);
        model.setTine_Slot_Name(TimeSlotName);
        model.setExtraQty(ExtraQty);
        add_Product_Items.add(model);

        rvAdapter.notifyDataSetChanged();
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