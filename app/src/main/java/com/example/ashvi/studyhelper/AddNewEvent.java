package com.example.ashvi.studyhelper;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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

    private static final String TAG = "ADD_NEW_EVENT_DEBUG";

    //public final static int ADD_NEW_CLASS = 1;

    EditText etStartTime, etEndTime, etSubject, etTeacher, etLocation, etDescription, etColor,etDayofMonth;
    //, etFrequency;
    private WeekViewEvent event;
    private ArrayList<String> repeat;
    CheckBox etMon, etTues, etWedn, etThur, etFri, etSat, etSun;

   // private HashMap<SqlDataEnum, String> classData;
    private boolean updateOperation = false;

    //private static SqlLiteHelper mDB;
    FirebaseDatabase fbdb;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            String userName = getIntent().getStringExtra("USERNAME");
            String description = getIntent().getStringExtra("Description");
            int nexEventID =getIntent().getIntExtra("nextEventID", Calendar.getInstance().get(Calendar.MINUTE));
            if (description==null){
                description="";
            }

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

        etStartTime = (EditText) findViewById(R.id.et_start_time);
        etStartTime.setOnClickListener(this);
        etEndTime = (EditText) findViewById(R.id.et_end_time);
        etEndTime.setOnClickListener(this);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etSubject.setOnClickListener(this);
        //etTeacher = (EditText) findViewById(R.id.et_teacher);
        //etTeacher.setOnClickListener(this);
        etLocation = (EditText) findViewById(R.id.et_classroom);
        etLocation.setOnClickListener(this);
        etDescription = (EditText) findViewById(R.id.et_description);
        etDescription.setOnClickListener(this);
        etColor = (EditText) findViewById(R.id.et_color);
        etColor.setOnClickListener(this);
        etDayofMonth =(EditText) findViewById(R.id.et_day_of_year);
        etDayofMonth.setOnClickListener(this);
//        etFrequency = (EditText) findViewById(R.id.et_frequency);
//        etFrequency.setOnClickListener(this);
    }

    private void setDBInstance(){
         fbdb= FirebaseDatabase.getInstance();
         dbref = fbdb.getReference();
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
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_add_new_class);
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

        mDatabase= FirebaseDatabase.getInstance().getReference();
        //Setting on Click on the Done button. Here the data entered should be sent to the database.

        //Getting current user
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        String uid=currentUser.getUid();

        DatabaseReference cal_ref = mDatabase.child("Calendar_Events");
        DatabaseReference new_cal_ref = cal_ref.push();

        Map<String, Object> event_updates = new HashMap<>();


        event_updates.put("Year",event.getStartTime().get(Calendar.YEAR));




        event_updates.put("StartHour",event.getStartTime().get(Calendar.HOUR_OF_DAY));
        event_updates.put("StartMinute",event.getStartTime().get(Calendar.MINUTE));
        event_updates.put("Month", event.getStartTime().get(Calendar.MONTH));
        event_updates.put("DayofMonth",event.getStartTime().get(Calendar.DAY_OF_MONTH));
        event_updates.put("EndHour",event.getEndTime().get(Calendar.HOUR_OF_DAY));
        event_updates.put("EndMinute",event.getEndTime().get(Calendar.MINUTE));
        event_updates.put("Location",event.getLocation());
        event_updates.put("Subject",event.getName());
        event_updates.put("Frequency",repeat.toString());




        //mDatabase.child("User_details").child(uid).child("Calendar_events").setValue(event_updates);

        mDatabase.child("Calendar_Events").child(uid).child("Events").setValue(event_updates);


        intent.putExtra("Year", event.getStartTime().get(Calendar.YEAR))
                .putExtra("StartHour",event.getStartTime().get(Calendar.HOUR_OF_DAY))
                .putExtra("StartMinute",event.getStartTime().get(Calendar.MINUTE))
                .putExtra("Month", event.getStartTime().get(Calendar.MONTH))
                .putExtra("DayofMonth",event.getStartTime().get(Calendar.DAY_OF_MONTH))
                .putExtra("EndHour",event.getEndTime().get(Calendar.HOUR_OF_DAY))
                .putExtra("EndMinute",event.getEndTime().get(Calendar.MINUTE))
                .putExtra("Location",event.getLocation())
                .putExtra("Subject",event.getName())
                .putExtra("Color",event.getColor())
                .putExtra("isNewEvent",true)
                .putExtra("Frequency",repeat.toString());


//        String returnedToMain = String.format("The event sent to mainActivity is on %02d/%02d/%02d,  from %02d:%02d %s to %02d:%02d %s, located at %s, subject is %s"
//                ,event.getStartTime().get(Calendar.MONTH)+1
//                ,event.getStartTime().get(Calendar.DAY_OF_MONTH)
//                , event.getStartTime().get(Calendar.YEAR)
//                ,event.getStartTime().get(Calendar.HOUR_OF_DAY)
//                ,event.getStartTime().get(Calendar.MINUTE)
//                ,event.getStartTime().get(Calendar.AM_PM)
//                ,event.getEndTime().get(Calendar.HOUR_OF_DAY)
//                ,event.getEndTime().get(Calendar.MINUTE)
//                ,event.getEndTime().get(Calendar.AM_PM)
//                ,event.getLocation()
//                ,event.getName()
//        );
//
//        Log.d("Adding new Event",returnedToMain);

        startActivity(intent);
    }

    private void addDataFromInputs() {
        //Log.d(TAG,"subject is "+etSubject);
        //Log.d(TAG,"day is "+etDayofMonth);
        event.setName(etSubject.getText().toString().trim());
        //event.setColor(Color.parseColor("#"+etColor.getText().toString().trim()));

        String startTimeString = etStartTime.getText().toString().trim();
        String endTimeString = etEndTime.getText().toString().trim();
        String ymd = etDayofMonth.getText().toString().trim();


        //Log.d("SettingEventStartTime",startTimeString);
        //Log.d("SettingEventEndTime",endTimeString);
        //Log.d("mm",ymd.substring(0,2));
        //Log.d("dd",ymd.substring(3,5));

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
        if (etStartTime.getText().toString().equals(etEndTime.getText().toString())) {
            return false;
        }
        return true;
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
