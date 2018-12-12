package com.example.ashvi.studyhelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView Email;
    private TextView Uid;
    private TextView user_name;
    private ImageButton logout;
    private ImageButton preferences;
    private ImageButton resources;
    private Button noiseDetect;
    private Button studyLocations;
    private ImageButton userCalendar;
    private ImageButton dash;
    private Button detectActivity;
    private Button testEvents;
    private Button study_log;
    int RECORD_AUDIO = 0;

    private ImageView profilePicture;
    private Bitmap profilePic;
    private String profilePicString;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private User u;
    private final int REQ_PERMISSION = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.button_logout);
        preferences = findViewById(R.id.preferences);
        resources = findViewById(R.id.resource);
        dash = findViewById(R.id.dashboard);
//        testEvents=(Button) findViewById(R.id.test_events);
        study_log=(Button) findViewById(R.id.log_studying);


        user_name = (TextView)findViewById(R.id.user_name);
        user = mAuth.getCurrentUser();
        profilePicture = findViewById(R.id.profile_image);
        profilePic = profilePicture.getDrawingCache();
//        noiseDetect = findViewById(R.id.noiseDetect);
        userCalendar = findViewById(R.id.calendar);

        askPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);
        }



        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("User_details");
        final FirebaseUser reqd_User;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("User_details");

        Query nameQuery = ref.orderByChild("id").equalTo(user.getUid());
        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            //System.out.print("I am inside event");

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    u = singleSnapshot.getValue(User.class);

                    String u_name = u.name;
                    String u_id = u.id;
                    String major = u.major;
                    profilePicString = u.profilePicture;
                    if (profilePicString != null){
                        byte[] decodedString = Base64.decode(profilePicString, Base64.DEFAULT);
                        profilePic = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profilePicture.setImageBitmap(profilePic);
                    }
                    user_name.setText("Hello " + u_name + "!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Cancelled sorry",
                        Toast.LENGTH_SHORT).show();
            }
        });

        study_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==study_log){

                    startActivity(new Intent(getApplicationContext(), AreYouStudying.class));

                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==logout){
                    if (user != null) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }
        });

//        testEvents.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v==testEvents){
//
//                        startActivity(new Intent(getApplicationContext(), Test_AutomaticEvent.class));
//
//                }
//            }
//        });
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==dash){

                        startActivity(new Intent(getApplicationContext(), Dashboard.class));

                }
            }
        });

        userCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==userCalendar){


                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                }
            }
        });


        if (user != null){
            String email = user.getEmail();
            String uid = user.getUid();
//            Email.setText(email);
//            Uid.setText(uid);




        }


        //Setting onClick listener for the preferences button

        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == preferences){
                    if (user != null) {
                        //Testing with MainActivity Class
                        startActivity(new Intent(getApplicationContext(), StudentPreferences.class));
                    }
                }
            }
        });
        resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == resources){
                    if (user != null) {
                        //Testing with MainActivity Class
                        startActivity(new Intent(getApplicationContext(), Resources.class));
                    }
                }
            }
        });
    }
    private void askPermission() {
        Log.d("tag", "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }
}