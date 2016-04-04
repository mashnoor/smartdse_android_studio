package com.smartdse2.android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

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
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class Splash_activity extends Activity {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    ImageView splash_logo;

    private final static String USER_AGENT = "Mozilla/5.0";
    String cdate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
            .getInstance().getTime());
    String ctime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance()
            .getTime());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.splash_activity);
        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        if(LoginHelper.isActivate(Splash_activity.this))
        {
            splash_logo.setImageResource(R.drawable.sd_splash_pro);
        }

        Intent i = new Intent(Splash_activity.this, PriceAlertService.class);

        if (!isMyServiceRunning(PriceAlertService.class))
        {
            startService(i);
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
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private String getIMIE() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imie = tMgr.getDeviceId();
        return imie;
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
                Intent main_activity = new Intent(Splash_activity.this, Home.class);
                if(!LoginHelper.isActivate(Splash_activity.this))
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
                SrcGrabber adgrabber = new SrcGrabber();
                adData = adgrabber
                        .grabSource(ButtonController.header_text_link);

                process_josn(adData);
                DevTools.write_file(Splash_activity.this,
                        ButtonController.ADV_FILE_NAME, adData);

				/*
				 * URL header_data_url; header_data_url = new
				 * URL(ButtonController.header_text_link); Scanner scanner = new
				 * Scanner(header_data_url.openStream());
				 * 
				 * 
				 * while (scanner.hasNext()) { String temp_string =
				 * scanner.nextLine(); System.out.println(temp_string);
				 * 
				 * ButtonController.header_texts.add(temp_string); }
				 * scanner.close();
				 */

            } catch (IOException | URISyntaxException | JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            StringBuilder upload_string = new StringBuilder();
            String l = "mashnoor";
            upload_string.append("imie=" + getIMIE() + "&lastdate='" + cdate
                    + "'&lasttime='" + ctime + "'&");

            Map<String, String> portfolio_items = Portfolio_menu_helper
                    .need_to_upload(Splash_activity.this);
            if (portfolio_items == null) {

                try {
                    String url = "http://104.131.22.246/dev/smartdsefiles/update_portfolio.php";
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj
                            .openConnection();

                    // add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    String urlParameters = removeLastChar(upload_string
                            .toString());

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(
                            con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result

                } catch (Exception e) {
                    // TODO: handle exception
                }

                return null;
            }
            for (String key : portfolio_items.keySet()) {

                upload_string
                        .append(key + "=" + portfolio_items.get(key) + "&");

            }

            try {
                String url = "http://104.131.22.246/dev/smartdsefiles/update_portfolio.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj
                        .openConnection();

                // add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = removeLastChar(upload_string.toString());

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(
                        con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
				/*
				 * System.out.println("\nSending 'POST' request to URL : " +
				 * url); System.out.println("Post parameters : " +
				 * urlParameters); System.out.println("Response Code : " +
				 * responseCode);
				 */
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                // System.out.println(response.toString());
            } catch (Exception e) {
                // TODO: handle exception
            }

            return null;
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

}
