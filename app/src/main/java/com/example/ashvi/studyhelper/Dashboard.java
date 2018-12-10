package com.example.ashvi.studyhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    private Button app_stats;
    private Button activity_stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        app_stats=(Button)findViewById(R.id.app_statistics);
        activity_stats=(Button)findViewById(R.id.activity_stats) ;

        app_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==app_stats){

                        startActivity(new Intent(getApplicationContext(), AppUsageStats.class));

                }
            }
        });
        activity_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==activity_stats){

                    startActivity(new Intent(getApplicationContext(), Activity_Stats.class));

                }
            }
        });
    }
}
