package com.smartdse2.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.smartdse2.android.R;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;
import com.smartdse2.android.utils.GlobalVars;


public class ItemDetailWeb extends Activity {


    ButtonController controller;
    WebView item_detail_web;
    private ProgressDialog progressBar;
    @Override
    protected void onPause() {
        super.onPause();
        GlobalVars.activtyPaused(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalVars.activityResumed(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_web);
        controller = new ButtonController(ItemDetailWeb.this);
        item_detail_web = (WebView) findViewById(R.id.itemdetailWeb);
        Intent i = getIntent();
        String item = i.getStringExtra("item");
        Log.d("-------------------", item);
        WebSettings settings = item_detail_web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        item_detail_web.setScrollbarFadingEnabled(false);
        settings.setLoadWithOverviewMode(true);
        item_detail_web.setInitialScale(1);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        item_detail_web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(ItemDetailWeb.this, "Detail of " + item, "Loading...");

        item_detail_web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("-------------------", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("-------------------", "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("-------------------", "Error: " + description);
                Toast.makeText(ItemDetailWeb.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        item_detail_web.loadUrl(Constants.ITEM_DETAIL_LINK + item);
    }


}


