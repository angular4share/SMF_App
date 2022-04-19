package com.shree.maulifoods.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.shree.maulifoods.R;
import com.shree.maulifoods.pojo.Customer;
import com.shree.maulifoods.pojo.Receipt;
import com.shree.maulifoods.ui.activity.CustomerReportActivity;
import com.shree.maulifoods.ui.activity.ReceiptReportActivity;
import com.shree.maulifoods.utility.SessionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class ReceiptReportAdapter extends RecyclerView.Adapter<ReceiptReportAdapter.MyViewHolder> {

    //<editor-fold desc="Description">
    private Context context;
    private ArrayList<Receipt> dArrayList;
    private ArrayList<Receipt> arrayList;
    private String TAG = "***ReceiptReportAdapter***";
    int pageHeight = 520; // 1120
    int pagewidth = 792;
    private Bitmap bmp, scaledbmp;
    SessionManagement session;
    private HashMap<String, String> user;
    //</editor-fold>

    public ReceiptReportAdapter(Context context, ArrayList<Receipt> tempArrayList) {
        this.context = context;
        this.dArrayList = tempArrayList;
        this.arrayList = new ArrayList<Receipt>();
        this.arrayList.addAll(ReceiptReportActivity.dreceiptArrayList);

        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.klogo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);

        session = new SessionManagement(context.getApplicationContext());
        Log.d(TAG, "Login Status " + session.isLoggedIn());
        session.checkLogin();
        user = session.getUserDetails();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_receipt_no, txt_receipt_dt, txt_srno, txt_route, txt_customer_name, txt_customer_address,
                txt_mobile, txt_contact, txt_old_balance, txt_paymode, txt_receipt_amt, txt_cheque_no, txt_cheque_dt,
                txt_cheque_issue_bank, txt_cls_amt, txt_print;

        public MyViewHolder(View view) {
            super(view);

            txt_receipt_no = view.findViewById(R.id.lay_receipt_txt_receipt_no);
            txt_receipt_dt = view.findViewById(R.id.lay_receipt_txt_receipt_dt);
            txt_srno = view.findViewById(R.id.lay_receipt_txt_srno);

            txt_route = view.findViewById(R.id.lay_delivery_txt_route);
            txt_customer_name = view.findViewById(R.id.lay_receipt_txt_customer_name);
            txt_customer_address = view.findViewById(R.id.lay_receipt_txt_customer_address);
            txt_contact = view.findViewById(R.id.lay_receipt_txt_contact);
            txt_mobile = view.findViewById(R.id.lay_receipt_txt_mobile);
            txt_old_balance = view.findViewById(R.id.lay_receipt_txt_old_balance);
            txt_paymode = view.findViewById(R.id.lay_receipt_txt_paymode);
            txt_receipt_amt = view.findViewById(R.id.lay_receipt_txt_receipt_amt);
            txt_cls_amt = view.findViewById(R.id.lay_receipt_txt_cls_amt);
            txt_cheque_no = view.findViewById(R.id.lay_receipt_txt_cheque_no);
            txt_cheque_dt = view.findViewById(R.id.lay_receipt_txt_cheque_dt);
            txt_cheque_issue_bank = view.findViewById(R.id.lay_receipt_txt_cheque_issue_bank);
            txt_print = view.findViewById(R.id.lay_receipt_txt_print);
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
        holder.txt_old_balance.setText(dArrayList.get(position).getOld_Balance());
        if (Double.valueOf(dArrayList.get(position).getOld_Balance()) > 0) {
            holder.txt_old_balance.setTextColor(ContextCompat.getColor(context, R.color.darkred));
        } else {
            holder.txt_old_balance.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.txt_mobile.setText(dArrayList.get(position).getMobile_No());
        holder.txt_paymode.setText(dArrayList.get(position).getPayMode_Name());
        holder.txt_receipt_amt.setText(dArrayList.get(position).getReceive_Amount());
        holder.txt_cls_amt.setText(dArrayList.get(position).getCls_Balance());
        if (Double.valueOf(dArrayList.get(position).getCls_Balance()) > 0) {
            holder.txt_cls_amt.setTextColor(ContextCompat.getColor(context, R.color.darkred));
        } else {
            holder.txt_cls_amt.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        holder.txt_cheque_no.setText(dArrayList.get(position).getCheque_No());
        holder.txt_cheque_dt.setText(dArrayList.get(position).getCheque_Date());
        holder.txt_cheque_issue_bank.setText(dArrayList.get(position).getCheque_IssueBank());
        holder.txt_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF(dArrayList.get(position).getReceipt_No(),
                        dArrayList.get(position).getReceipt_Dt(),
                        dArrayList.get(position).getCustomer_Name(),
                        dArrayList.get(position).getReceive_Amount(),
                        dArrayList.get(position).getReceive_Amount_InWords(), "NA");
            }
        });
    }

    @Override
    public int getItemCount() {
        return dArrayList == null ? 0 : dArrayList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ReceiptReportActivity.dreceiptArrayList.clear();
        if (charText.length() == 0) {
            ReceiptReportActivity.dreceiptArrayList.addAll(arrayList);
        } else {
            for (Receipt objFilter : arrayList) {
                if (objFilter.getCustomer_Name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ReceiptReportActivity.dreceiptArrayList.add(objFilter);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void generatePDF(String recNo, String recDt, String custName, String recAmt, String recAmtInWords, String remark) {

        //<editor-fold desc="Description">
        String fileName = "Receipt.pdf";
        File dir;
        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            dir = new File(Environment.getExternalStorageDirectory().getPath() + "//KALA");
        } else {
            dir = new File(context.getApplicationContext().getExternalFilesDir("KALA").getAbsolutePath());
            //dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "//KALA");
        }
        if (dir.exists()) {
            for (File tempFile : dir.listFiles()) {
                tempFile.delete();
            }
        }
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, fileName);
        //</editor-fold>

        int x = 130, x2 = 220, x3 = 520, x4 = 620, y = 80;
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();
        Paint imgLogo = new Paint();
        Paint rowText = new Paint();
        canvas.drawBitmap(scaledbmp, 30, 80, imgLogo);

        drawSingalLineHedingText(canvas, "PAYMENT RECEIPT", 300, y, rowText);
        y += 10;
        canvas.drawLine(x, y, 740, y, rowText);
        y += 25;
        drawSingalLineSubHedingText(canvas, user.get(SessionManagement.COMPANY_NAME), x, y, rowText);
        y += 20;

        int index = 0;
        String tmpString = user.get(SessionManagement.COMPANY_ADDRESS);
        StringBuffer company_Address =new StringBuffer();
        while (index < tmpString.length()) {
            company_Address.append(tmpString.substring(index, Math.min(index + 55,tmpString.length()))+"-\n");
            index += 55;
        }

        drawMultiLineText(canvas, company_Address.toString(), x, y, rowText, 1);
        y += 30;
        canvas.drawLine(x, y, 740, y, rowText);
        y += 30;

        //Start Row 1
        drawSingalLineBoldText(canvas, "Receipt No:", x, y, rowText);
        drawSingalLineNormalText(canvas, recNo, x2, y, rowText);

        drawSingalLineBoldText(canvas, "Receipt Date:", x3, y, rowText);
        drawSingalLineNormalText(canvas, recDt, x4, y, rowText);
        y += 25;
        //End Row 1

        //Start Row 2
        drawSingalLineBoldText(canvas, "Customer:", x, y, rowText);
        drawSingalLineNormalText(canvas, custName, x2, y, rowText);

        drawSingalLineBoldText(canvas, "Rec. Amount:", x3, y, rowText);
        drawSingalLineNormalText(canvas, recAmt +"/-", x4, y, rowText);
        y += 25;
        //End Row 2

        //Start Row 3
        drawSingalLineBoldText(canvas, "Rec. Amount(In Words):", x, y, rowText);
        drawSingalLineNormalText(canvas, recAmtInWords, 310, y, rowText);
        y += 25;
        //End Row 3

        //Start Row 4
        drawSingalLineBoldText(canvas, "Remark:", x, y, rowText);
        drawSingalLineNormalText(canvas, remark, x2, y, rowText);
        //End Row 4

        y += 100;
        drawSingalLineSubHedingText(canvas, "For " +user.get(SessionManagement.COMPANY_NAME), 460, y, rowText);
        y += 80;
        drawSingalLineSubHedingText(canvas, "Authorized Signature", 470, y, rowText);
        y += 20;
        canvas.drawLine(x, y, 740, y, rowText);
        y += 25;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        drawSingalLineNormalText(canvas, "Generated on " + currentDate, 320, y, rowText);
        pdfDocument.finishPage(myPage);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            String path = dir.getPath() + "/" + fileName;
            Log.d(TAG, "path " + path);
            openPdf(path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }

    }

    public void drawSingalLineHedingText(Canvas canvas, String text, int x, int y, Paint paint) {
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText(text, x, y, paint);
    }

    public void drawSingalLineSubHedingText(Canvas canvas, String text, int x, int y, Paint paint) {
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(16);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText(text, x, y, paint);
    }

    public void drawSingalLineNormalText(Canvas canvas, String text, int x, int y, Paint paint) {
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(15);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText(text, x, y, paint);
    }

    public void drawSingalLineBoldText(Canvas canvas, String text, int x, int y, Paint paint) {
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(15);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText(text, x, y, paint);
    }

    public void drawMultiLineText(Canvas canvas, String text, int x, int y, Paint paint, float lineHeight) {
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        paint.setTextSize(15);
        paint.setColor(ContextCompat.getColor(context, R.color.black));

        String[] lines = text.split("\n");
        for (String line : lines) {
            canvas.drawText(line, x, y, paint);
            y += (-paint.ascent() + paint.descent()) * lineHeight;
        }
    }

    private void openPdf(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkURI = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".provider", file);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }
        context.startActivity(intent);
    }

}
