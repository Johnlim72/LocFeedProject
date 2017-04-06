package com.example.johnl.locfeedproject;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EventFeedActivity extends AppCompatActivity {


    ArrayList<EventModel> eventModels;
    ListView listView;
    private static EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.list);

        eventModels = new ArrayList<>();
        eventModels.add(new EventModel("Event", "TuDev event", "Monil", "40", "5:00-", "7:00"));
        eventModels.add(new EventModel("Event2", "TuDev event2", "John", "32", "7:00-", "9:00"));
        eventModels.add(new EventModel("party!!", "Party", "Jae", "90", "6:00-", "9:00"));
        eventModels.add(new EventModel("party2", "TuDev event", "Hai", "72", "5:00 -", "7:00"));
        eventModels.add(new EventModel("Event3", "TuDev event", "Frank","100","5:00 -", "7:00"));
        eventModels.add(new EventModel("Event4", "TuDev event", "Lenny","241","1:00-", "4:00"));
        eventModels.add(new EventModel("Event5", "TuDev event", "George","1242", "3:00-", "5:00"));
        eventModels.add(new EventModel("Event11", "TuDev event", "Peter","102", "5:00 -", "7:00"));
        eventModels.add(new EventModel("Event125", "TuDev event", "Shahram","56", "2:00 -", "6:00"));
        eventModels.add(new EventModel("Event454", "TuDev event", "Noah","345", "9:00 -", "12:00"));
        eventModels.add(new EventModel("Event34", "TuDev event", "Monil","34", "5:00 -", "7:00"));

        adapter= new EventAdapter(eventModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventModel eventModel = eventModels.get(position);

                Snackbar.make(view, eventModel.getName()+"\n"+ eventModel.getDescription()+" User: "+ eventModel.getUser(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
