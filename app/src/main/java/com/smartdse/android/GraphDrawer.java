package com.smartdse.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mashnoor on 2/16/16.
 */
public class GraphDrawer {
    Activity activity;
    JSONArray home_graph_data;
    ArrayList<Entry> dsex_values;
    ArrayList<String> dsex_times;

    public GraphDrawer(Activity activity)

    {

        this.activity = activity;
    }

    public class grab_and_draw_graph extends AsyncTask<String, String, String>
    {

        ProgressDialog progressDialog = ProgressDialog.show(activity, "",
                "Processing Graph", true);

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }

            final LineChart lineChart = (LineChart) activity.findViewById(R.id.home_chart);





            LineDataSet dataset = new LineDataSet(dsex_values, "# of Calls");

            // creating labels



            final LineData data = new LineData(dsex_times, dataset);
            lineChart.setData(data); // set the data and list of lables into chart
            lineChart.setDescription("Description");  // set the description
            lineChart.setTouchEnabled(true);
            lineChart.setBackgroundColor(Color.BLACK);
            lineChart.setGridBackgroundColor(Color.BLACK);
            lineChart.setDrawBorders(true);
            lineChart.setBorderWidth(1f);
            lineChart.getAxisLeft().setTextColor(Color.WHITE);
            lineChart.getXAxis().setTextColor(Color.WHITE);
            dataset.setValueTextColor(Color.WHITE);
            lineChart.setBorderColor(Color.parseColor(Constants.FAVOURITE_COLOR));
            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    Toast.makeText(activity, lineChart.getXValue(e.getXIndex()), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected() {

                }
            });



        }

        @Override
        protected String doInBackground(String... params) {


            dsex_times = new ArrayList<>();
            dsex_values = new ArrayList<>();
            String home_graph = "";
            try {
                SrcGrabber grabber = new SrcGrabber();
                home_graph = grabber.grabSource(Constants.HOME_GRAPH_LINK);
                home_graph_data = new JSONArray(home_graph);
                for (int i = 0; i<home_graph_data.length(); i++)
                {
                    JSONObject curr_obj = home_graph_data.getJSONObject(i);
                    Iterator keys = curr_obj.keys();
                    String time = (String) keys.next();
                    float value = (float)curr_obj.getDouble(time);
                    dsex_times.add(time);
                    dsex_values.add(new Entry(value, i));
                }
            }
            catch (Exception e)
            {

            }

            return null;
        }
    }

    public void drawHomeGraph()
    {

       new grab_and_draw_graph().execute("");

    }



}
