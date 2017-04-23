package com.example.johnl.locfeedproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChooseActivity extends AppCompatActivity {

    private String location_id, id;

    //sets the view on the screen for the "activity_choose" layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location_id = extras.getString("LocationID").toString();
            id = extras.getString("id").toString();
        } else {
            location_id = "2";
            id = "0";
        }

        System.out.println("++====++++++++ ID in ChooseActivity: " + id);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);
    }

    //Go to EventFeedActivity when user clicks on 'EVENTS' button on screen
    public void onEventClick(View view) {
        Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //Go back to Map Activity when back button is pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //Go to CommentFeedActivity when user clicks on 'COMMENTS' button on screen
    public void onCommentClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CommentFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //Go back to MapActivity when user clicks 'CHOOSE LOCATION' button on screen
    public void onChooseLocationClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra("id", id);

        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}