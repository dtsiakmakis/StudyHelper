package com.example.ashvi.studyhelper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddNewEvent extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private static final String TAG = "ADD_NEW_EVENT_DEBUG";

    //public final static int ADD_NEW_CLASS = 1;

    EditText etStartTime, etEndTime, etSubject, etTeacher, etLocation, etDescription, etColor,etDayofMonth;
    //, etFrequency;
    private WeekViewEvent event;
    private ArrayList<String> repeat;
    CheckBox etMon, etTues, etWedn, etThur, etFri, etSat, etSun;

    private boolean updateOperation = false;

    //private static SqlLiteHelper mDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        repeat = new ArrayList<>();
        setContentView(R.layout.add_new_event);
        setToolbar();
        setEditTexts();
        setCheckBox();
        setDefaultInputs();
        checkForUpdateOperation();
        setDBInstance();
        //dontShowKeyboardOnStart();

    }

    private void setCheckBox(){
        etMon = findViewById(R.id.et_Mon);
        etMon.setOnClickListener(this);
        etMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etTues= findViewById(R.id.et_Tues);
        etTues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etWedn= findViewById(R.id.et_Wedn);
        etWedn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etThur= findViewById(R.id.et_Thur);
        etThur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etFri= findViewById(R.id.et_Fri);
        etFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etSat= findViewById(R.id.et_Sat);
        etSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
        etSun= findViewById(R.id.et_Sun);
        etSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    repeat.add(compoundButton.getText().toString().trim());
                    Log.d(TAG,"repeat "+repeat.toString());
                } else {
                    for(int i=0; i <repeat.size();i++){
                        if (repeat.get(i).equals(compoundButton.getText().toString().trim())){
                            repeat.remove(compoundButton.getText().toString().trim());
                        }
                    }
                }
            }
        });
    }

    private void checkForUpdateOperation() {
        String location = getIntent().getStringExtra("Location");
        if (location != null) {
            // so this is to edit existing event.
            updateOperation = true;
            String subject = getIntent().getStringExtra("Subject");
            //String description = getIntent().getStringExtra("Description");
            String color = String.valueOf(getIntent().getIntExtra("Color",0));
            int dayofMonth = getIntent().getIntExtra("DayofMonth", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            int startHour = getIntent().getIntExtra("StartHour", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            int startMinute = getIntent().getIntExtra("StartMinute", Calendar.getInstance().get(Calendar.MINUTE));
            int year = getIntent().getIntExtra("Year", Calendar.getInstance().get(Calendar.MINUTE));
            int month = getIntent().getIntExtra("Month", Calendar.getInstance().get(Calendar.MINUTE));
            int endHour = getIntent().getIntExtra("EndHour", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            int endMinute = getIntent().getIntExtra("EndMinute", Calendar.getInstance().get(Calendar.MINUTE));
            String description = getIntent().getStringExtra("Description");
            long eventID = getIntent().getLongExtra("nextEventID",0);
            if (description==null){
                description="";
            }

            event = new WeekViewEvent(eventID,subject,year,month,dayofMonth,startHour,startMinute,year,month,dayofMonth,endHour,endMinute);
            event.setColor(getIntent().getIntExtra("Color",0));
            event.setLocation(location);




            String startTime = String.format("%02d:%02d",startHour,startMinute);
            String endTime = String.format("%02d:%02d",endHour,endMinute);
            String day  = String.format("%02d/%02d/%d",month,dayofMonth,year);


            Log.d(TAG, "New Input from MainActivity ,editing existing event");
            setEventInput(location,subject,description,color,day,startTime,endTime);
        } else {
            Log.d(TAG,"No input from MainActivity, setting new event");
            event = new WeekViewEvent();
        }
    }

    private void setEventInput(String location, String subject, String description, String color, String day, String startTime, String endTime) {
        etSubject.setText(subject);
        //etTeacher.setText(classData.get(SqlDataEnum.TEACHER));
        etLocation.setText(location);
        etDescription.setText(description);
        etColor.setText(color);
////        etFrequency.setText(classData.get(SqlDataEnum.FREQUENCY));
        etDayofMonth.setText(day);
        etStartTime.setText(startTime);
        etEndTime.setText(endTime);

    }

    private void setEditTexts() {

        etStartTime = findViewById(R.id.et_start_time);
        etStartTime.setOnClickListener(this);
        etEndTime = findViewById(R.id.et_end_time);
        etEndTime.setOnClickListener(this);
        etSubject = findViewById(R.id.et_subject);
        etSubject.setOnClickListener(this);
        //etTeacher = (EditText) findViewById(R.id.et_teacher);
        //etTeacher.setOnClickListener(this);
        etLocation = findViewById(R.id.et_classroom);
        etLocation.setOnClickListener(this);
        etDescription = findViewById(R.id.et_description);
        etDescription.setOnClickListener(this);
        etColor = findViewById(R.id.et_color);
        etColor.setOnClickListener(this);
        etDayofMonth = findViewById(R.id.et_day_of_year);
        etDayofMonth.setOnClickListener(this);
//        etFrequency = (EditText) findViewById(R.id.et_frequency);
//        etFrequency.setOnClickListener(this);
    }

    private void setDBInstance(){
        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void validateStartTimeInput() {
        if (getTimeInMinutesFromTimePicker(etStartTime.getText().toString()) > getTimeInMinutesFromTimePicker(etEndTime.getText().toString())) {
            etEndTime.setText(etStartTime.getText());
        }
    }

    private void validateEndTimeInput() {
        if (getTimeInMinutesFromTimePicker(etEndTime.getText().toString()) < getTimeInMinutesFromTimePicker(etStartTime.getText().toString())) {
            etStartTime.setText(etEndTime.getText());
        }
    }

    private static int getTimeInMinutesFromTimePicker(String time) {
        int colonIndex = time.indexOf(':');
        int hour = Integer.parseInt(time.substring(0, colonIndex));
        int minutes = Integer.parseInt(time.substring(colonIndex+1));
        return hour*60 + minutes;
    }

    private void setDefaultInputs() {
        Log.d(TAG,"Setting default input for a event");
        int startHour = getIntent().getIntExtra("StartHour",0);
        int startMinute = getIntent().getIntExtra("StartMinute",0);
        int month = getIntent().getIntExtra("Month",0);
        int dayofMonth = getIntent().getIntExtra("DayofMonth",12);
        int year = getIntent().getIntExtra("Year",2018);
        etStartTime.setText(String.format("%02d:%02d",startHour,startMinute));
        etEndTime.setText(String.format("%02d:%02d",startHour,startMinute));
        etDayofMonth.setText(String.format("%02d/%02d/%d",month,dayofMonth,year));
    }

    /**
     * TOOLBAR
     */
    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_add_new_class);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.toolbar_add_new_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_add_new_class) {
            Log.d(TAG,"no crush");
            if (validateTimes()) {
                updateDataInDB();
                finish();
                goBackToTimetableActivity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void goBackToTimetableActivity() {
        Log.d(TAG,  "Going back to MainActivity");
        Intent intent = new Intent(this, CalendarActivity.class);
        String uid=currentUser.getUid();

        DatabaseReference cal_ref = mDatabase.child("Calendar_Events");
        DatabaseReference new_cal_ref = cal_ref.push();

        Map<String, Object> event_updates = new HashMap<>();


        event_updates.put("Year",event.getStartTime().get(Calendar.YEAR));


        String eventID = String.format("%d",getIntent().getIntExtra("nextEventID",0));
        Log.d(TAG,"NEW EVENT'S ID IS "+eventID);

        event_updates.put("StartTime",event.getStartTime());
        event_updates.put("EndTime",event.getEndTime());
        event_updates.put("Location",event.getLocation());
        event_updates.put("Subject",event.getName());
        event_updates.put("Frequency",repeat.toString());
        event_updates.put("ID",eventID);
        event_updates.put("Color",event.getColor());

        Log.d(TAG,"Anything wrong before setValue? got event from "+event.getStartTime().getTime().toString()+" to "+ event.getEndTime().getTime().toString());

        //mDatabase.child("User_details").child(uid).child("Calendar_events").setValue(event_updates);

        mDatabase.child("Calendar_Events").child(uid).child(eventID).setValue(event_updates);

        //TODO editting existing events in the database got messed up by: Found a conflicting setters with name: setGregorianChange (conflicts with setGregorianChange defined on java.util.GregorianCalendar)
        // it seems that update data in the database uses different method another than setValue
        Log.d(TAG,"IS HERE EXECUTED?");

        Calendar notifyCalendar = (Calendar)event.getStartTime().clone();
        //notifyCalendar.add(Calendar.MINUTE,-2);
        notifyCalendar.add(Calendar.MINUTE,-5);

        long notifyTime  = notifyCalendar.getTimeInMillis();
        Intent toPending = new Intent(getApplicationContext(), AlarmService.class);
        toPending.putExtra("Frequency",repeat.toString());
        pendingIntent = PendingIntent.getService(getApplicationContext(), 0, toPending, 0);

        Log.d(TAG,String.format("Notify time is "+notifyCalendar.getTime().toString()));
        manager.setRepeating(AlarmManager.RTC, notifyTime,AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d(TAG,String.format("Alarm Setting complete!"));



        startActivity(intent);
    }

    private void addDataFromInputs() {
        //Log.d(TAG,"subject is "+etSubject);
        //Log.d(TAG,"day is "+etDayofMonth);
        event.setName(etSubject.getText().toString().trim());
        event.setId(getIntent().getLongExtra("ID",0));
        //event.setColor(Color.parseColor("#"+etColor.getText().toString().trim()));

        String startTimeString = etStartTime.getText().toString().trim();
        String endTimeString = etEndTime.getText().toString().trim();
        String ymd = etDayofMonth.getText().toString().trim();


        Log.d(TAG,"Starting at "+startTimeString);
        Log.d(TAG,"To "+ endTimeString);
        Log.d(TAG,ymd.substring(3,5));

        String dayOfMonth = ymd.substring(3,5);
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, Integer.parseInt(ymd.substring(ymd.length()-4,ymd.length())));
        startTime.set(Calendar.MONTH, Integer.parseInt(ymd.substring(0,2))-1);
        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonth));
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeString.substring(0,2)));
        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeString.substring(startTimeString.length()-2,startTimeString.length())));
        event.setStartTime((Calendar) startTime.clone());
        //Log.d("ymd new",ymd.substring(3,5));

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR, Integer.parseInt(ymd.substring(ymd.length()-4,ymd.length())));
        endTime.set(Calendar.MONTH, Integer.parseInt(ymd.substring(0,2))-1);
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonth));
        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeString.substring(0,2)));
        endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeString.substring(endTimeString.length()-2,endTimeString.length())));
        event.setEndTime((Calendar) endTime.clone());
        event.setLocation(etLocation.getText().toString().trim());
        event.setName(etSubject.getText().toString().trim());
        event.setId(getIntent().getLongExtra("nextEventID",0));


        // past test, nothing wrong here
        Log.d(TAG,String.format("anything wrong here? event end at "+endTime.getTime().toString()));

        //classData.put(SqlDataEnum.TEACHER, etTeacher.getText().toString().trim());
    }

    private void updateDataInDB() {
        //TODO add instance to database
        // should search the event from the data base, if exist, delete it and then add the new one
        // otherwise just add a new one
        addDataFromInputs();
        //dbref.push().setValue("testing, attention plz");
        //dbref.child("userName").setValue("testUserName");
        //TODO test frequency

    }


    @Override
    public void onClick(View v) {
        if (v.equals(etStartTime) || v.equals(etEndTime)) {
            showTimePicker((EditText) v);
        } else if (v.equals(etColor)) {
            showColorPicker();
        } else if(v.equals(etDayofMonth)){
            showDayPicker((EditText) v);
        }
    }

    private static Map<Integer, Integer> getARGBfromHexNumber(String hexNumber) {
        Log.d("lol5", hexNumber);
        long dec = Long.parseLong(hexNumber, 16);
        Map<Integer, Integer> colors = new HashMap<>();
        colors.put(Color.BLACK, Color.alpha((int) dec));
        colors.put(Color.RED, Color.red((int) dec));
        colors.put(Color.GREEN, Color.green((int) dec));
        colors.put(Color.BLUE, Color.blue((int) dec));
        return colors;
    }

    private void showColorPicker() {
        String colorNow = etColor.getText().toString();
        final ColorPicker colorPicker;
        if (colorNow.equals("")) {
            colorPicker = new ColorPicker(this, 255, 0, 147, 178);
        } else {
            Map<Integer, Integer> decColors = getARGBfromHexNumber(colorNow);
            colorPicker = new ColorPicker(this, decColors.get(Color.BLACK), decColors.get(Color.RED), decColors.get(Color.GREEN), decColors.get(Color.BLUE));
        }

        colorPicker.show();
        colorPicker.setCallback(new ColorPickerCallback() {

            @Override
            public void onColorChosen(@ColorInt int color) {
                //test
                etColor.setText(String.format("%08X", (0xffffffff & color)));
                //etColor.setText(String.format("%08X", (color)));
                event.setColor(color);
            }
        });
    }

    private boolean validateTimes() {
        return !etStartTime.getText().toString().equals(etEndTime.getText().toString());
    }

    private void showTimePicker(final EditText v) {
        String time = v.getText().toString();
        int colonIndex = time.indexOf(':');
        int hour = Integer.parseInt(time.substring(0, colonIndex));
        int minutes = Integer.parseInt(time.substring(colonIndex + 1));

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewEvent.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int pickerMinutes) {
                String pickerMinutersStr = String.valueOf(pickerMinutes);
                if (pickerMinutersStr.length() == 1) {
                    pickerMinutersStr = "0" + pickerMinutersStr;
                }


                v.setText(String.format("%02d",hourOfDay) + ":" + pickerMinutersStr);
                if (v == etStartTime) {
                    validateStartTimeInput();
                } else if (v == etEndTime) {
                    validateEndTimeInput();
                }
            }
        }, hour, minutes, true);
        timePickerDialog.show();
    }

    private void showDayPicker(final EditText v){

    }
}
