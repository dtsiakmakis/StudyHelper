package com.example.ashvi.studyhelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CalendarActivity  extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    private  ArrayList<WeekViewEvent> eventsFromDB;
    private ArrayList<WeekViewEvent> eventsFromLocal;
    private ArrayList<Map<String,Object>> dataFromDB;


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

    private int nextEventID;

    private static final String TAG ="MAIN_DEBUG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getEventsFromFB();
        getLocalEvents();

        super.onCreate(savedInstanceState);

        //Log.d(TAG,String.format("got %d events from database",eventsFromDB.size()));
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth){
        List<WeekViewEvent> eventsForView = getEvents(newYear,newMonth);
        Log.d(TAG,String.format("OnMonthChange Called,year:%d,month%d got %d events from db and local",newYear,newMonth,eventsForView.size()));
        //example event
//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth - 1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        WeekViewEvent event = new WeekViewEvent(nextEventID, "testEvent", startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_01));
//        eventsForView.add(event);


        return eventsForView;
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
                .putExtra("nextEventID",event.getId());
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
        showEventLongPressAlert(event);
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    private void showEventLongPressAlert(WeekViewEvent event){
        final WeekViewEvent toBeDeleted = event;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteEventFromDB(toBeDeleted.getId());
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

    private void deleteEventFromDB(long eventID){
        //TODO delete event from database
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        showEmptyAlert(time);
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    private void showEmptyAlert(Calendar time){
        final Intent toAddIntent = new Intent(this,AddNewEvent.class);
        Log.d(TAG,String.format("Inside showEmptyAlert NextEventID %d",nextEventID));
        toAddIntent.putExtra("Year", time.get(Calendar.YEAR))
                .putExtra("StartHour",time.get(Calendar.HOUR_OF_DAY))
                .putExtra("StartMinute",time.get(Calendar.MINUTE))
                .putExtra("Month", time.get(Calendar.MONTH)+1)
                .putExtra("DayofMonth",time.get(Calendar.DAY_OF_MONTH))
                .putExtra("nextEventID",nextEventID);
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

        List<WeekViewEvent> updatedEvents = new ArrayList<>();


        for(WeekViewEvent event:eventsFromDB) {
            if (event.getStartTime().get(Calendar.MONTH) == newMonth-1 && event.getStartTime().get(Calendar.YEAR) == newYear) {
                Log.d(TAG,"Inside getEvents: data from db added!");
                updatedEvents.add(event);
            }
        }

        for(WeekViewEvent event:eventsFromLocal) {
            if (event.getStartTime().get(Calendar.MONTH) == newMonth-1 && event.getStartTime().get(Calendar.YEAR) == newYear) {
                updatedEvents.add(event);
            }
        }



        return updatedEvents;
    }

    private  void getEventsFromFB(){
        //TODO get data from database
        String uid = currentUser.getUid();
        eventsFromDB = new ArrayList<>();
        dataFromDB = new ArrayList<>();
        DatabaseReference userCalendar = mDatabase.child("Calendar_Events").child(uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    dataFromDB.addAll((ArrayList<Map<String,Object>>)  dataSnapshot.getValue());
                }
                for(Map<String,Object> uEvent:dataFromDB) {
                    if(uEvent!=null){
                        Log.d(TAG,"Inside onDataChange, got sth from db: "+uEvent.toString());
                        Map startMap = (Map<String, Object>) uEvent.get("StartTime");
                        Map endMap = (Map<String, Object>) uEvent.get("EndTime");
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTimeInMillis(Long.parseLong(String.valueOf(startMap.get("timeInMillis"))));
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTimeInMillis(Long.parseLong(String.valueOf(endMap.get("timeInMillis"))));
                        String freq = (String) uEvent.get("Frequency");



                        WeekViewEvent toAdd = new WeekViewEvent(Integer.parseInt((String) uEvent.get("ID")), (String) uEvent.get("Subject"), (String) uEvent.get("Location"), startTime, endTime);
                        toAdd.setColor(Integer.parseInt(String.valueOf(uEvent.get("Color"))));
                        Log.d(TAG,"Inside function getEventsFromFB: Adding new event from DB to the view," +
                                "it's "+toAdd.getName()+" at "+toAdd.getLocation()+" starts from year " +
                                String.valueOf(toAdd.getStartTime().get(Calendar.YEAR)) +
                                String.valueOf(toAdd.getStartTime().get(Calendar.MONTH)) +
                                String.valueOf(toAdd.getStartTime().get(Calendar.DAY_OF_MONTH)) +
                                " to "+ String.valueOf(toAdd.getStartTime().get(Calendar.YEAR)) +
                                String.valueOf(toAdd.getStartTime().get(Calendar.MONTH)) +
                                String.valueOf(toAdd.getStartTime().get(Calendar.DAY_OF_MONTH)) +
                                "colored with "+ toAdd.getColor()+
                                "repeat on" +freq);
                        if(freq.length()>2){
                            Log.d(TAG,String.format("Repeat test, got freq as "+freq));
                            eventsFromDB.addAll(makeRepeatEvents(freq,toAdd));
                        }
                        eventsFromDB.add(toAdd);

                        nextEventID = Math.max(dataFromDB.size(),(int)toAdd.getId()+1);

                    }



//                    Log.d(TAG,String.format("WORKING? %d",eventsFromDB.size()));
//                    Log.d(TAG,String.format("NextEventID %d",nextEventID));
//                    Log.d(TAG,String.format("WORKING? %s",eventsFromDB.get(0).getLocation()));
                }




                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        userCalendar.addValueEventListener(postListener);

//        new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<Map<String,Object>> value =(ArrayList<Map<String,Object>>)  dataSnapshot.getValue();
//                if (value!=null){
//                    for(Map<String,Object> uEvent:value){
//                        Map startMap =(Map<String,Object>) uEvent.get("StartTime");
//                        Map endMap = (Map<String,Object>)uEvent.get("EndTime");
//                        Calendar startTime = Calendar.getInstance();
//                        startTime.setTimeInMillis(Long.parseLong(String.valueOf(startMap.get("timeInMillis"))));
//                        Calendar endTime = Calendar.getInstance();
//                        endTime.setTimeInMillis(Long.parseLong(String.valueOf(endMap.get("timeInMillis"))));
//
//                        WeekViewEvent toAdd = new WeekViewEvent(Integer.parseInt((String)uEvent.get("ID")),(String)uEvent.get("Subject"),(String)uEvent.get("Location"),startTime,endTime);
//                        //toAdd.setColor(Long.parseLong(String.valueOf(uEvent.get("Color"))));
//                        toAdd.setColor(R.color.event_color_02);
//
//                        Log.d(TAG,"Adding new event from DB to the view," +
//                                "it's "+toAdd.getName()+" at "+toAdd.getLocation()+" starts from " +
//                                toAdd.getStartTime().toString() + " to "+ toAdd.getEndTime().toString() +
//                                "colored with "+ toAdd.getColor());
//                        eventsFromDB.add(toAdd);
//                        Log.d(TAG,String.format("database called, got %d events from DB",eventsFromDB.size()));
//
//
//                    }
//
//                }
//                else{
//                    Log.d(TAG,"None");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//
//
//        }

    }

    private List<WeekViewEvent> makeRepeatEvents(String freq,WeekViewEvent eve){
        List<WeekViewEvent> res = new ArrayList<>();
        Calendar startDate = eve.getStartTime();
        Calendar endTime = eve.getEndTime();
        endTime.add(Calendar.DAY_OF_YEAR,1);
        startDate.add(Calendar.DAY_OF_YEAR,1);
        Calendar endDate = (Calendar)startDate.clone();
        endDate.add(Calendar.MONTH,6);
        Log.d(TAG,"Inside the Freq test, the start date of the repeat is "+startDate.getTime().toString());
        Log.d(TAG,"Inside the Freq test, the end date of the repeat is "+endDate.getTime().toString());

        while(startDate.getTimeInMillis()!=endDate.getTimeInMillis()){
            Calendar tmpStartForEve = (Calendar)startDate.clone();
            Calendar tmpEndForEve = (Calendar)endTime.clone();
           switch(startDate.get(Calendar.DAY_OF_WEEK)){
               case 1: // Sunday
                   if(freq.contains("Sunday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 2: // Monday
                   if(freq.contains("Monday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 3: // Tuesday
                   if(freq.contains("Tuesday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 4: // Wednesday
                   if(freq.contains("Wednesday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 5: // Thursday
                   if(freq.contains("Thursday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 6: // Friday
                   if(freq.contains("Friday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
               case 7: // Saturday
                   if(freq.contains("Saturday")){
                       WeekViewEvent tmp = new WeekViewEvent(eve.getId(),eve.getName(),eve.getLocation(),tmpStartForEve,tmpEndForEve);
                       tmp.setColor(eve.getColor());
                       res.add(tmp);
                   }
                   break;
           }
            startDate.add(Calendar.DAY_OF_YEAR,1);
            endTime.add(Calendar.DAY_OF_YEAR,1);

        }

        for(WeekViewEvent wve:res){
            Log.d(TAG,"Inside function makeRepeatEvents: figuring out whats wrong," +
                    "it's "+wve.getName()+" at "+wve.getLocation()+" starts from year " +
                    String.valueOf(wve.getStartTime().get(Calendar.YEAR)) +
                    String.valueOf(wve.getStartTime().get(Calendar.MONTH)) +
                    String.valueOf(wve.getStartTime().get(Calendar.DAY_OF_MONTH)) +
                    " to "+ String.valueOf(wve.getStartTime().get(Calendar.YEAR)) +
                    String.valueOf(wve.getStartTime().get(Calendar.MONTH)) +
                    String.valueOf(wve.getStartTime().get(Calendar.DAY_OF_MONTH)) +
                    "colored with "+ wve.getColor()+
                    "repeat on" +freq);
        }



        return res;





    }



    private void getLocalEvents(){
        eventsFromLocal = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, callbackID);
        }else{
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2018,0,1,0,0,0);
            long startMillis = beginTime.getTimeInMillis();

            Calendar endTime = Calendar.getInstance();
            endTime.set(2019,12,28,0,0,0);
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

                eventsFromLocal.add(toWeekViewEvent(eventId,beginVal,endVal,title,description,color,location));

            }
            if(!firstFound){
                Toast.makeText(this,"No Event Found From User Calendar.",Toast.LENGTH_SHORT).show();
            }

        }

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
