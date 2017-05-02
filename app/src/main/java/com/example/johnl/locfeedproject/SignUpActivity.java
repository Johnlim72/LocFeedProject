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

//Activity for users to sign up for LocFeed
public class SignUpActivity extends AppCompatActivity{

    private String f_name, l_name, user_id, password, re_password;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onCancelClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onSubmitClick(View view){
        EditText f_name_et, l_name_et, user_id_et, email_et, password_et, re_password_et;

        f_name_et = (EditText) findViewById(R.id.first_name_ET);
        l_name_et = (EditText) findViewById(R.id.last_name_ET);
        user_id_et = (EditText) findViewById(R.id.user_id_ET);
        password_et = (EditText) findViewById(R.id.password_ET);
        re_password_et = (EditText) findViewById(R.id.re_password_ET);

        f_name = f_name_et.getText().toString();
        l_name = l_name_et.getText().toString();
        user_id = user_id_et.getText().toString();
        password = password_et.getText().toString();
        re_password = re_password_et.getText().toString();

        if(!password.equals(re_password)){
            System.out.println("Password: " + password);
            System.out.println("Re_Password: " + re_password);
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords Don't Match!", Toast.LENGTH_LONG);
            toast.show();
        } else if(f_name.equals("") || l_name.equals("") || user_id.equals("") || password.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), "All Fields Are Required!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            new CreateNewUser().execute(f_name, l_name, user_id, password);
        }
    }

    private class CreateNewUser extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Creating User");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings){
            try {
                String link = "https://locfeed.000webhostapp.com/android_connect/create_user.php";
                String data = URLEncoder.encode("f_name", "UTF-8") + "=" +
                        URLEncoder.encode(f_name, "UTF-8");
                data += "&" + URLEncoder.encode("l_name", "UTF-8") + "=" +
                        URLEncoder.encode(l_name, "UTF-8");
                data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" +
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

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            System.out.println("onPost: s = " + s);
            progressDialog.hide();

            if (s.equals("Success!")) {
                Toast.makeText(SignUpActivity.this, "Sucessfully created user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } else {
                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}