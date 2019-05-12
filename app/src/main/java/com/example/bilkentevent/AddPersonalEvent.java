package com.example.bilkentevent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;

public class AddPersonalEvent extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;
    private DatePickerDialog.OnDateSetListener mDateListener;
    Event myEvent;
    TextView dateText;
    EditText event;
    Button eventButton;
    Button create;
    TextView all;
    Button cancel;
    Date dateOfEvent;
    Time time;
    String createdEvent;
    String dayOfEvent;
    String monthOfEvent;
    String yearOfEvent;
    String hour;
    String minute;
    Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_event);

        //variables
        TextView date = findViewById(R.id.date);
        event = findViewById(R.id.event1);
        eventButton = findViewById(R.id.button6);
        create = findViewById(R.id.button2);

        cancel = findViewById(R.id.button5);

        time = new Time(0,0);
        dateText = findViewById(R.id.dateText);
        //get ıntent
        Intent incomingIntent = getIntent();
        int day = (int) incomingIntent.getIntExtra("day",cal.get(Calendar.DAY_OF_MONTH));
        final int month = (int) incomingIntent.getIntExtra("month",cal.get(Calendar.MONTH)) + 1;
        int year = (int) incomingIntent.getIntExtra("year",cal.get(Calendar.YEAR));
        dateText.setText("DATE: "+ day + "/" + month + "/" + year);

        dayOfEvent = day+"";
        monthOfEvent = ""+month;
        yearOfEvent = ""+year;
        dateOfEvent=new Date(day,month,year);


        //set date
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int yearD = c.get(Calendar.YEAR);
                int monthD = c.get(Calendar.MONTH);
                int dayD = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddPersonalEvent.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateListener,yearD,monthD,dayD);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dayOfEvent = "" + dayOfMonth;
                monthOfEvent = "" + month;
                yearOfEvent = "" + year;
                dateText.setText("DATE: "+ dayOfEvent + "/" + monthOfEvent + "/" + yearOfEvent);
            }
        };



        //show time picker
        final Button myButton = (Button)findViewById(R.id.button4);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        final Intent backToCalender = new Intent(this,CalendarActivity.class);

        //getEvent
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdEvent = event.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                String rand = RandomString.generate(10);
                String currentUID = mAuth.getUid();
                userDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("PersonalEvents").child(rand);
                HashMap<String, Object> datas = new HashMap<String, Object>();
                datas.put("Topic" ,createdEvent);
                datas.put("Day" , dayOfEvent);
                datas.put("Month" , monthOfEvent);
                datas.put("Year" , yearOfEvent);
                datas.put("Hour" , hour);
                datas.put("Minute" , minute);
                datas.put("Passed" , false);
                userDb.setValue(datas);
                startActivity(backToCalender);

            }
        });
        //send event
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backToCalender);

            }
        });
    }



    //setTime
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView text = (TextView) findViewById(R.id.textTime);
        hour = ""+hourOfDay;
        this.minute = ""+minute;
        text.setText(hour +" : " + this.minute);

    }





}

