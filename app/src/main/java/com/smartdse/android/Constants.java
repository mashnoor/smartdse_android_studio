package com.smartdse.android;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by mashnoor on 1/12/16.
 */
public class Constants {
    //Texts
    public static final String ADD_PRICE_ALERT = "Add to Price Alert";
    public static final String CUSTOMIZE_PRICE_ALERT = "Customize price Alert";
    public static final String DATABASE_NAME = "dsedatabase";
    public static final String PRICE_ALERT_TABLE = "pricealertitems";
    public static final String FAVOURITE_COLOR = "#00CCCF";




    //Links
    public static final String AGM_URL ="http://104.131.22.246/dev/smartdsefiles/agm.txt";
    public static final String CURRENCY_CONVERT = "https://www.google.com/finance/converter?a=1&from=USD&to=BDT";
    public static final  String MARKET_DEPTH = "http://104.131.22.246/dev/smartdsefiles/depth/";
    public static final String LPT_VALUES = "http://104.131.22.246/dev/smartdsefiles/itemvalues_portfolio.txt";
    public static final String HOME_GRAPH_LINK = "http://104.131.22.246/dev/smartdsefiles/home_graph.txt";


    public static final String DEBUG_TAG = "SmartDSE";



    //Timer Counter

    static class LoadAd extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ButtonController.loadInterstitialAd();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }


    public static Thread showAdThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                try
                {
                    Thread.sleep(60000);
                    new LoadAd().execute("");



                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    });



}
