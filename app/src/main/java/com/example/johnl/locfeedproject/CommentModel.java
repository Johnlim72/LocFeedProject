package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

//the object to be populated into the ListView by CommentAdapter
public class CommentModel {

    String comment;
    String user;
    String userRep;
    String commenter_id;

    public CommentModel(String comment, String user, String userRep, String commenter_id  ) {
        this.comment=comment;
        this.user=user;
        this.userRep=userRep;
        this.commenter_id=commenter_id;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public String getUserRep() { return userRep; }

    public String getCommenterID() { return commenter_id; }

}