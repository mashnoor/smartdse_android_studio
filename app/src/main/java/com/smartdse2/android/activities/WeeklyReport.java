package com.smartdse2.android.activities;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartdse2.android.R;
import com.smartdse2.android.utils.Active_net_checking;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;


import java.io.IOException;
import java.net.URISyntaxException;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;


public class WeeklyReport extends Activity {

    @BindView(R.id.dse_weekly_report)
    WebView dse_weekly_report_webView;
    ButtonController buttonController;
    AlertDialog dialog;
    AsyncHttpClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_report);
        buttonController = new ButtonController(this);
        dialog = new SpotsDialog(this);
        dialog.setMessage("Getting data from server...");
        client = new AsyncHttpClient();
        dse_weekly_report_webView.getSettings();
        dse_weekly_report_webView.setBackgroundColor(Color.BLACK);
        getWeeklyReport();


    }
    private void getWeeklyReport()
    {
        client.get(Constants.URLS.WEEKLY_REPORT, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                dse_weekly_report_webView.loadDataWithBaseURL(null, response,  "text/html", "utf-8", null);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();

            }
        });
    }



}
