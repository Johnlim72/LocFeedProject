package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */


public class CommentModel {

    String comment;
    String user;
    String userRep;

    public CommentModel(String comment, String user, String userRep ) {
        this.comment=comment;
        this.user=user;
        this.userRep=userRep;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public String getUserRep() { return userRep; }

}