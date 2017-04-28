package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

//the object to be populated into the ListView by CommentAdapter
public class CommentModel {

    String comment;
    String user;
    String userRep;
    String userID;

    public CommentModel(String comment, String user, String userRep, String userID  ) {
        this.comment=comment;
        this.user=user;
        this.userRep=userRep;
        this.userID=userID;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public String getUserRep() { return userRep; }

    public String getUserID() { return userID; }

}