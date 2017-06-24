package com.smartdse2.android.utils;

import android.os.AsyncTask;

import com.smartdse2.android.utils.ButtonController;

/**
 * Created by mashnoor on 1/12/16.
 */
public class Constants {
    //Texts
    public static final String ADD_PRICE_ALERT = "Add to Price Alert";
    public static final String CUSTOMIZE_PRICE_ALERT = "Customize price Alert";
    private static final String DATABASE_NAME = "dsedatabase";
    private static final String PRICE_ALERT_TABLE = "pricealertitems";
    public static final String FAVOURITE_COLOR = "#00CCCF";

    public class URLS
    {
        private final static String BASE_URL = "http://104.131.22.246/dev/smartdsefiles/";
        public static final String AGM_URL =  BASE_URL + "agm.txt";
        public static final String CURRENCY_CONVERT = "https://www.google.com/finance/converter?a=1&from=USD&to=BDT";
        public static final  String MARKET_DEPTH = BASE_URL + "depth/";
        public static final String LPT_VALUES = BASE_URL + "itemvalues_portfolio.txt";
        public static final String HOME_GRAPH_LINK = BASE_URL + "dsex_graph.txt";
        public static final String VOLUME_GRAPH_LINK = BASE_URL + "volume_graph.txt";
        public static final String EXPERT_ANALYSIS_LINK = BASE_URL + "expert_analysis/analysis.txt";
        public static final String WEEKLY_REPORT = BASE_URL + "weekly_report.txt";
        public static final String STOCK_ON_NEWS_LINK = BASE_URL + "stock_on_news.txt";
        public static final String PAID_NOTICE = BASE_URL + "paid/paid_notice.txt";
        public static final String ITEM_DETAIL_LINK = "http://www.dsebd.org/displayCompany.php?name=";
        final public static String HEADER_TEXT_LINK= BASE_URL + "header_text_2.5.0.txt";
    }




    //Links



    public static final String LOGIN_NAME_NOT_SET = "Not Set";
    public static final String LOGGED_IN_USING_FB = "F";
    public static final String  LOGGED_IN_USING_GMAIL = "G";
    private static final String ACTIVATION_CODE = "activecode";
    private static final String IS_ACTIVATE = "isactivate";
    public static final String YES = "yes";
    public static final String NO = "no";


    //SharedPref Infos
    public static final String DEBUG_TAG = "SmartDSE";
    public static final String SHARED_PREF_NAME = "LoginDetail";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "useremail";
    public static final String LOGGEDIN_USING = "loggedinusing";

    public static final String CHANNEL_NAME = "SmartDSE_Release";
    public static final String LTP_FILE = "ltp_values.txt";



    //Messages
    public static final String ACTIVE_SUCCESS_MSG = "Congratulation! Succcessfully activated the app!";




}
