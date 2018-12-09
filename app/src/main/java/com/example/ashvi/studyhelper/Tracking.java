package com.example.ashvi.studyhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;



        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.support.v4.content.LocalBroadcastManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.location.DetectedActivity;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.concurrent.TimeUnit;

public class Tracking extends AppCompatActivity {

    private String TAG = Tracking.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;

    private TextView txtActivity, txtConfidence;
    private ImageView imgActivity;
    long start,stop;
    Date current_date;
    long tilting_time=0;
    long still_time=0;
    long foot_time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        txtActivity = findViewById(R.id.txt_activity);
        txtConfidence = findViewById(R.id.txt_confidence);
        imgActivity = findViewById(R.id.img_activity);
        Button btnStartTrcking = findViewById(R.id.btn_start_tracking);
        Button btnStopTracking = findViewById(R.id.btn_stop_tracking);


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        btnStartTrcking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();

            }
        });

        btnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };

        //startTracking();
    }

    private void handleUserActivity(int type, int confidence) {
        String label = getString(R.string.activity_unknown);

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = getString(R.string.activity_in_vehicle);
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = getString(R.string.activity_on_bicycle);
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = getString(R.string.activity_on_foot);
                stop = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Activity done for this much time:"+(stop-start),
                        Toast.LENGTH_SHORT).show();
                foot_time=foot_time+(stop-start);
                start=System.currentTimeMillis();
                break;
            }
            case DetectedActivity.RUNNING: {
                label = getString(R.string.activity_running);
                break;
            }
            case DetectedActivity.STILL: {
                label = getString(R.string.activity_still);
                stop = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Activity done for this much time:"+(stop-start),
                        Toast.LENGTH_SHORT).show();
                still_time=still_time+(stop-start);
                start=System.currentTimeMillis();
                break;
            }
            case DetectedActivity.TILTING: {
                label = getString(R.string.activity_tilting);
                stop = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Activity done for this much time:"+(stop-start),
                        Toast.LENGTH_SHORT).show();
                tilting_time=tilting_time+(stop-start);
                start=System.currentTimeMillis();

                break;
            }
            case DetectedActivity.WALKING: {
                label = getString(R.string.activity_walking);
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = getString(R.string.activity_unknown);
                break;
            }
        }

        Log.e(TAG, "User activity: " + label + ", Confidence: " + confidence);

        if (confidence > Constants.CONFIDENCE) {
            txtActivity.setText(label);
            txtConfidence.setText("Confidence: " + confidence);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void startTracking() {
        Toast.makeText(getApplicationContext(), "Tracking:",
                Toast.LENGTH_SHORT).show();
        start = System.currentTimeMillis();
        current_date = new Date();
        Intent intent = new Intent(Tracking.this, BackgroundDetectedActivitiesService.class);
        startService(intent);
    }

    private void stopTracking() {

        Intent intent = new Intent(Tracking.this, BackgroundDetectedActivitiesService.class);
        stopService(intent);
        String still=String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(still_time),
                TimeUnit.MILLISECONDS.toSeconds(still_time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(still_time)));
        String tilt=String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(tilting_time),
                TimeUnit.MILLISECONDS.toSeconds(tilting_time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tilting_time)));

        Toast.makeText(getApplicationContext(), "Tilting Time:"+tilting_time+" Still time:"+still,
                Toast.LENGTH_SHORT).show();

    }
}