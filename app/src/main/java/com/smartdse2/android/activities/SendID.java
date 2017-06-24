package com.smartdse2.android.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdse2.android.R;
import com.smartdse2.android.utils.SrcGrabber;
import com.smartdse2.android.utils.Active_net_checking;
import com.smartdse2.android.utils.Constants;
import com.smartdse2.android.utils.GlobalVars;
import com.smartdse2.android.utils.LoginHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class SendID extends Activity {


    WebView purchase_web;
    Button buy_send_active;
    EditText buy_mobile, buy_tnxID, buy_email;
    RadioGroup buy_options;


    TextView txtTxnActive;
    final int REQUEST_READ_PHONE_STATE = 11234;
    String imei;

    @Override
    protected void onPause() {
        super.onPause();
        GlobalVars.activtyPaused(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        GlobalVars.activityResumed(this);
        if(!LoginHelper.isLoggedin(this))
        {
           buy_email.setText("Not Logged In");
        }
        else
        {
            buy_email.setText(LoginHelper.getUserEmail(SendID.this));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_id);

        initialize_all();
        if(!LoginHelper.isLoggedin(this))
        {
            Intent i = new Intent(SendID.this, Login_logout.class);
            startActivity(i);
        }



        purchase_web.getSettings();
        purchase_web.setBackgroundColor(Color.BLACK);
        new grab_instruction().execute();
        buy_send_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buy_options.getCheckedRadioButtonId() == R.id.opt_tnxid) {

                    //Send the tnxid to server
                    sendToServer();

                } else if (buy_options.getCheckedRadioButtonId() == R.id.opt_code) {

                    checkActivationCode();


                }


            }
        });
        buy_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_get_info_dialog("Mobile", "Mobile Number:", R.id.buy_mobile, buy_mobile.getText().toString());
            }
        });
        buy_tnxID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buy_options.getCheckedRadioButtonId()==R.id.opt_tnxid)
                {
                    show_get_info_dialog("TTrx ID / Number:", "TTrx ID / Number:", R.id.buy_tnx_activation, buy_tnxID.getText().toString());

                }
                else
                {
                    show_get_info_dialog("Activation Code", "Activation Code", R.id.buy_tnx_activation, buy_tnxID.getText().toString());

                }
            }
        });

        buy_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.opt_tnxid)
                {
                    txtTxnActive.setText("Trx ID / Number:");
                }
                else if(checkedId==R.id.opt_code)
                {
                    txtTxnActive.setText("Activation Code:");
                }
            }
        });







    }

    private void showDialog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SendID.this);
        builder1.setMessage("You have successfully activated this copy.\n" +
                "Please keep your User ID and Activation code for future use.");
        builder1.setTitle("Congratulations!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Restart SmartDSE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        doRestart(SendID.this);
                    }
                });
        builder1.setCancelable(false);


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e("-------------", "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e("---------", "Was not able to restart application, PM null");
                }
            } else {
                Log.e("-------------", "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e("-----------------", "Was not able to restart application");
        }
    }



    private void checkActivationCode() {

        if (LoginHelper.isLoggedin(SendID.this)) {
            String email = LoginHelper.getUserEmail(SendID.this);

            String activation_code = buy_tnxID.getText().toString().trim();
            String mobile = buy_mobile.getText().toString().trim();

            if (isValid(email)  && isValid(activation_code) && isValid(mobile)) {

                new check_and_active().execute(email, activation_code, mobile);
            } else {
                showToast("Something wrong with your input. Please Check!");
            }
        } else {
            showToast("Please login and try again!");
            Intent i = new Intent(SendID.this, Login_logout.class);
            startActivity(i);
        }

    }

    private void sendToServer() {

        if (LoginHelper.isLoggedin(SendID.this)) {
            String email = LoginHelper.getUserEmail(SendID.this);
            String loginWith = LoginHelper.getLoggedInUsing(SendID.this);
            String tnxID = buy_tnxID.getText().toString().trim();
            String mobile = buy_mobile.getText().toString().trim();
            String IMEI = "Black";
            if (isValid(email) && isValid(loginWith) && isValid(tnxID) && isValid(mobile)) {

                new send_data_to_server().execute(email, loginWith, tnxID, mobile, IMEI);
            } else {
                showToast("Something wrong with your input. Please Check!");
            }
        } else {
            showToast("Please login and try again!");
            Intent i = new Intent(SendID.this, Login_logout.class);
            startActivity(i);
        }



    }

    private void showToast(String s) {
        Toast.makeText(SendID.this, s, Toast.LENGTH_LONG).show();
    }


    //Class for Checking and active the app
    class check_and_active extends AsyncTask<String, Void, String>
    {

        ProgressDialog progressDialog = ProgressDialog.show(SendID.this,
                "", "Contacting with SmartDSE server...", true);
        @Override
        protected String doInBackground(String... params) {

            String email =  params[0];

            String code =  params[1];
            String mobile = params[2];



            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://104.131.22.246/dev/smartdsefiles/paid/check_code.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", email));

                nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
                nameValuePairs.add(new BasicNameValuePair("activation_code", code));

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
                if(s.equals(Constants.ACTIVE_SUCCESS_MSG))
                {
                    LoginHelper.setActivationCode(SendID.this, buy_tnxID.getText().toString().trim());
                    showDialog();
                }
            }
            else
            {
                showToast("Can't connect to server! Check your internet");
            }
        }
    }




    class send_data_to_server extends AsyncTask<String, Void, String>
    {

        ProgressDialog progressDialog = ProgressDialog.show(SendID.this,
                "", "Contacting with SmartDSE server...", true);
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



    class grab_instruction extends AsyncTask<Void, Void, String>
    {
        ProgressDialog progressDialog = ProgressDialog.show(SendID.this, "",
                "Retrieving Data...", true);
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result!=null) {
                purchase_web.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
            }
            else
            {
                purchase_web.loadDataWithBaseURL(null, "<center><h1>Can't load instruction. Connect to the internet</h1></center>", "text/html", "utf-8", null);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SrcGrabber grabber = new SrcGrabber();
            try {
                if (Active_net_checking.testInte("104.131.22.246")) {
                    String ipo_data = grabber.grabSource(Constants.PAID_NOTICE);
                    return ipo_data;
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






    private void show_get_info_dialog(String title, String tv_text ,final int sent_from, String prev_data)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(this);
        tv.setText(tv_text);
        tv.setPadding(40, 40, 40, 40);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);

        final EditText et = new EditText(this);

        String etStr = et.getText().toString();
        TextView tv1 = new TextView(this);
        tv1.setText(tv_text);

        LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.bottomMargin = 5;

        layout.addView(tv1, tv1Params);
        layout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setTitle(title);
        // alertDialogBuilder.setMessage("Input Student ID");
        alertDialogBuilder.setCustomTitle(tv);
        et.setText(prev_data);

        et.setInputType(InputType.TYPE_CLASS_NUMBER);


        // Setting Positive "Yes" Button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(sent_from==R.id.buy_mobile)
                {
                    buy_mobile.setText(et.getText().toString().trim());
                }
                else if(sent_from==R.id.buy_tnx_activation)
                {
                    buy_tnxID.setText(et.getText().toString().trim());
                }

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }
    }
    private void initialize_all() {
        purchase_web = (WebView) findViewById(R.id.purchase_web);
        buy_send_active = (Button) findViewById(R.id.buy_btn_send_active);
        buy_mobile = (EditText) findViewById(R.id.buy_mobile);
        buy_tnxID = (EditText) findViewById(R.id.buy_tnx_activation);
        buy_options = (RadioGroup) findViewById(R.id.buy_options);
        txtTxnActive = (TextView) findViewById(R.id.txtTxnActive);
        buy_email = (EditText) findViewById(R.id.buy_email);
    }

}
