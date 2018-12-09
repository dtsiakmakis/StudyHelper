package com.example.ashvi.studyhelper;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
/*
public class Test_AutomaticEvent extends AppCompatActivity {
    private PowerManager.WakeLock mWakeLock;
    private NotificationCompat.Builder notification_builder;
    private NotificationManagerCompat notification_manager;
    private  BroadcastReceiver broadcastReceiver;

    private Button test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__automatic_event);

        test=(Button)findViewById(R.id.btnAlarm);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(view);
            }
        });


        /*
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    sendNotification();
                }
            }
        };




    }*/

    public class Test_AutomaticEvent extends AppCompatActivity implements View.OnClickListener {
        private PendingIntent alarmIntent;
        private PowerManager.WakeLock mWakeLock;
        private NotificationCompat.Builder notification_builder;
        private NotificationManagerCompat notification_manager;
        private  BroadcastReceiver broadcastReceiver;

        private Button test;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test__automatic_event);

            // attach the listener to both buttons
            findViewById(R.id.start).setOnClickListener(this);
            findViewById(R.id.stop).setOnClickListener(this);

            // create the launch sender
            Intent launchIntent = new Intent(this, AlarmService.class);
            alarmIntent = PendingIntent.getService(this, 0, launchIntent, 0);
        }

        @Override
        public void onClick(View v) {
            AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            long interval = 60 * 1000; // 1 minute

            switch(v.getId()) {
                case R.id.start:
                    Toast.makeText(this, "Scheduled", Toast.LENGTH_SHORT).show();
                    long time;
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.SECOND, 10);
                    long afterTenSeconds = c.getTimeInMillis();
                    // get a Calendar and set the time to 14:00:00
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, 11);
                    startTime.set(Calendar.MINUTE,33);

// get a Calendar at the current time
                    Calendar now = Calendar.getInstance();

                    if (now.before(startTime)) {
                        // it's not 14:00 yet, start today
                        time = startTime.getTimeInMillis();
                    } else {
                        // start 14:00 tomorrow
                        startTime.add(Calendar.DATE, 1);
                        time = startTime.getTimeInMillis();
                    }

// set the alarm
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        manager.set(AlarmManager.RTC, time, alarmIntent);
                    } else {
                        manager.setExact(AlarmManager.RTC, time, alarmIntent);
                    }

                    //manager.setRepeating(AlarmManager.RTC_WAKEUP, afterTenSeconds, interval, alarmIntent);
                    break;

                case R.id.stop:
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    manager.cancel(alarmIntent);
                    break;
                default:
                    break;
            }
        }
    }

/*
    public void sendNotification(){
        Intent open_activity_intent = new Intent(this, Test_AutomaticEvent.class);
        PendingIntent pending_intent = PendingIntent
                .getActivity(this, 0, open_activity_intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notification_manager = (NotificationManager) this
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
            notification_builder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(this);
        }
        notification_builder.setSmallIcon(R.drawable.evernote)
                .setContentTitle("Study Helper")
                .setContentText("Testing Automatic Event")
                .setAutoCancel(true)
                .setContentIntent(pending_intent);
        notification_manager.notify(3000,notification_builder.build());
    }
*/
