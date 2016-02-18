package com.smartdse.android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Currency extends AppCompatActivity {

    WebView currency_web;
    ButtonController buttonController;
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
