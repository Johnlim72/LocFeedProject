package com.example.johnl.locfeedproject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.event_list);

        eventModels = new ArrayList<>();

        adapter = new EventAdapter(eventModels, getApplicationContext());

        new GetEvents().execute();

        System.out.println("At end of On Create");
    }

    private class GetEvents extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/get_events_test.php";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                String data = URLEncoder.encode("location_id", "UTF-8") +
                        URLEncoder.encode("1", "UTF-8");

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
                    if(jsonString != null){
                        try{
                            JSONObject jsonObject = new JSONObject(jsonString);

                            JSONArray events = jsonObject.getJSONArray("events");

                            for(int i = 0; i < events.length(); i++){
                                JSONObject event = events.getJSONObject(i);
                                String event_header = event.getString("event_header");
                                String event_description = event.getString("event_description");
                                String event_date = event.get("event_date").toString();
                                String start_time = event.get("start_time").toString();
                                String end_time = event.get("end_time").toString();

                                System.out.println("Event Header = " + event_header);

                                eventModels.add(new EventModel(event_header, event_description, "test_user", "test_rep", start_time, end_time, event_date));
                            }



                        } catch (final JSONException e){
                            Log.e("JSON Error", "JSON Parsing Error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
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
            System.out.println("On Post Execute");
            listView.setAdapter(adapter);
        }

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
