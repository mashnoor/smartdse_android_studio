package com.smartdse2.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;
import com.smartdse2.android.utils.GlobalVars;
import com.smartdse2.android.R;

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
