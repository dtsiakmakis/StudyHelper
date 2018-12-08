package com.example.ashvi.studyhelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;






public class CalendarActivity  extends BaseActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbref;
    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID, // 0
            CalendarContract.Instances.BEGIN, // 1
            CalendarContract.Instances.END, //2
            CalendarContract.Instances.TITLE, // 3
            CalendarContract.Instances.DESCRIPTION, //4
            CalendarContract.Instances.EVENT_LOCATION,//5
            CalendarContract.Instances.CALENDAR_COLOR, //6
            CalendarContract.Instances.TITLE //7
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_END_INDEX = 2;
    private static final int PROJECTION_TITLE_INDEX = 3;
    private static final int PROJECTION_DESCRIPTION_INDEX = 4;
    private static final int PROJECTION_EVENT_LOCATION = 5;
    private static final int PROJECTION_CALENDAR_COLOR = 6;
    final int callbackID = 42;

    List<WeekViewEvent> events = new ArrayList<>();;

    private int callingTime =1;

    private int nextEventID;

    private static final String TAG ="MAIN_DEBUG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbdb= FirebaseDatabase.getInstance();
        dbref = fbdb.getReference();
        if(getIntent().getBooleanExtra("isNewEvent", false)){
            Log.d("Main","Not None!");
            WeekViewEvent newEvent = makeEventFromIntent();
            Log.d("newEvent From Intent",newEvent.getName());
            Log.d("new Event month",String.format("%d",newEvent.getStartTime().get(Calendar.MONTH)));
            events.add(newEvent);
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth){
        //why is this called 3 times?

        //to log how many time this function is called.
        // so this is called for this month, last month, and next month,
        // and then return them to the view
        // So here, we check the events from db and local, to see if them in those month and them just return them to the view

        //Log.d(TAG,String.format("This function is being called for the %d time",callingTime++));

        // so we need to clear the old events and then add the ones with the correct mm/yy to the view
        List<WeekViewEvent> eventsForView = getEvents(newYear,newMonth);

        //example event
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(nextEventID, "testEvent", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        eventsForView.add(event);


        //events.addAll(getLocalEvents(newYear,newMonth));


        nextEventID += eventsForView.size();
        return eventsForView;
    }

    private WeekViewEvent makeEventFromIntent(){
        Intent intent = getIntent();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("StartHour",0));
        startTime.set(Calendar.MINUTE, intent.getIntExtra("StartMinute",0));
        startTime.set(Calendar.MONTH, intent.getIntExtra("Month",12));
        //startTime.set(Calendar.YEAR, intent.getIntExtra("Year",2018));
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("EndHour",0));
        endTime.set(Calendar.MONTH, intent.getIntExtra("EndMinute",0));
        endTime.set(Calendar.MONTH, intent.getIntExtra("Month",12));
        //endTime.set(Calendar.YEAR, intent.getIntExtra("Year",2018));

        String frequency = intent.getStringExtra("Frequency");
        Log.d(TAG,"repeat"+frequency);
        if (frequency.contains("Monday")){
            //startTime.set(Calendar.DAY_OF_WEEK,1);
            //endTime.set(Calendar.DAY_OF_WEEK,1);
        }

        WeekViewEvent event = new WeekViewEvent(1, intent.getStringExtra("Subject"),intent.getStringExtra("Location"), startTime, endTime);
        event.setColor(intent.getIntExtra("Color",intent.getIntExtra("Color",R.color.event_color_01)));


        return event;
    }


//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        if (requestCode == callbackID) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted
//            } else {
//                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect){
        showEventClickAlert(event);
        Toast.makeText(this, "Clicked event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showEventClickAlert(WeekViewEvent event){
        final Intent toEditIntent = new Intent(this,AddNewEvent.class);
        toEditIntent.putExtra("Year", event.getStartTime().get(Calendar.YEAR))
                .putExtra("StartHour",event.getStartTime().get(Calendar.HOUR_OF_DAY))
                .putExtra("StartMinute",event.getStartTime().get(Calendar.MINUTE))
                .putExtra("Month", event.getStartTime().get(Calendar.MONTH)+1)
                .putExtra("DayofMonth",event.getStartTime().get(Calendar.DAY_OF_MONTH))
                .putExtra("Location",event.getLocation())
                .putExtra("Subject",event.getName())
                .putExtra("EndHour",event.getEndTime().get(Calendar.HOUR_OF_DAY))
                .putExtra("EndMinute",event.getEndTime().get(Calendar.MINUTE))
                .putExtra("USERNAME","testUserName")
                .putExtra("nextEventID",nextEventID);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        startActivity(toEditIntent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You want to edit this event?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        //TODO delete the event when long press
        showEventLongPressAlear(event);
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showEventLongPressAlear(WeekViewEvent event){
        final WeekViewEvent toBeDeleted = event;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteEventFromDB(toBeDeleted);
                        finish();
                        Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete the event from your calendar?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void deleteEventFromDB(WeekViewEvent event){
        //TODO delete event from database
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        showEmptyAlert(time);
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    private void showEmptyAlert(Calendar time){
        final Intent toAddIntent = new Intent(this,AddNewEvent.class);
        toAddIntent.putExtra("Year", time.get(Calendar.YEAR))
                .putExtra("StartHour",time.get(Calendar.HOUR_OF_DAY))
                .putExtra("StartMinute",time.get(Calendar.MINUTE))
                .putExtra("Month", time.get(Calendar.MONTH)+1)
                .putExtra("DayofMonth",time.get(Calendar.DAY_OF_MONTH))
                .putExtra("USERNAME","testUserName")
                .putExtra("neexEventID",nextEventID);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        startActivity(toAddIntent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getEmptyLongPressAlertText(time)).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private String getEmptyLongPressAlertText(Calendar time){
        String out ="Do you want to add new event begins at ";
        out+= String.format("%02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
        out+="?\n";
        return out;
    }


    private List<WeekViewEvent> getEvents(int newYear, int newMonth){
        List<WeekViewEvent> updatedEvents = new ArrayList<WeekViewEvent>();
        updatedEvents.addAll(getLocalEvents(newYear,newMonth));
        //updatedEvents.addAll(getEventsFromFB(newYear,newMonth));
        for(WeekViewEvent event:events) {
            if (event.getStartTime().get(Calendar.MONTH) == newMonth && event.getStartTime().get(Calendar.YEAR) == newYear) {
                updatedEvents.add(event);
            }
        }
        return updatedEvents;
    }

    private  List<WeekViewEvent> getEventsFromFB(int newYear, int newMonth){
        //TODO get data from database

        return new ArrayList<WeekViewEvent>();
    }


    private List<WeekViewEvent> getLocalEvents(int newYear, int newMonth){
        List<WeekViewEvent> localEvents = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, callbackID);
        }else{
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(newYear,newMonth,1,0,0,0);
            long startMillis = beginTime.getTimeInMillis();

            Calendar endTime = Calendar.getInstance();
            endTime.set(newYear,newMonth+1,1,0,0,0);
            long endMillis = endTime.getTimeInMillis(); //now + 12 hours. Fixing the end time to something


            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startMillis);
            ContentUris.appendId(builder, endMillis);
            String selection = CalendarContract.Instances.EVENT_ID + " > ?";
            String[] selectionArgs = new String[] {"0"};
            // Submit the query and get a Cursor object back.
            cur = cr.query(builder.build(),
                    INSTANCE_PROJECTION, selection,
                    selectionArgs, "startDay ASC, startMinute ASC");


            boolean firstFound = false;
            while(cur.moveToNext()){
                long eventId = cur.getLong(PROJECTION_ID_INDEX);
                long beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
                long endVal = cur.getLong(PROJECTION_END_INDEX);
                String title = cur.getString(PROJECTION_TITLE_INDEX);
                String description = cur.getString(PROJECTION_DESCRIPTION_INDEX);
                String location = cur.getString(PROJECTION_EVENT_LOCATION);
                String color = cur.getString(PROJECTION_CALENDAR_COLOR);

                firstFound = true;

                localEvents.add(toWeekViewEvent(eventId,beginVal,endVal,title,description,color,location));

            }
            if(!firstFound){
                Toast.makeText(this,"No Event Found From User Calendar.",Toast.LENGTH_SHORT).show();
            }

        }

        return localEvents;

    }

    private WeekViewEvent toWeekViewEvent(long id,long begin,long end, String title,String description,String color,String location){
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(begin);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(end);
        WeekViewEvent event = new WeekViewEvent(id,title,location,startTime,endTime);
        event.setColor(Integer.parseInt(color));

        return event;

    }





}
