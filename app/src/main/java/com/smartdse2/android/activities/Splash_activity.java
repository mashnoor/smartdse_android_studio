package com.smartdse2.android.activities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.smartdse2.android.R;
import com.smartdse2.android.utils.SrcGrabber;
import com.smartdse2.android.services.PriceAlertService;
import com.smartdse2.android.services.RegistrationIntentService;
import com.smartdse2.android.utils.Active_net_checking;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;
import com.smartdse2.android.utils.DevTools;
import com.smartdse2.android.utils.LoginHelper;
import com.smartdse2.android.utils.QuickstartPreferences;


public class Splash_activity extends Activity {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private BroadcastReceiver mRegistrationBroadcastReceiver;

    ImageView splash_logo;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.splash_activity);

        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        if(LoginHelper.isActivate(Splash_activity.this))
        {
            splash_logo.setImageResource(R.drawable.sd_splash_pro);
            Intent i = new Intent(Splash_activity.this, PriceAlertService.class);

            if (!isMyServiceRunning(PriceAlertService.class))
            {
                startService(i);
            }
        }





        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {



                } else {


                    Toast.makeText(getApplicationContext(), "Token not GOT!!", Toast.LENGTH_LONG).show();
                }
            }
        };


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }



        ButtonController.header_texts = new ArrayList<String>();

        new upload_to_server().execute("");

    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

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

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }




    class upload_to_server extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            if(ButtonController.header_texts.get(0).equals("New Version Available!"))
            {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        Splash_activity.this)
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
            else {
                final Intent main_activity = new Intent(Splash_activity.this, Home.class);

                    if(!Constants.showAdThread.isAlive())
                    {
                        Constants.showAdThread.start();
                    }
                startActivity(main_activity);

                finish();



            }


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (!Active_net_checking.testInte("104.131.22.246")) {
                    if (DevTools.fileExistance(Splash_activity.this,
                            ButtonController.ADV_FILE_NAME)) {
                        process_josn(DevTools.read_file(Splash_activity.this,
                                ButtonController.ADV_FILE_NAME));
                    } else {
                        String no_data = "[{\"ad\":\"Enable internet connection!\"}]";
                        process_josn(no_data);
                    }
                    return null;
                }
            } catch (IOException | JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                String adData;

                adData = SrcGrabber
                        .grabSource(ButtonController.header_text_link);

                process_josn(adData);
                DevTools.write_file(Splash_activity.this,
                        ButtonController.ADV_FILE_NAME, adData);



            } catch (IOException | URISyntaxException | JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }



                return null;
            }


        }

        private void process_josn(String adData) throws JSONException {
            JSONArray adarray = new JSONArray(adData);
            JSONObject adObject;
            int adSize = adarray.length();
            for (int i = 0; i < adSize; i++) {
                adObject = adarray.getJSONObject(i);
                ButtonController.header_texts.add(adObject.getString("ad"));
            }

        }

    }

