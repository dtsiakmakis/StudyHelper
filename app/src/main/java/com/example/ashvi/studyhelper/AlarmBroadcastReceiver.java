package com.example.ashvi.studyhelper;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "en.proft.alarms.ACTION_ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_ALARM.equals(intent.getAction())) {
            String freq = intent.getStringExtra("Frequency");
            Calendar current = Calendar.getInstance();
            switch (current.get(Calendar.DAY_OF_WEEK)){
                case 1: // Sunday
                    if(freq.contains("Sunday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 2: // Monday
                    if(freq.contains("Monday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 3: // Tuesday
                    if(freq.contains("Tuesday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 4: // Wednesday
                    if(freq.contains("Wednesday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 5: // Thursday
                    if(freq.contains("Thursday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 6: // Friday
                    if(freq.contains("Friday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 7: // Saturday
                    if(freq.contains("Saturday")){
                        Toast.makeText(context, ACTION_ALARM, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


}