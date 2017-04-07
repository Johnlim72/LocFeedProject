package com.example.johnl.locfeedproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Monil on 3/22/17.
 */

public class LoginActivity extends AppCompatActivity{

    private String user_id, password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onCancelClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onLoginClick(View view){
        EditText user_id_et, password_et;

        user_id_et = (EditText) findViewById(R.id.user_id_L);
        password_et = (EditText) findViewById(R.id.password_L);

        user_id = user_id_et.getText().toString();
        password = password_et.getText().toString();

        if(user_id.equals("") || password.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "All Fields Are Required!", Toast.LENGTH_LONG);
            toast.show();
        } else{
            new LoginUser().execute(user_id, password);
        }
    }

    private class LoginUser extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Logging In");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings){
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/login_user.php";
                String data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                        URLEncoder.encode(user_id, "UTF-8");
                data += "&" + URLEncoder.encode("u_password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");


                System.out.println(data);

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

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
            if(s.equals("Success")){
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully Logged In!", Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);

            } else if(s.equals("Wrong Password")){
                Toast toast = Toast.makeText(getApplicationContext(), "You Entered The Wrong Password!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Wrong Credentials!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
