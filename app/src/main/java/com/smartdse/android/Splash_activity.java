package com.smartdse.android;

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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;


public class Splash_activity extends Activity {

    private final static String USER_AGENT = "Mozilla/5.0";
    String cdate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
            .getInstance().getTime());
    String ctime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance()
            .getTime());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.splash_activity);
        ButtonController.header_texts = new ArrayList<String>();

        new upload_to_server().execute("");

    };

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

            Intent main_activity = new Intent(Splash_activity.this, Home.class);
            startActivity(main_activity);
            finish();

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
