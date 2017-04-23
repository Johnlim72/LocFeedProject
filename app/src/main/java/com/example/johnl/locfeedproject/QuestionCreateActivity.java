package com.example.johnl.locfeedproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Monil on 4/1/17.
 */

public class QuestionCreateActivity extends Activity {

    String question_details;
    ProgressDialog progressDialog;

    private String location_id, id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            location_id = extras.getString("LocationID");
            id = extras.getString("id");
        } else {
            location_id = "2";
            id = "0";
        }

        System.out.println("++____++++____ ID in QuestionCreate: " + id);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question_create);
    }

    public void createQuestion(View view){
        EditText question_details_ET = (EditText) findViewById(R.id.question_details);

        question_details = question_details_ET.getText().toString();

        new CreateNewQuestion().execute(question_details);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), QuestionFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    class CreateNewQuestion extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuestionCreateActivity.this);
            progressDialog.setMessage("Creating Question");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args){
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/create_question.php";
                String data = URLEncoder.encode("question_details", "UTF-8") + "=" +
                        URLEncoder.encode(question_details, "UTF-8");
                data += "&" + URLEncoder.encode("location_id", "UTF-8") + "=" +
                        URLEncoder.encode(location_id, "UTF-8");
                data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" +
                        URLEncoder.encode(id, "UTF-8");

                System.out.println(data);

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = "";

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    System.out.println("Line: " + line);
                    sb.append(line);
                    break;
                }

                System.out.println("sb = " + sb.toString());

                return sb.toString();
            } catch (Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            progressDialog.hide();
            System.out.println("onPost s:" + s);
            if(s.equals("Success!")){
                Toast.makeText(getApplicationContext(), "Successfully Created Question", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), QuestionFeedActivity.class);
                intent.putExtra("LocationID", location_id);
                intent.putExtra("id", id);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Error While Creating Question", Toast.LENGTH_LONG).show();
            }
        }
    }

}