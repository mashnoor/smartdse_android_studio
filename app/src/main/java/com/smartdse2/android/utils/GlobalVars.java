package com.smartdse2.android.utils;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.smartdse2.android.activities.Home;
import com.smartdse2.android.activities.IPOActivity;
import com.smartdse2.android.activities.ItemDetailWeb;
import com.smartdse2.android.activities.ItemInfo;
import com.smartdse2.android.activities.MainActivity;
import com.smartdse2.android.activities.MarketDepth;
import com.smartdse2.android.activities.NewsActivity;
import com.smartdse2.android.activities.NewsDetail;
import com.smartdse2.android.activities.Portfolio_activity;
import com.smartdse2.android.activities.PriceAlert;
import com.smartdse2.android.activities.SendID;
import com.smartdse2.android.activities.StockOnNewsPaper;
import com.smartdse2.android.activities.Top10Gainers;
import com.smartdse2.android.activities.Top10Shares;
import com.smartdse2.android.activities.Watch_List_list_view;
import com.smartdse2.android.activities.WeeklyReport;
import com.smartdse2.android.activities.AGMActivity;
import com.smartdse2.android.activities.About;
import com.smartdse2.android.activities.BuyNow;
import com.smartdse2.android.activities.Chat;
import com.smartdse2.android.activities.Currency;
import com.smartdse2.android.activities.DSE30List;
import com.smartdse2.android.activities.ExpertAnalysis;
import com.smartdse2.android.activities.FullScreenGraph;
import com.smartdse2.android.models.AGM;

import java.util.ArrayList;

/**
 * Created by Mashnoor on 12/26/16.
 */

public class GlobalVars extends Application {
    static  int totalActivitiesPaused;
    String[] activities = {About.class.getSimpleName(), AGM.class.getSimpleName(), AGMActivity.class.getSimpleName(),

                            BuyNow.class.getSimpleName(), Chat.class.getSimpleName(), Currency.class.getSimpleName(),
                            DSE30List.class.getSimpleName(),  ExpertAnalysis.class.getSimpleName(), FullScreenGraph.class.getSimpleName(),
                            Home.class.getSimpleName(), IPOActivity.class.getSimpleName(), ItemDetailWeb.class.getSimpleName(),
                            ItemInfo.class.getSimpleName(), MainActivity.class.getSimpleName(), MarketDepth.class.getSimpleName(),
                            NewsActivity.class.getSimpleName(), NewsDetail.class.getSimpleName(), Portfolio_activity.class.getSimpleName(),
                            PriceAlert.class.getSimpleName(), SendID.class.getSimpleName(), StockOnNewsPaper.class.getSimpleName(),
                            Top10Gainers.class.getSimpleName(), Top10Shares.class.getSimpleName(), Watch_List_list_view.class.getSimpleName(),
                            WeeklyReport.class.getSimpleName()

                        };


    static ArrayList<String> runningActivities;
    static int totalActivitiesResumed;
    @Override
    public void onCreate() {
        super.onCreate();
        totalActivitiesPaused = 0;
        totalActivitiesResumed = 0;
        runningActivities = new ArrayList();
    }



    public static void activtyPaused(Activity activity)
    {
        //Log.d("--------", "" + GlobalVars.getTotalActivities());
        //Log.d("--------", activity.getClass().getSimpleName());
        totalActivitiesPaused++;
        if(runningActivities.contains(activity.getClass().getSimpleName()))
        {
            runningActivities.remove(activity.getClass().getSimpleName());
        }

    }
    public static void activityResumed(Activity activity)
    {
        //Log.d("--------", "" + GlobalVars.getTotalActivities());
        if(!runningActivities.contains(activity.getClass().getSimpleName())) {
            runningActivities.add(activity.getClass().getSimpleName());
        }


        totalActivitiesResumed++;
    }
    public static int getTotalActivities()
    {
        return (totalActivitiesResumed-totalActivitiesPaused);

    }
    public static boolean isAllActivityPaused()
    {
        Log.d("--------", runningActivities.size() + "");
        if(runningActivities!=null && runningActivities.size()>0)
            return false;
        else
            return true;
    }
}
