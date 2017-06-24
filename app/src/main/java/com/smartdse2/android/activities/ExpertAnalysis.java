package com.smartdse2.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.smartdse2.android.utils.GlobalVars;
import com.smartdse2.android.R;
import com.smartdse2.android.utils.SrcGrabber;
import com.smartdse2.android.utils.Active_net_checking;
import com.smartdse2.android.utils.ButtonController;
import com.smartdse2.android.utils.Constants;

import java.io.IOException;
import java.net.URISyntaxException;

public class ExpertAnalysis extends Activity {


    ButtonController buttonController;
    WebView expert_analysis_web;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expert_analysis);

        buttonController = new ButtonController(this);
        expert_analysis_web = (WebView) findViewById(R.id.dse_expert_analysis);
        expert_analysis_web.getSettings();
        expert_analysis_web.setBackgroundColor(Color.BLACK);
        new grab_expert_analysis().execute();

    }


    class grab_expert_analysis extends AsyncTask<Void, Void, String>
    {
        ProgressDialog progressDialog = ProgressDialog.show(ExpertAnalysis.this, "",
                "Retrieving Data...", true);
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result!=null) {
                expert_analysis_web.loadDataWithBaseURL(null, result,  "text/html", "utf-8", null);
            }
            else
            {
                Toast.makeText(ExpertAnalysis.this, "Problem connecting to server!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SrcGrabber grabber = new SrcGrabber();
            try {
                if (Active_net_checking.testInte("104.131.22.246")) {
                    String expert_analysis_data = grabber.grabSource(Constants.EXPERT_ANALYSIS_LINK);
                    return expert_analysis_data;
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

}
