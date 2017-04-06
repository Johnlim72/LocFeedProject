package com.example.johnl.locfeedproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Monil on 4/1/17.
 */

public class EventCreateActivity extends Activity {
    private Calendar calendar;

    int year, month, day, startHour, startMinute, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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

    protected Dialog onCreateDialog(int id){
        calendar = Calendar.getInstance();
        if (id == 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(this, myDateListener, year, month, day);
        } else if(id == 2){
            startHour = calendar.get(Calendar.HOUR);
            startMinute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(this, myStartTimeListener, startHour, startMinute, true);
        } else if(id == 3){
            endHour = calendar.get(Calendar.HOUR);
            endMinute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(this, myEndTimeListener, endHour, endMinute, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day

            //showDate(arg1, arg2+1, arg3);
        }
    };

    private TimePickerDialog.OnTimeSetListener myStartTimeListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute){

        }
    };

    private TimePickerDialog.OnTimeSetListener myEndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        }
    };

}
