package com.example.ashvi.studyhelper;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.location.DetectedActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.example.ashvi.studyhelper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityDetector {

    public long start,stop=0;
    public Date current_date;
    public long tilting_time=0;
    public long still_time=0;
    public long foot_time=0;
    public BroadcastReceiver broadcastReceiver;
    public Context context;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public DatabaseReference mDatabase;
    Map<String, Object> event_updates;
    public String uid;

    ActivityDetector(Context c){
        context=c;
        event_updates = new HashMap<>();
    }


    public void initialize() {


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

    }

    private void handleUserActivity(int type, int confidence) {
        String label =  context.getString(R.string.activity_unknown);
        Toast.makeText(context, "Inside Handle User Activity:"+type,
                Toast.LENGTH_SHORT).show();

            switch (type) {
                case DetectedActivity.IN_VEHICLE: {
                    label = context.getString(R.string.activity_in_vehicle);
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    label = context.getString(R.string.activity_on_bicycle);
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    label = context.getString(R.string.activity_on_foot);
                    stop = System.currentTimeMillis();
                    Toast.makeText(context, "Activity done for this much time:" + (stop - start),
                            Toast.LENGTH_SHORT).show();
                    foot_time = foot_time + (stop - start);
                    start = System.currentTimeMillis();
                    break;
                }
                case DetectedActivity.RUNNING: {
                    label = context.getString(R.string.activity_running);
                    break;
                }
                case DetectedActivity.STILL: {
                    label = context.getString(R.string.activity_still);
                    stop = System.currentTimeMillis();
                    Toast.makeText(context, "Activity done for this much time:" + (stop - start),
                            Toast.LENGTH_SHORT).show();
                    still_time = still_time + (stop - start);
                    start = System.currentTimeMillis();
                    break;
                }
                case DetectedActivity.TILTING: {
                    label = context.getString(R.string.activity_tilting);
                    stop = System.currentTimeMillis();
                    Toast.makeText(context, "Activity done for this much time:" + (stop - start),
                            Toast.LENGTH_SHORT).show();
                    tilting_time = tilting_time + (stop - start);
                    start = System.currentTimeMillis();

                    break;
                }
                case DetectedActivity.WALKING: {
                    label = context.getString(R.string.activity_walking);
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    label = context.getString(R.string.activity_unknown);
                    break;
                }

        }

        //Log.e(TAG, "User activity: " + label + ", Confidence: " + confidence);

        //if (confidence > Constants.CONFIDENCE) {
         //   txtActivity.setText(label);
         //   txtConfidence.setText("Confidence: " + confidence);
        //}
    }

    public void startTracking() {

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
        start=System.currentTimeMillis();

        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));



        //DatabaseReference cal_ref = mDatabase.child("Study Sessions");
        //DatabaseReference new_cal_ref = cal_ref.push();



        Map mMap = new HashMap();
        List<Map> list = new ArrayList();
        event_updates.put("Start Time",System.currentTimeMillis());
        list.add(event_updates);

        //event_updates.put("Year",event.getStartTime().get(Calendar.YEAR));

        Toast.makeText(context, "Tracking:",
                Toast.LENGTH_SHORT).show();
        start = System.currentTimeMillis();
        current_date = new Date();
        Intent intent = new Intent(context, BackgroundDetectedActivitiesService.class);
        context.startService(intent);
    }

    public void stopTracking() {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        //Setting on Click on the Done button. Here the data entered should be sent to the database.

        //Getting current user
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        uid=currentUser.getUid();

        event_updates.put("Stop Time",System.currentTimeMillis());
        event_updates.put("Still Time",still_time);

        //mDatabase.child("Study Sessions").child(uid).setValue(event_updates);
        DatabaseReference study_sessions=mDatabase.child("Study Sessions").child(uid);


        DatabaseReference newRequestRef = study_sessions.child("Sessions").push();
        newRequestRef.setValue(event_updates);

        Intent intent = new Intent(context, BackgroundDetectedActivitiesService.class);
        context.stopService(intent);
        String still=String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(still_time),
                TimeUnit.MILLISECONDS.toSeconds(still_time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(still_time)));
        String tilt=String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(tilting_time),
                TimeUnit.MILLISECONDS.toSeconds(tilting_time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tilting_time)));

        Toast.makeText(context, "Tilting Time:"+tilting_time+" Still time:"+still,
                Toast.LENGTH_SHORT).show();

    }


}
