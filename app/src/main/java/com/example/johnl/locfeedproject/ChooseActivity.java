package com.example.johnl.locfeedproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChooseActivity extends AppCompatActivity {

    String location_id, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        location_id = "2";
        id = "0";
        if(extras != null){
            location_id = extras.getString("LocationID");
            id = extras.getString("id");
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);
    }

    public void onEventClick(View view){
        Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void onQuestionClick(View view){
        Intent intent = new Intent(getApplicationContext(), QuestionFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void onChooseLocationClick(View view){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}