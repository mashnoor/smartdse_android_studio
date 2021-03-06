package com.smartdse2.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.webkit.WebView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.smartdse2.android.R;
import com.smartdse2.android.utils.Active_net_checking;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;


import java.io.IOException;
import java.net.URISyntaxException;

import butterknife.BindView;

public class StockOnNewsPaper extends Activity {


    @BindView(R.id.stock_on_newspaper_web)
    WebView stock_on_newspaperwebview;
    ButtonController buttonController;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_news_paper);
        buttonController = new ButtonController(this);

        stock_on_newspaperwebview.getSettings();
        stock_on_newspaperwebview.setBackgroundColor(Color.BLACK);
        new grab_stock_on_newspaper().execute();



    }

    private void showNews()
    {

    }


    class grab_stock_on_newspaper extends AsyncTask<Void, Void, String>
    {
        ProgressDialog progressDialog = ProgressDialog.show(StockOnNewsPaper.this, "",
                "Retrieving Data...", true);
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result!=null) {
                stock_on_newspaperwebview.loadDataWithBaseURL(null, result,  "text/html", "utf-8", null);
            }
            else
            {
                Toast.makeText(StockOnNewsPaper.this, "Problem connecting to server!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SrcGrabber grabber = new SrcGrabber();
            try {
                if (Active_net_checking.testInte("104.131.22.246")) {
                    String expert_analysis_data = grabber.grabSource(Constants.STOCK_ON_NEWS_LINK);
                    return expert_analysis_data;
                }
                else {
                    return null;
                }

            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }


        }

    }

}
