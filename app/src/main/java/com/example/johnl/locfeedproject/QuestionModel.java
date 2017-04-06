package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */


public class QuestionModel {

    String question;
    String user;
    String userRep;

    public QuestionModel(String question, String user, String userRep ) {
        this.question=question;
        this.user=user;
        this.userRep=userRep;
    }

    public String getQuestion() {
        return question;
    }

    public String getUser() {
        return user;
    }

    public String getUserRep() { return userRep; }

}