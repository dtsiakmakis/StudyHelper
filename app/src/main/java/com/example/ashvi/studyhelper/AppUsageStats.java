package com.example.ashvi.studyhelper;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppUsageStats extends AppCompatActivity {

    private TextView permissionMessage ;
    private Button ShowStats;
    private Boolean permission=Boolean.TRUE;
    private UsageStatsManager usageStatsManager;
    private Context context;
    PackageManager packageManager;
    private BarChart chart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_stats);

        context=getApplicationContext();
        packageManager = context.getPackageManager();
        permissionMessage = (TextView) findViewById(R.id.grant_permission_message);
        //PhoneUnlock = (TextView)findViewById(R.id.phone_unlock);
        ShowStats = (Button)findViewById(R.id.stats_button);
        //context=this.context;
        chart = (BarChart) findViewById(R.id.chart);
        usageStatsManager=(UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);





        permissionMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == permissionMessage){
                    openSettings();
                }
            }
        });

        ShowStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == ShowStats){
                    try {
                        ShowGraphs();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            }
        });



    }

    private void openSettings() {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        permission=true;
        permissionMessage.setText("DONE");
    }

    public void ShowGraphs() throws PackageManager.NameNotFoundException {
        if(permission==true)
        {
            // if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            //         != PackageManager.PERMISSION_GRANTED)
            Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(System.currentTimeMillis()-7200000, System.currentTimeMillis());
            List<UsageStats> stats = new ArrayList<>();
            List<String> app=new ArrayList<>();
            List<Long> app_time=new ArrayList<>();
            stats.addAll(usageStats.values());

            for (UsageStats stat : stats) {
                if(stat.getLastTimeUsed()!= 0L) {

                    ApplicationInfo ai = packageManager.getApplicationInfo(stat.getPackageName(), 0);
                    if(stat.getTotalTimeInForeground()!=0) {
                        app.add(packageManager.getApplicationLabel(ai).toString());
                        app_time.add(stat.getTotalTimeInForeground());

                    }

                }
            }
            permissionMessage.setText("App name :"+app.toString()+"\n Time Taken:"+app_time.toString());


            BarData data = new BarData(app, getDataSet(app_time));
            chart.setData(data);
            chart.setDescription("My Chart");
            chart.animateXY(2000, 2000);
            chart.invalidate();






        }
        else
        {
            permissionMessage.setText("Permission is false");
        }


    }

    private ArrayList<BarDataSet> getDataSet(List<Long> app_time) {
        ArrayList<BarDataSet> dataSets = null;
        /*
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);
        */
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        for (int counter = 0; counter < app_time.size(); counter++) {
            System.out.println(app_time.get(counter));
            BarEntry v2e1 = new BarEntry(app_time.get(counter), counter); // Jan
            valueSet2.add(v2e1);

        }
        /*
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        */

        // BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Apps");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        //dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

}
