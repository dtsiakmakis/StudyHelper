package com.example.ashvi.studyhelper;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Stats extends AppCompatActivity {
    private static final String TAG = "StatsActivity" ;
    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public List<String> Userlist;
    private TextView result;
    public ArrayList<Long> start_times = new ArrayList<Long>();
    public ArrayList<Long> stop_times=new ArrayList<Long>();
    public ArrayList<Long> still_times = new ArrayList<Long>();
    public ArrayList<Long> walking_times = new ArrayList<Long>();
    public ArrayList<Map<String,Object>> dataFromDb;
    String res;
    public Study_Session u;
    private BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Setting on Click on the Done button. Here the data entered should be sent to the database.

        //Getting current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        result = (TextView) findViewById(R.id.result);

        //mDatabase.child("Study Sessions").child(uid).setValue(event_updates);
        DatabaseReference study_sessions = mDatabase.child("Study Sessions").child(uid).child("Sessions");
        res="";
        study_sessions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Object temp = singleSnapshot.getValue();
                    HashMap<String,Object> s=(HashMap<String, Object>)temp;


                    Set set = s.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()) {
                        Map.Entry mentry = (Map.Entry)iterator.next();
                        //res=res+""+"key is: "+ mentry.getKey() + " & Value is: "+mentry.getValue();
                        if(mentry.getKey().toString().equalsIgnoreCase("Still Time"))
                        {
                            still_times.add(Long.parseLong(mentry.getValue().toString()));
                        }
                        if(mentry.getKey().toString().equalsIgnoreCase("Start Time"))
                        {
                            start_times.add(Long.parseLong(mentry.getValue().toString()));
                        }
                        if(mentry.getKey().toString().equalsIgnoreCase("Stop Time"))
                        {
                            stop_times.add(Long.parseLong(mentry.getValue().toString()));
                        }
                        if(mentry.getKey().toString().equalsIgnoreCase("Walking"))
                        {
                            walking_times.add(Long.parseLong(mentry.getValue().toString()));
                        }
                        System.out.println(mentry.getValue());
                    }




                }
                result.setText("Still times:"+still_times+"\n"+"Start times:"+start_times+"\n"+"End times:"+stop_times);
                List<String> study_session_names=new ArrayList<>();
                ArrayList<BarEntry> valueSet2 = new ArrayList<>();
                ArrayList<BarEntry> valueSet3 = new ArrayList<>();

                for (int counter = 0; counter < still_times.size(); counter++) {
                    System.out.println(still_times.get(counter));
                    Long start=start_times.get(counter);
                    Timestamp t=new Timestamp(start);
                    DateFormat df = new SimpleDateFormat("dd/MM/yy");

                    //study_session_names.add("Session "+(Integer.toString(counter)));
                    //study_session_names.add(t.toString());
                    study_session_names.add(df.format(start).toString());
                    BarEntry v2e1 = new BarEntry(still_times.get(counter), counter); // Jan
                    valueSet2.add(v2e1);
                    BarEntry w2 = new BarEntry(walking_times.get(counter), counter); // Jan
                    valueSet3.add(w2);

                }

                BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Still");
                BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Walk");

                //barDataSet2.setColors(Color.rgb(0, 155, 0));
                barDataSet2.setColor(Color.rgb(0, 155, 0));
                ArrayList<BarDataSet> dataSets = null;
                dataSets = new ArrayList<>();
                //dataSets.add(barDataSet1);
                dataSets.add(barDataSet2);
                dataSets.add(barDataSet3);
                chart=(BarChart) findViewById(R.id.chart);
                BarData data = new BarData(study_session_names, dataSets);
                //result.setText(dataSets.toString());
                chart.setData(data);
                chart.setDescription("My Activity Chart");
                chart.animateXY(2000, 2000);
                chart.invalidate();
                Toast.makeText(Activity_Stats.this, "Times:"+study_session_names.toString(),
                        Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }




}


