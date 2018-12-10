package com.example.ashvi.studyhelper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AreYouStudying extends AppCompatActivity {
    ActivityDetector activityDetector;
    NoiseDetector noiseDetector;
    private Button start_stud,stop_stud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are_you_studying);
        Button stop;
        stop=(Button)findViewById(R.id.stop_loc);
        activityDetector= new ActivityDetector(getApplicationContext());
        noiseDetector = new NoiseDetector(getApplicationContext());
        start_stud=(Button)findViewById(R.id.start_loc);
        stop_stud=(Button)findViewById(R.id.stop_loc);


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Toast.makeText(getApplicationContext(), "Yes, Let us start tracking", Toast.LENGTH_LONG).show();
                        activityDetector.initialize();
                        activityDetector.startTracking();
                        //noiseDetector.start();
                        //Call all the tracking
                        //Activity_Tracking
                        //App_Usage Tracking
                        //NoiseTracking
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Stop tracking
                        Toast.makeText(getApplicationContext(), "NOOOOOOOOOOOOOOOOOOOOOOO", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };



        start_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDetector.startTracking();
                noiseDetector.start();

            }
        });
        stop_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDetector.stopTracking();
                noiseDetector.stop();

            }
        });


        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setMessage("Are you studying?").setPositiveButton("Yes", dialogClickListener)
         //       .setNegativeButton("No", dialogClickListener).show();

       // AlertDialog alert = builder.create();
        //alert.show();
    }

    public void start_locater()
    {
        activityDetector.startTracking();
    }
}
