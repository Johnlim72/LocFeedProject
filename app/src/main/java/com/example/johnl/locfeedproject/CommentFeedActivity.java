package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

//Sets the CommentAdapter to the listview
//Gets the comments for the specific location
public class CommentFeedActivity extends AppCompatActivity {

    ArrayList<CommentModel> commentModels;
    ListView listView;
    private CommentAdapter adapter;

    private String location_id, id;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_feed);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            location_id = extras.getString("LocationID").toString();
            id = extras.getString("id").toString();
        } else {
            location_id = "2";
            id = "0";
        }

        System.out.println("!_!_@_@_$_@_!@ ID in CommentFeed: " + id);

        listView=(ListView)findViewById(R.id.comment_list);

        commentModels = new ArrayList<>();

        adapter = new CommentAdapter(commentModels,getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Comments");
        progressDialog.show();

        new GetComments().execute();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CommentModel commentModel = commentModels.get(position);

                Snackbar.make(view, commentModel.getComment()+"\n"+" User: "+ commentModel.getUser(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //when 'refresh feed' button is clicked
    public void OnRefresh(View view) {
        Intent intent = new Intent(getApplicationContext(), CommentFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);

        startActivity(intent);
    }

    private class GetComments extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0){
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/get_comments.php";
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
                        if(jsonString.equals("No results")){
                            Toast.makeText(getApplicationContext(), "No Comments!", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String success = jsonObject.getString("success");

                                if(success.equals("1")){
                                    JSONArray comments = jsonObject.getJSONArray("comments");

                                    for (int i = 0; i < comments.length(); i++) {
                                        JSONObject comment = comments.getJSONObject(i);
                                        String comment_details = comment.getString("comment_details");
                                        String user_id = comment.getString("user_id");
                                        String user_reputation = comment.getString("user_reputation");
                                        String commenter_id = comment.getString("commenter_id");

                                        commentModels.add(new CommentModel(comment_details, user_id, user_reputation, commenter_id));
                                    }
                                } else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "No Comments", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            } catch (final JSONException e) {
                                Log.e("JSON Error", "JSON Parsing Error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "No Comments!",
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

    //starts CommentCreateActivity
    public void onCommentCreateClick(View view){
        Intent intent = new Intent(getApplicationContext(), CommentCreateActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}