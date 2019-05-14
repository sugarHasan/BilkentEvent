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
import com.google.firebase.storage.FirebaseStorage;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;

public class AddPersonalEvent extends AppCompatActivity {
    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private EditText etYear,etDay,etMonth,etTopic,etLocation,etStartTime,etEndTime;
    EditText event;
    Button bRegister;
    String createdEvent;
    String currentUID;
    Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_event);

        //variables
        bRegister = (Button) findViewById(R.id.add);
        etYear = (EditText) findViewById(R.id.year);
        etDay = (EditText) findViewById(R.id.day);
        etMonth = (EditText) findViewById(R.id.month);
        etTopic = (EditText) findViewById(R.id.topic);
        etLocation = (EditText) findViewById(R.id.location);
        etStartTime = (EditText) findViewById(R.id.startTime);
        etEndTime = (EditText) findViewById(R.id.endTime);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getUid();

        //set date
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rand = RandomString.generate(10);
                userDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("PersonalEvents").child(rand);

                final String year = etYear.getText().toString();
                final String month = etMonth.getText().toString();
                final String day = etDay.getText().toString();
                final String topic = etTopic.getText().toString();
                final String location = etLocation.getText().toString();
                final String startTime = etStartTime.getText().toString();
                final String endTime = etEndTime.getText().toString();
                HashMap<String, Object> datas = new HashMap<String, Object>();
                datas.put("Topic", topic);
                datas.put("Location", location);
                datas.put("Month", month);
                datas.put("Day", day);
                datas.put("Year", year);
                datas.put("Start Time", startTime);
                datas.put("End Time", endTime);
                datas.put("Passed", false);
                datas.put("Active", true);

                userDb.setValue(datas);
                finish();
            }
        });
    }
}

