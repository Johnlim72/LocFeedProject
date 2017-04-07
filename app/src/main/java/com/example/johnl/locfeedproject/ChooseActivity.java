package com.example.johnl.locfeedproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);
    }

    public void onEventClick(View view){
        Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onQuestionClick(View view){
        Intent intent = new Intent(getApplicationContext(), QuestionFeedActivity.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}