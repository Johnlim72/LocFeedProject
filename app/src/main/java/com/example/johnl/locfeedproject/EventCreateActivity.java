package com.example.johnl.locfeedproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Monil on 4/1/17.
 */

//Activity for creating an event at a location.
public class EventCreateActivity extends Activity {
    private Calendar calendar;
    private String location_id, id;
    private boolean already_set_start_time, already_set_end_time;
    int year, month, day, startHour, startMinute, endHour, endMinute;

    String event_date, start_time, end_time;
    String event_header, event_description;

    ProgressDialog progressDialog;

    //sets the view on the screen for the "activity_choose" layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            location_id = extras.getString("LocationID");
            id = extras.getString("id");
        } else {
            location_id = "2";
            id = "0";
        }

        System.out.println("+__+_+_+++___ ID in CreateEventActivity: " + id);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event_create);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);
    }

    @SuppressWarnings("deprecation")
    public void setStartTime(View view) {
        showDialog(2);
    }

    @SuppressWarnings("deprecation")
    public void setEndTime(View view) {
        showDialog(3);
    }

    protected Dialog onCreateDialog(int id) {
        calendar = Calendar.getInstance();
        if (id == 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);

            datePickerDialog.setTitle("Event Date");

            return datePickerDialog;
        } else if (id == 2) {
            startHour = calendar.get(Calendar.HOUR);
            startMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, myStartTimeListener, startHour, startMinute, true);

            timePickerDialog.setTitle("Event Start Time");

            return timePickerDialog;
        } else if (id == 3) {
            endHour = calendar.get(Calendar.HOUR);
            endMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, myEndTimeListener, endHour, endMinute, true);

            timePickerDialog.setTitle("Event End Time");

            return timePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            event_date = year + "-" + month + "-" + day;
            System.out.println(event_date);

            if (already_set_start_time == false) {
                showDialog(2);
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener myStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String minute_string = "" + minute + "";
            String hour_string = "" + hour + "";
            if (minute < 10) {
                minute_string = "0" + minute + "";
            }
            if (hour < 10) {
                hour_string = "0" + hour + "";
            }
            start_time = hour_string + ":" + minute_string + ":00";
            System.out.println(start_time);

            already_set_start_time = true;

            if (already_set_end_time == false) {
                showDialog(3);
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener myEndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String minute_string = "" + minute + "";
            String hour_string = "" + hour + "";
            if (minute < 10) {
                minute_string = "0" + minute + "";
            }
            if (hour < 10) {
                hour_string = "0" + hour + "";
            }
            end_time = hour_string + ":" + minute_string + ":00";
            System.out.println(end_time);

            already_set_end_time = true;
        }
    };

    public void createEvent(View view) {
        EditText event_header_ET = (EditText) findViewById(R.id.event_header);
        EditText event_description_ET = (EditText) findViewById(R.id.event_description);

        event_header = event_header_ET.getText().toString().replaceAll("'", "''");
        event_description = event_description_ET.getText().toString().replaceAll("'", "''");

        new CreateNewEvent().execute(event_header, event_description, start_time, end_time, event_date);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
        intent.putExtra("LocationID", location_id);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private class CreateNewEvent extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventCreateActivity.this);
            progressDialog.setMessage("Creating Event");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                System.out.println("Before data");
                System.out.println("event_header: " + event_header);
                System.out.println("start_time: " + start_time);
                System.out.println("end_time: " + end_time);
                System.out.println("event_date: " + event_date);
                System.out.println("event_description: " + event_description);
                System.out.println("location_id: " + location_id);
                System.out.println("id: " + id);
                String link = "https://locfeed.000webhostapp.com/android_connect/create_event.php";
                String data = URLEncoder.encode("event_header", "UTF-8") + "=" +
                        URLEncoder.encode(event_header, "UTF-8");
                data += "&" + URLEncoder.encode("start_time", "UTF-8") + "=" +
                        URLEncoder.encode(start_time, "UTF-8");
                data += "&" + URLEncoder.encode("end_time", "UTF-8") + "=" +
                        URLEncoder.encode(end_time, "UTF-8");
                data += "&" + URLEncoder.encode("event_date", "UTF-8") + "=" +
                        URLEncoder.encode(event_date, "UTF-8");
                data += "&" + URLEncoder.encode("event_description", "UTF-8") + "=" +
                        URLEncoder.encode(event_description, "UTF-8");
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            System.out.println("onPost s:" + s);
            if (s.equals("Success!")) {
                Toast.makeText(getApplicationContext(), "Successfully Created Event", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EventFeedActivity.class);
                intent.putExtra("LocationID", location_id);
                intent.putExtra("id", id);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Error While Creating Event", Toast.LENGTH_LONG).show();
            }
        }
    }
}
