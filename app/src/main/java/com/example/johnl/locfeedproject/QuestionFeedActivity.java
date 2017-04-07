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

public class QuestionFeedActivity extends AppCompatActivity {


    ArrayList<QuestionModel> questionModels;
    ListView listView;
    private static QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_feed);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.question_list);

        questionModels = new ArrayList<>();
        questionModels.add(new QuestionModel("Where is the event?", "Monil", "4.3"));
        questionModels.add(new QuestionModel("Where is the event?", "Monil", "4.3"));
        questionModels.add(new QuestionModel("Where is the event?", "Monil", "4.3"));
        questionModels.add(new QuestionModel("Where is the event?", "Monil", "4.3"));
        questionModels.add(new QuestionModel("Where is the event?", "Monil", "4.3"));



        adapter= new QuestionAdapter(questionModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionModel questionModel = questionModels.get(position);

                Snackbar.make(view, questionModel.getQuestion()+"\n"+" User: "+ questionModel.getUser(), Snackbar.LENGTH_LONG)
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