package com.example.johnl.locfeedproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class QuestionFeedActivity extends AppCompatActivity {


    ArrayList<QuestionModel> questionModels;
    ListView listView;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_feed);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.question_list);

        questionModels = new ArrayList<>();

        adapter = new QuestionAdapter(questionModels,getApplicationContext());

        new GetQuestions().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionModel questionModel = questionModels.get(position);

                Snackbar.make(view, questionModel.getQuestion()+"\n"+" User: "+ questionModel.getUser(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    private class GetQuestions extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0){
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/get_questions_test.php";
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

                            JSONArray questions = jsonObject.getJSONArray("questions");

                            for(int i = 0; i < questions.length(); i++){
                                JSONObject question = questions.getJSONObject(i);
                                String question_details = question.getString("question_details");
                                String user_id = question.getString("user_id");

                                questionModels.add(new QuestionModel(question_details, "test_user", "test_rep"));
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