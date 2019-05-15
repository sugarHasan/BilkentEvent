package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * This interface displays the events user selected in a calender.
 * @author Hasan Yıldırım
 * @version 13/05/19
 */
public class CalendarActivity extends AppCompatActivity{
    CalendarView myCalender;
    Date date;
    private Button mybutton;

    private AdapterEvent adapter;
    String currentUID;


    EventBox box;
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        myCalender = findViewById(R.id.calendar);
        mybutton = (Button) findViewById(R.id.button3);


        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getUid();

        final Intent i = new Intent(CalendarActivity.this,AddPersonalEvent.class);
        final ArrayList<Event> list = new ArrayList<Event>();
        box = new EventBox();
        getEvents();
        adapter = new AdapterEvent(CalendarActivity.this, R.layout.listevent,list);
        final ListView  listView =  (ListView)findViewById(R.id.listView1);

        // When user choose a day from the calendar;
        myCalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                // It empties the adapter and the list,
                if(!adapter.isEmpty())
                    adapter.clear();
                if(!list.isEmpty())
                    list.clear();
                date = new Date(dayOfMonth,month+1,year);
                i.putExtra("day",dayOfMonth);
                i.putExtra("month",month);
                i.putExtra("year",year);

                // Checks which events are up today,
                for(int i = 0 ; i < box.getSize() ; i++){
                    Event checker = box.getEvent(i);
                    Date eventDay = checker.getDayOfEvent();
                    if(eventDay.getDay()==dayOfMonth && eventDay.getMonth()==(month+1) && eventDay.getYear()==year) {
                        // And adds them to the list.
                        list.add(checker);
                    }
                }
                if(!list.isEmpty()) {
                    adapter = new AdapterEvent(CalendarActivity.this, R.layout.listevent,list);
                    listView.setAdapter(adapter);
                }
            }
        });

        // This is to make user be able to create his own events.
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CalendarActivity.this , AddPersonalEvent.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = list.get(position);
                ClubEvent c = new ClubEvent(new Date(0, 0, 0),(new Time(0, 0)).toString(), (new Time(0, 0)).toString()," ", " ", " ", " ",0);
                if(e.getClass().equals(c.getClass())){
                    ClubEvent clubEvent = (ClubEvent)e;

                    Cards card = new Cards(clubEvent.getClubID(), clubEvent.getEventID());
                    Intent intent = new Intent(CalendarActivity.this, EventActivity.class);
                    intent.putExtra("Event", card);
                    startActivity(intent);
                }
            }
        });
    }

    // Taking the events from database. First it takes the personal events, and then the club ones.
    public void getEvents(){

        final DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("PersonalEvents");
        getter.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {


                    HashMap<String, Object> datas = (HashMap<String, Object>) dataSnapshot.getValue();
                    if(datas==null)
                        return;
                    String topic = (String)datas.get("Topic");
                    String day = (String)datas.get("Day");
                    String month = (String)datas.get("Month");
                    String year = (String)datas.get("Year");
                    String startTime = (String)datas.get("Start Time");
                    String endTime = (String)datas.get("End Time");


                    if(isPast(day,month,year) == false){
                        DatabaseReference r = dataSnapshot.getRef();
                        r.child("Passed").setValue(true);
                    }

                    int dayOfEvent = Integer.parseInt(day);
                    int monthOfEvent = Integer.parseInt(month);
                    int yearOfEvent = Integer.parseInt(year);
                    Date date = new Date(dayOfEvent,monthOfEvent,yearOfEvent);
                    PersonalEvent pers = new PersonalEvent(date,startTime,endTime,topic);
                    box.addEvent(pers);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference getterC = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("Connections");
        getterC.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    final String clubID = dataSnapshot.getKey();
                    for (DataSnapshot childSnapshot : dataSnapshot.child("Attend").getChildren()) {

                        if(childSnapshot.exists()) {

                            final String eventID = childSnapshot.getKey();
                            DatabaseReference event = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(clubID).child("Events").child(eventID);
                            event.child("Profile").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    HashMap<String, Object> datas = (HashMap<String, Object>) snapshot.getValue();
                                    if (datas == null)
                                        return;
                                    String start = (String) datas.get("Start Time");
                                    String end = (String) datas.get("End Time");
                                    String day = (String) datas.get("Day");
                                    String month = (String) datas.get("Month");
                                    String year = (String) datas.get("Year");
                                    String topic = (String) datas.get("Topic");
                                    String location = (String) datas.get("Location");



                                    if (isPast(day, month, year) == false) {
                                        DatabaseReference r = snapshot.getRef();
                                        r.child("Profile").child("Passed").setValue(true);
                                    }
                                        ClubEvent temp = new ClubEvent(new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)), start, end, topic, clubID, eventID, location,(int) snapshot.child("Connections").child("Attend").getChildrenCount());
                                        box.addEvent(temp);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }

                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // This method is to check whether a date has passed.
    // Author: Hasan.
    private boolean isPast(String day, String month, String year) {
        int iDay = Integer.parseInt(day);
        int iMonth = Integer.parseInt(month);
        int iYear = Integer.parseInt(year);

        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if(currentYear > iYear)
            return false;
        else if(currentYear == iYear)   {
            if(currentMonth > iMonth)   {
                return false;
            }
            else if(currentMonth == iMonth) {
                if(currentDay>iDay) {
                    return false;
                }
                else if(currentDay<= iDay)
                    return true;
            }
            else
                return true;

        }
        else
            return true;
        return true;
    }


}


