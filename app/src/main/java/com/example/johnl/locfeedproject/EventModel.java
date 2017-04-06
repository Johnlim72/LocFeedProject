package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

public class EventModel {

    String name;
    String description;
    String user;
    String userRep;
    String timeStart;
    String timeEnd;


    public EventModel(String name, String description, String user, String userRep, String timeStart, String timeEnd ) {
        this.name=name;
        this.description=description;
        this.user=user;
        this.userRep=userRep;
        this.timeStart=timeStart;
        this.timeEnd=timeEnd;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public String getUserRep() { return userRep; }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

}

