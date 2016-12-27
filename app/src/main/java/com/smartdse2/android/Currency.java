package com.smartdse2.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Currency extends Activity {

    WebView currency_web;
    ButtonController buttonController;
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
        setContentView(R.layout.activity_currency);
        buttonController = new ButtonController(this);
        currency_web = (WebView) findViewById(R.id.currency_web);
        currency_web.setWebViewClient(new WebViewClient());
        currency_web.loadUrl(Constants.CURRENCY_CONVERT);
    }

}
