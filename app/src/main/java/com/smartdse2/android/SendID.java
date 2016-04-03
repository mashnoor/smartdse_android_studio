package com.smartdse2.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class SendID extends Activity {


    WebView purchase_web;
    Button buy_send_active;
    EditText buy_mobile, buy_tnxID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_id);

        initialize_all();

        purchase_web.getSettings();
        purchase_web.setBackgroundColor(Color.BLACK);
        buy_send_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(LoginHelper.isLoggedin(SendID.this))
                {
                    String email = LoginHelper.getUserEmail(SendID.this);
                    String loginWith = LoginHelper.getLoggedInUsing(SendID.this);
                    String tnxID = buy_tnxID.getText().toString().trim();
                    String mobile = buy_mobile.getText().toString().trim();
                    String IMEI = getIMEI();
                    if(isValid(email) && isValid(loginWith) && isValid(tnxID) && isValid(mobile))
                    {

                        new send_data_to_server().execute(email, loginWith, tnxID, mobile, IMEI);
                    }
                    else
                    {
                        showToast("Something wrong with your input. Please Check!");
                    }
                }
                else
                {
                    showToast("Please login and try again!");
                    Intent i = new Intent(SendID.this, Login_logout.class);
                    startActivity(i);
                }

            }
        });




    }

    private void showToast(String s) {
        Toast.makeText(SendID.this, s, Toast.LENGTH_LONG).show();
    }

    class send_data_to_server extends AsyncTask<String, Void, String>
    {

        ProgressDialog progressDialog = ProgressDialog.show(SendID.this,
                "", "Retrieving Data...", true);
        @Override
        protected String doInBackground(String... params) {

            String email =  params[0];
            String loginwith =  params[1];
            String tnxid =  params[2];
            String mobile = params[3];
            String imei = params[4];


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://104.131.22.246/dev/smartdsefiles/paid/save_payment.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("loggedinwith", loginwith));
                nameValuePairs.add(new BasicNameValuePair("imei", imei));
                nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
                nameValuePairs.add(new BasicNameValuePair("tnxid", tnxid));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                Log.i("--------------", responseString);
                return responseString;

            } catch (Exception e)
            {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(s!=null)
            {
                showToast(s);
            }
            else
            {
                showToast("Can't connect to server! Check your internet");
            }
        }
    }

    private boolean isValid(String s) {
        if (s.equals("") || s==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    private String getIMEI(){

        TelephonyManager mngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;

    }
    private void initialize_all() {
        purchase_web = (WebView) findViewById(R.id.purchase_web);
        buy_send_active = (Button) findViewById(R.id.buy_btn_send_active);
        buy_mobile = (EditText) findViewById(R.id.buy_mobile);
        buy_tnxID = (EditText) findViewById(R.id.buy_tnx_activation);
    }

}
