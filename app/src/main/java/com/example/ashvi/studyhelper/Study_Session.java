package com.example.ashvi.studyhelper;

public class Study_Session {

    public String StartTime;
    public String StillTime;
    public String EndTime;
    public String hours;
    public String profilePicture;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Study_Session() {
    }

    public Study_Session(String start,String still, String end) {
        this.StartTime = start;
        this.StillTime = still;
        this.EndTime = end;

    }
}
