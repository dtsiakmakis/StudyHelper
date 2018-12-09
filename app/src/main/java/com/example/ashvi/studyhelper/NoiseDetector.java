package com.example.ashvi.studyhelper;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class NoiseDetector  {

    Context c;
    public DetectNoise mSensor;
    public static final int POLL_INTERVAL = 300;

    /** running state **/
    public boolean mRunning = false;

    /** config state **/
    public int mThreshold;

    int RECORD_AUDIO = 0;
    public PowerManager.WakeLock mWakeLock;
    public NotificationCompat.Builder notification_builder;
    public NotificationManagerCompat notification_manager;

    public Handler mHandler = new Handler();
    NoiseDetector(Context context)
    {
        c=context;
        mSensor = new DetectNoise();
        PowerManager pm = (PowerManager)c.getSystemService(c.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");
    }
    /* constants */




    /* sound data source */



    /****************** Define runnable thread again and again detect noise *********/

    public Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");
//            start();
        }
    };

    // Create runnable thread to Monitor Voice
    public Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();

            if ((amp > mThreshold)) {
                callForHelp(amp);
                //Log.i("Noise", "==== onCreate ===");
            }
            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    //    @Override
//    public void onResume() {
//        super.onResume();
//        //Log.i("Noise", "==== onResume ===");
//
//
//        if (!mRunning) {
//            mRunning = true;
//            start();
//        }
//    }

    public void start() {
        /*Should be moved to preferences
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ProfileActivity., new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);
        }*/
        Toast.makeText(c, "Started Noise sensing",
                Toast.LENGTH_LONG).show();
        initializeApplicationConstants();
        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }
    public void stop() {
        Log.d("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        //bar.setProgress(0);
        //updateDisplay("stopped...", 0.0);
        mRunning = false;
    }

    public void initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 14;

    }
    /*
    private void updateDisplay(String status, double signalEMA) {
        mStatusView.setText(status);
        bar.setProgress((int)signalEMA);
        Log.d("SOUND", String.valueOf(signalEMA));
        tv_noice.setText(signalEMA+"dB");
    }*/

    public void callForHelp(double signalEMA) {

        //stop();
        // Show alert when noise threshold crossed
        Toast.makeText(c, "Place is too loud. Please move somewhere else to study.",
                Toast.LENGTH_LONG).show();
        Log.d("SOUND", String.valueOf(signalEMA));
        sendNotification();
        //tv_noice.setText(signalEMA+"dB");
    }
    public void sendNotification(){
        Intent open_activity_intent = new Intent(c, ProfileActivity.class);
        PendingIntent pending_intent = PendingIntent
                .getActivity(c, 0, open_activity_intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notification_manager = (NotificationManager) c
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(c, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(c);
        }
        notification_builder.setSmallIcon(R.drawable.blue)
                .setContentTitle("Study Helper")
                .setContentText("Place is too loud. Please move somewhere else to study.")
                .setAutoCancel(true)
                .setContentIntent(pending_intent);
        notification_manager.notify(3000,notification_builder.build());
    }
}