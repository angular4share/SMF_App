package com.shree.maulifoods.utility;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.DIRECTORY_DOCUMENTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.shree.maulifoods.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFViewerActivity extends AppCompatActivity {

    private ProgressInfo progressInfo;
    private CommonUtil commonUtil;
    private NetworkUtil networkUtil;
    private ApiInterface apiService = null;
    private WebSettings webSettings;
    private WebView webview;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    String download_file_url = "https://docs.google.com/viewerng/viewer?embedded=true&url=https://kalapms.com/media/receipt.pdf", TAG = "***PDFViewerActivity***";
    float per = 0;

    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        commonUtil = new CommonUtil();
        networkUtil = new NetworkUtil();
        progressInfo = new ProgressInfo(PDFViewerActivity.this);
        apiService = RESTApi.getClient().create(ApiInterface.class);

        getSupportActionBar().setTitle("Receipt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0.0f);
        getSupportActionBar().show();

        //downloadAndOpenPDF();

      /*  Uri path = Uri.parse(download_file_url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

      /*  webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url){
                // do your stuff here
                progressbar.setVisibility(View.GONE);
            }
        });*/

       /* progressInfo.ProgressShow();
        //webview = (WebView) findViewById(R.id.webView);
        webview = new WebView(PDFViewerActivity.this);
        setContentView(webview);
        webview.setWebViewClient(new WebView1());
        url = "https://drive.google.com/viewerng/viewer?embedded=true&url=https://kalapms.com/media/receipt.pdf";
        webview.loadUrl(url);
        Log.d(TAG, " URL " + url);

         //Settings
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setBuiltInZoomControls(true);*/

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.klogo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        generatePDF();
        //createandDisplayPdf();
    }

    public void createandDisplayPdf() {

        try {
            String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/ProgrammerWorld.pdf";
            File file = new File(stringFilePath);

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Paint paint = new Paint();

            int x = 10, y = 25;

            for (String line : "Audumbar Gavali".split("\n")) {
                page.getCanvas().drawText(line, x, y, paint);

                y += paint.descent() - paint.ascent();
            }
            pdfDocument.finishPage(page);
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "ioException:" + e);
            }
            pdfDocument.close();

        } catch (Exception e) {
            Log.d(TAG, "ioException:" + e);
        }

        //viewPdf("newFile.pdf", "Dir");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(PDFViewerActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);

        pdfDocument.finishPage(myPage);
        String fileName = "Receipt.pdf";

        File dir = null;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d(TAG, "ok1");
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        } else {
            Log.d(TAG, "ok2");
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }*/

        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            dir = new File(Environment.getExternalStorageDirectory().getPath()+ "//KALA");
        } else {
            dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()+ "//KALA");
        }

        if (dir.exists()) {
            for (File tempFile : dir.listFiles()) {
                tempFile.delete();
            }
        }
        if (!dir.exists()) {
            dir.mkdir();
        }

        //File file = new File(dir, fileName);
        File file = new File(dir, fileName);
        Log.d(TAG, "filePath1 " + dir);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(PDFViewerActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            openPdf(dir + "/"+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }
    }

    private void openPdf(String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PDFViewerActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}