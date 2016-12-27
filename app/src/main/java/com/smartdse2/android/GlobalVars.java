package com.smartdse2.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

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
