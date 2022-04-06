package com.shree.maulifoods.utility;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shree.maulifoods.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

        downloadAndOpenPDF();

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
    }

    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {

                Uri path = Uri.fromFile(downloadFile(download_file_url));
                try {
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, "PDF Reader application is not installed in your device");
                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);
            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again.", Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.", Color.RED);
        } catch (final Exception e) {
            setTextError("Failed to download image. Please check your internet connection.", Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d(TAG, message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d(TAG, " Error txt " + txt);
            }
        });

    }

    class WebView1 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressInfo.ProgressHide();
        }
    }

}