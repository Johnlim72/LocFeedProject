package com.example.johnl.locfeedproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class EventFeedActivity extends AppCompatActivity {

    ArrayList<EventModel> eventModels;
    private ListView listView;
    private EventAdapter adapter;

    ProgressDialog progressDialog;

    private String location_id, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            location_id = extras.getString("LocationID").toString();
            id = extras.getString("id").toString();
        } else {
            location_id = "2";
            id = "0";
        }

        listView = (ListView)findViewById(R.id.event_list);

        eventModels = new ArrayList<>();

        adapter = new EventAdapter(eventModels, getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Events");
        progressDialog.show();

        new GetEvents().execute();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void OnRefresh(View view) {
        Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);
    }

    private class GetEvents extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/get_events.php";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                String data = URLEncoder.encode("location_id", "UTF-8") + "=" +
                        URLEncoder.encode(location_id, "UTF-8");

                wr.write(data);
                wr.flush();

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    try{
                        while((line = reader.readLine()) != null){
                            System.out.println("Line = " + line);
                            sb.append(line).append('\n');
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    String jsonString = sb.toString();

                    Log.e("JSON String Tag", "Response from URL: " + jsonString);

                    System.out.println("Before jsonString != null");

                    if (jsonString != null) {
                        if(jsonString.equals("No results")){
                            Toast.makeText(getApplicationContext(), "No Events!", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String success = jsonObject.getString("success");

                                if(success.equals("1")){
                                    JSONArray events = jsonObject.getJSONArray("events");

                                    for (int i = 0; i < events.length(); i++) {
                                        JSONObject event = events.getJSONObject(i);
                                        String event_header = event.getString("event_header");
                                        String event_description = event.getString("event_description");
                                        String event_date = event.get("event_date").toString();
                                        String start_time = event.get("start_time").toString();
                                        start_time = start_time.substring(0, 5);
                                        String end_time = event.get("end_time").toString();
                                        end_time = end_time.substring(0, 5);
                                        String user_id = event.getString("user_id");
                                        String user_reputation = event.getString("user_reputation");

                                        System.out.println("Event Header = " + event_header);

                                        eventModels.add(new EventModel(event_header, event_description, user_id, user_reputation, start_time, end_time, event_date));
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "No Events", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }

                            } catch (final JSONException e) {
                                Log.e("JSON Error", "JSON Parsing Error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "JSON Parsing Error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }
                
            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            listView.setAdapter(adapter);
            progressDialog.hide();
        }

    }

    public void onCreateEventClick(View view){
        Intent intent = new Intent(getApplicationContext(), EventCreateActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
