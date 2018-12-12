package com.example.ashvi.studyhelper;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmService extends Service {

    private NotificationCompat.Builder notification_builder;
    private NotificationManagerCompat notification_manager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // display the current time
        Calendar now = Calendar.getInstance();
        DateFormat formatter = SimpleDateFormat.getTimeInstance();
        Toast.makeText(this, formatter.format(now.getTime()), Toast.LENGTH_SHORT).show();
        sendNotification();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendNotification(){
        Intent open_activity_intent = new Intent(this, AreYouStudying.class);
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
                .setContentText("Its time to study!!!")
                .setAutoCancel(true)
                .setContentIntent(pending_intent);
        notification_manager.notify(3000,notification_builder.build());
    }
}