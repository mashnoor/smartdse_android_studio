package com.smartdse2.android.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smartdse2.android.R;
import com.smartdse2.android.services.PriceAlertService;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;
import com.smartdse2.android.utils.LoginHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class SplashActivity extends Activity {


    @BindView(R.id.splash_logo)
    ImageView splash_logo;

    AsyncHttpClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);

        if (LoginHelper.isActivate(SplashActivity.this)) {
            splash_logo.setImageResource(R.drawable.sd_splash_pro);
            Intent i = new Intent(SplashActivity.this, PriceAlertService.class);
            if (!isMyServiceRunning(PriceAlertService.class)) {
                startService(i);
            }
        }
        client = new AsyncHttpClient();

        ButtonController.header_texts = new ArrayList<>();

        checkForUpdate();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void checkForUpdate() {
        client.get(Constants.URLS.HEADER_TEXT_LINK, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                if (response.equals("New Version Available!")) {
                    showOnUpdateMsg();
                } else {
                    Intent main_activity = new Intent(SplashActivity.this, Home.class);
                    startActivity(main_activity);
                    ButtonController.header_texts.add(response);
                    finish();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ButtonController.header_texts.add("No internet connection");

            }
        });

    }

    private void showOnUpdateMsg() {
        new AlertDialog.Builder(
                SplashActivity.this)
                .setTitle("New Version Available!")
                .setMessage(
                        "Good News! SmartDSE has been updated!\nNew version is now available to download.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface arg0,
                                    int arg1) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                        .parse("market://details?id=" + getApplicationContext().getPackageName())));

                            }
                        }).setCancelable(false).show();
    }


}


