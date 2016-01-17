package com.smartdse.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.smartdse.android.R;

public class ButtonController {

    private boolean sentfromportfolioactivity = false;
    public static String update_status = "Not Updated Yet";
    final static String header_text_link = "http://104.131.22.246/dev/smartdsefiles/header_text.txt";
    final static String ADV_FILE_NAME = "advertise";
    public static ArrayList<String> header_texts;
    Handler handler;
    Thread thread;
    int i = 0;

    private volatile boolean thread_flag = false;

    public static void change_down_panel_color(Activity sentacActivity,
                                               int layout_id) {
        View down_color;
        down_color = sentacActivity.findViewById(layout_id);
        down_color.setBackgroundColor(Color.parseColor("#ff33b5e5"));

    }

    class header_text_thread implements Runnable {

        @Override
        public void run() {
            if (header_texts != null) {

                int k = header_texts.size();
                thread_flag = true;
                while (thread_flag) {
                    if (i == k) {
                        i = 0;
                    }
                    Message message = Message.obtain();
                    message.arg1 = i;
                    handler.sendMessage(message);
                    ++i;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    public ButtonController(final Activity sentactivity) {

        final View logo, home, items, search, watchlist, portfolio, news, top20, gainerloser, close_app, rateus, refresh_button, menuicon;
        final TextView update_status_tv;
        refresh_button = sentactivity.findViewById(R.id.dse_home_refresh);

        // panel_show_hide_button =
        // sentactivity.findViewById(R.id.panel_show_hide);

        //Adding the Drawer
        new DrawerBuilder().withActivity(sentactivity).build();
        //Adding the items... :D
        PrimaryDrawerItem home_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.home)).withName("Home").withSelectable(false);
        PrimaryDrawerItem a_z_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.itemlist)).withName("A to Z");
        PrimaryDrawerItem search_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.search)).withName("Search");
        PrimaryDrawerItem watchlist_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.watchlist)).withName("Watchlist");
        PrimaryDrawerItem portfolio_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.portfolio)).withName("Portfolio");
        PrimaryDrawerItem news_and_ipo_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.news)).withName("News and IPO");
        PrimaryDrawerItem top20_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.top20)).withName("Top 20");
        PrimaryDrawerItem gainerloser_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.gainerloser)).withName("Top Gainer Loser");
        PrimaryDrawerItem currency_convert_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.currency)).withName("Currency Conversion");

        PrimaryDrawerItem rateus_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.rateus)).withName("Rate Us");
        PrimaryDrawerItem info_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.info)).withName("Info");
        PrimaryDrawerItem quit_item = new PrimaryDrawerItem().withIcon(sentactivity.getResources().getDrawable(R.drawable.quit)).withName("Quit");










//create the drawer and remember the `Drawer` result object
         int clickedPosition;
        final Drawer result = new DrawerBuilder()

                .withActivity(sentactivity)

                .addDrawerItems(
                        home_item,
                        a_z_item,
                        search_item,
                        watchlist_item,
                        portfolio_item,
                        news_and_ipo_item,
                        top20_item,
                        gainerloser_item,
                        currency_convert_item,
                        rateus_item,
                        info_item,
                        quit_item
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        //Handle the clicks
                        if (position == 0)
                        {
                            MainActivity.show = false;
                            Intent homeiIntent = new Intent(sentactivity, Home.class);
                            sentactivity.startActivity(homeiIntent);



                        }
                      else if(position == 1)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    MainActivity.class);
                            sentactivity.startActivity(itemintent);
                        }
                        else if(position == 2)
                        {
                            if (sentactivity instanceof MainActivity) {
                                View search_box = sentactivity
                                        .findViewById(R.id.dse_list_search_box);

                                int search_box_visibility = search_box.getVisibility();
                                if (search_box_visibility == View.GONE) {
                                    InputMethodManager imm = (InputMethodManager) sentactivity
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    Animation animation;
                                    animation = AnimationUtils.loadAnimation(sentactivity,
                                            R.anim.fade_in);
                                    sentactivity.findViewById(R.id.dse_list_search_box)
                                            .setAnimation(animation);
                                    sentactivity.findViewById(R.id.dse_list_search_box)
                                            .setVisibility(View.VISIBLE);
                                    search_box.requestFocus();
                                    imm.showSoftInput(search_box, 0);

                                } else if (search_box_visibility == View.VISIBLE) {
                                    InputMethodManager imm = (InputMethodManager) sentactivity
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    Animation animation;
                                    animation = AnimationUtils.loadAnimation(sentactivity,
                                            R.anim.fade_out);
                                    sentactivity.findViewById(R.id.dse_list_search_box)
                                            .setAnimation(animation);
                                    sentactivity.findViewById(R.id.dse_list_search_box)
                                            .setVisibility(View.GONE);

                                    imm.hideSoftInputFromWindow(
                                            search_box.getWindowToken(), 0);
                                }

                            } else {
                                Intent itemintent = new Intent(sentactivity,
                                        MainActivity.class);
                                sentactivity.startActivity(itemintent);
                                MainActivity.show = true;
                            }

                        }
                        else if(position == 3)
                        {
                            MainActivity.show = false;
                            Intent watchlist = new Intent(sentactivity,
                                    Watch_List_list_view.class);
                            sentactivity.startActivity(watchlist);

                        }
                        else if (position == 4)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    Portfolio_activity.class);
                            sentactivity.startActivity(itemintent);

                        }
                        else if (position == 5)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    NewsActivity.class);
                            sentactivity.startActivity(itemintent);
                        }
                        else if (position == 6)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    Top10Shares.class);
                            sentactivity.startActivity(itemintent);

                        }
                        else if(position == 7)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    Top10Gainers.class);
                            sentactivity.startActivity(itemintent);
                        }
                        else if (position == 8)
                        {
                            MainActivity.show = false;
                            Intent itemintent = new Intent(sentactivity,
                                    Currency.class);
                            sentactivity.startActivity(itemintent);
                        }

                        else if (position == 9)
                        {
                            sentactivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse("market://details?id=" + sentactivity.getApplicationContext().getPackageName())));

                        }
                        else if(position == 10)
                        {
                            MainActivity.show = false;
                            Intent intent = new Intent(sentactivity, About.class);
                            sentactivity.startActivity(intent);
                        }
                        else if(position == 11)
                        {
                            sentactivity.finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            sentactivity.startActivity(intent);
                        }




                      return  true;
                    }
                })
                .build();



    /***
        home = sentactivity.findViewById(R.id.dhome);
        items = sentactivity.findViewById(R.id.ditem);
        search = sentactivity.findViewById(R.id.dsearch);
        watchlist = sentactivity.findViewById(R.id.dwatchlist);
        portfolio = sentactivity.findViewById(R.id.dportfolio);
        news = sentactivity.findViewById(R.id.dnews);
        top20 = sentactivity.findViewById(R.id.dtop20);
        gainerloser = sentactivity.findViewById(R.id.dgainerloser);
        rateus = sentactivity.findViewById(R.id.drateus);
        close_app = sentactivity.findViewById(R.id.dcloseapp);

     ***/
        logo = sentactivity.findViewById(R.id.sdse_logo);
        menuicon = sentactivity.findViewById(R.id.menu_icon);
        update_status_tv = (TextView) sentactivity
                .findViewById(R.id.dse_marketstatus);


        update_status_tv.setText(update_status);



        // Handler for showing advertisement
        if (!(sentactivity instanceof Home)) {
            final Animation animation, animation2;
            animation = AnimationUtils.loadAnimation(sentactivity,
                    R.anim.fade_out);
            animation2 = AnimationUtils.loadAnimation(sentactivity,
                    R.anim.fade_in);
            thread = new Thread(new header_text_thread());
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    if (msg.arg1 == header_texts.size()) {
                        return;
                    } else if (header_texts.isEmpty()) {
                        return;
                    } else if (msg.arg1 > (header_texts.size() - 1)) {
                        return;
                    }
                    Spannable s = null;
                    try {
                        s = (Spannable) Html
                                .fromHtml(ButtonController.header_texts
                                        .get(msg.arg1));
                    } catch (Exception e) {
                        return;
                    }

                    for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
                        s.setSpan(new UnderlineSpan() {
                            public void updateDrawState(TextPaint tp) {
                                tp.setUnderlineText(false);
                            }
                        }, s.getSpanStart(u), s.getSpanEnd(u), 0);
                    }
                    update_status_tv.startAnimation(animation);

                    update_status_tv.setText(s);

                    update_status_tv.setMovementMethod(LinkMovementMethod
                            .getInstance());
                    update_status_tv.startAnimation(animation2);
                }
            };
            thread.start();

        }

        // Logo

        if (!(sentactivity instanceof Home)) {
            logo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    MainActivity.show = false;
                    Intent homeiIntent = new Intent(sentactivity, Home.class);
                    sentactivity.startActivity(homeiIntent);
                    ;

                }
            });

        }
        //Menu Click Listener
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result.openDrawer();

            }
        });

		/*
		 * This if for showing panel show/hide
		 * panel_show_hide_button.setOnClickListener(new View.OnClickListener()
		 * {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * if (isSentfromportfolioactivity()) { if
		 * (showingmenuinportfolioactivity) {
		 * 
		 * RelativeLayout.LayoutParams lParams = new
		 * RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 * lParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); View view =
		 * sentactivity .findViewById(R.id.totalcalculation);
		 * 
		 * view.setLayoutParams(lParams); showingmenuinportfolioactivity =
		 * false; } else if (!showingmenuinportfolioactivity) {
		 * RelativeLayout.LayoutParams lParams = new
		 * RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 * lParams.addRule(RelativeLayout.ABOVE, R.id.down_panel_rellayout);
		 * View view = sentactivity .findViewById(R.id.totalcalculation);
		 * view.setLayoutParams(lParams); showingmenuinportfolioactivity = true;
		 * }
		 * 
		 * }
		 * 
		 * if (PanelController.down_panel_show) { Animation animation; animation
		 * = AnimationUtils.loadAnimation(sentactivity, R.anim.bottom_down);
		 * downpanelLayout.setAnimation(animation);
		 * downpanelLayout.setVisibility(View.GONE);
		 * PanelController.down_panel_show = false; } else { Animation
		 * animation; animation = AnimationUtils.loadAnimation(sentactivity,
		 * R.anim.bottom_up); downpanelLayout.setAnimation(animation);
		 * downpanelLayout.setVisibility(View.VISIBLE);
		 * PanelController.down_panel_show = true; }
		 * 
		 * } });
		 */

        /***

        // Home
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof Home)) {
                    MainActivity.show = false;
                    Intent homeiIntent = new Intent(sentactivity, Home.class);
                    sentactivity.startActivity(homeiIntent);

                }

            }
        });

        // Items
        items.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (sentactivity instanceof MainActivity) {
                    Bundle tbBundle = sentactivity.getIntent().getExtras();
                    if (tbBundle!=null) {
                        Intent itemintent = new Intent(sentactivity,
                                MainActivity.class);
                        sentactivity.startActivity(itemintent);

                    }
                }

                if (!(sentactivity instanceof MainActivity)) {
                    MainActivity.show = false;
                    Intent itemintent = new Intent(sentactivity,
                            MainActivity.class);
                    sentactivity.startActivity(itemintent);


                }

            }
        });

        // Search
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sentactivity instanceof MainActivity) {
                    View search_box = sentactivity
                            .findViewById(R.id.dse_list_search_box);

                    int search_box_visibility = search_box.getVisibility();
                    if (search_box_visibility == View.GONE) {
                        InputMethodManager imm = (InputMethodManager) sentactivity
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        Animation animation;
                        animation = AnimationUtils.loadAnimation(sentactivity,
                                R.anim.fade_in);
                        sentactivity.findViewById(R.id.dse_list_search_box)
                                .setAnimation(animation);
                        sentactivity.findViewById(R.id.dse_list_search_box)
                                .setVisibility(View.VISIBLE);
                        search_box.requestFocus();
                        imm.showSoftInput(search_box, 0);

                    } else if (search_box_visibility == View.VISIBLE) {
                        InputMethodManager imm = (InputMethodManager) sentactivity
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        Animation animation;
                        animation = AnimationUtils.loadAnimation(sentactivity,
                                R.anim.fade_out);
                        sentactivity.findViewById(R.id.dse_list_search_box)
                                .setAnimation(animation);
                        sentactivity.findViewById(R.id.dse_list_search_box)
                                .setVisibility(View.GONE);

                        imm.hideSoftInputFromWindow(
                                search_box.getWindowToken(), 0);
                    }

                } else {
                    Intent itemintent = new Intent(sentactivity,
                            MainActivity.class);
                    sentactivity.startActivity(itemintent);
                    MainActivity.show = true;
                }

            }
        });

        // Watchlist
        watchlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof Watch_List_list_view)) {
                    MainActivity.show = false;
                    Intent watchlist = new Intent(sentactivity,
                            Watch_List_list_view.class);
                    sentactivity.startActivity(watchlist);

                }

            }
        });

        // Portfolio
        portfolio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof Portfolio_activity)) {
                    MainActivity.show = false;
                    Intent itemintent = new Intent(sentactivity,
                            Portfolio_activity.class);
                    sentactivity.startActivity(itemintent);

                }

            }
        });

        // News
        news.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof NewsActivity)) {
                    MainActivity.show = false;
                    Intent itemintent = new Intent(sentactivity,
                            NewsActivity.class);
                    sentactivity.startActivity(itemintent);

                }

            }
        });

        // top20
        top20.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof Top10Shares)) {
                    MainActivity.show = false;
                    Intent itemintent = new Intent(sentactivity,
                            Top10Shares.class);
                    sentactivity.startActivity(itemintent);

                }

            }
        });

        // 10gainerloser
        gainerloser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof Top10Gainers)) {
                    MainActivity.show = false;
                    Intent itemintent = new Intent(sentactivity,
                            Top10Gainers.class);
                    sentactivity.startActivity(itemintent);

                }

            }
        });

        // Rate Us

        rateus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sentactivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + sentactivity.getApplicationContext().getPackageName())));

            }
        });

        // Close App
        close_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!(sentactivity instanceof About)) {
                    MainActivity.show = false;
                    Intent intent = new Intent(sentactivity, About.class);
                    sentactivity.startActivity(intent);
                }


            }
        });
         ***/
        // Refresh Button
        refresh_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                thread_flag = false;
                Intent refreshIntent = sentactivity.getIntent();
                sentactivity.startActivity(refreshIntent);
                sentactivity.finish();

            }
        });

    }

    public boolean isSentfromportfolioactivity() {
        return sentfromportfolioactivity;
    }

    public void setSentfromportfolioactivity(boolean sentfromportfolioactivity) {
        this.sentfromportfolioactivity = sentfromportfolioactivity;
    }

}