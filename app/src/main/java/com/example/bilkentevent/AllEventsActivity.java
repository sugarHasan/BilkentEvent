package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * This interface is to display all the events user will participate in a list.
 * @author İbrahim Karakoç & Hasan Yıldırım
 * @version 15/05/2019
 */
public class AllEventsActivity extends AppCompatActivity {

    private AdapterAllEvents adapter;
    private String currentUID;
    private EventBox box;
    private FirebaseAuth mAuth;
    private ArrayList<Event> list;
    private Button bSortDate, bSortName, bChangeDisplay;
    private ListView listView;
    private boolean b;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        bSortDate = (Button) findViewById(R.id.sortByDate);
        bSortName = (Button) findViewById(R.id.sortByName);
        bChangeDisplay = (Button) findViewById(R.id.changeDisplay);
        listView =  (ListView) findViewById(R.id.listViewForAll);
        title = (TextView)  findViewById(R.id.title);
        list = new ArrayList<Event>();
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getUid();


        // Taking the events from database and sorting them.
        getEvents();
        Collections.sort(list);
        b = true;

        // We need an adapter to take all these events and hold.
        adapter = new AdapterAllEvents(AllEventsActivity.this, R.layout.list_all_events,list);
        // We assign the adapter to list view in the interface.
        listView.setAdapter(adapter);

        // We need an action listener to make user be able to press the events in the list.
        // I took this part directly from Hasan's CalendarActivity.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = list.get(position);
                ClubEvent c = new ClubEvent(new Date(0, 0, 0),(new Time(0, 0)).toString(), (new Time(0, 0)).toString()," ", " ", " ", " ",0);
                if(e.getClass().equals(c.getClass())){
                    ClubEvent clubEvent = (ClubEvent)e;

                    Cards card = new Cards(clubEvent.getClubID(), clubEvent.getEventID());
                    Intent intent = new Intent(AllEventsActivity.this, EventActivity.class);
                    intent.putExtra("Event", card);
                    startActivity(intent);
                }
            }
        });

        // This action listener changes the sorting criteria.
        bSortName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Collections.sort(list, new Comparator<Event>() {
                    public int compare(Event e1, Event e2) {
                        return e1.getTopic().compareTo(e2.getTopic());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        // This action listener changes the sorting criteria.
        bSortDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Collections.sort(list);
                adapter.notifyDataSetChanged();
            }
        });

        // This action listener changes the list of events.
        // If you click the button while the events you liked are displaying,
        // It will switch and show you the events you disliked, and vice versa.
        bChangeDisplay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                if (b) {
                    bChangeDisplay.setText("Show liked");
                    title.setText ("Events you will not join:");
                    list.clear();
                    getPassEvents();;
                    b = false;
                }
                else {
                    bChangeDisplay.setText("Show disliked");
                    title.setText ("Events you will join:");
                    list.clear();
                    getEvents();
                    b = true;
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    // This is a method to take the events from database. I did some minor changes, but the
    // original method belongs to Hasan.
    public void getEvents(){

        // Database referance to the PERSONAL events.
        DatabaseReference getter;
        getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("PersonalEvents");
        getter.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                    // Taking the data.
                    HashMap<String, Object> datas = (HashMap<String, Object>) dataSnapshot.getValue();
                    if(datas==null)
                        return;

                    // Putting the data together.
                    String topic = (String)datas.get("Topic");
                    String day = (String)datas.get("Day");
                    String month = (String)datas.get("Month");
                    String year = (String)datas.get("Year");
                    String startTime = (String)datas.get("Start Time");
                    String endTime = (String)datas.get("End Time");

                    // If the date of the event is not passed,
                    if(!isPast(day,month,year)){
                        DatabaseReference r = dataSnapshot.getRef();
                        r.child("Passed").setValue(true);
                        System.out.println ("Check4");
                    }

                    // A personal event will be created & added to our list.
                    int dayOfEvent = Integer.parseInt(day);
                    int monthOfEvent = Integer.parseInt(month);
                    int yearOfEvent = Integer.parseInt(year);
                    Date date = new Date(dayOfEvent,monthOfEvent,yearOfEvent);
                    PersonalEvent pers = new PersonalEvent(date,startTime,endTime,topic);
                    list.add(pers);
                    Collections.sort(list);
                    adapter.notifyDataSetChanged();
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

        // Database reference to CLUB events.
        DatabaseReference getterC = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("Connections");
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

                                    // Taking the data from the database.
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



                                    // If the date of the event is not passed,
                                    if (isPast(day, month, year) == false) {
                                        DatabaseReference r = snapshot.getRef();
                                        r.child("Profile").child("Passed").setValue(true);
                                    }

                                    // A club event will be created & added to our list.
                                    ClubEvent temp = new ClubEvent(new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)), start, end, topic, clubID, eventID, location,(int) snapshot.child("Connections").child("Attend").getChildrenCount());
                                    list.add(temp);
                                    Collections.sort(list);
                                    adapter.notifyDataSetChanged();
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
    public void getPassEvents(){
        // Database reference to CLUB events.
        DatabaseReference getterC = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(currentUID).child("Connections");
        getterC.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    final String clubID = dataSnapshot.getKey();
                    for (DataSnapshot childSnapshot : dataSnapshot.child("Pass").getChildren()) {
                        if(childSnapshot.exists()) {

                            final String eventID = childSnapshot.getKey();
                            DatabaseReference event = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(clubID).child("Events").child(eventID);
                            event.child("Profile").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    // Taking the data from the database.
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



                                    // If the date of the event is not passed,
                                    if (isPast(day, month, year) == false) {
                                        DatabaseReference r = snapshot.getRef();
                                        r.child("Profile").child("Passed").setValue(true);
                                    }

                                    // A club event will be created & added to our list.
                                    ClubEvent temp = new ClubEvent(new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)), start, end, topic, clubID, eventID, location,(int) snapshot.child("Connections").child("Attend").getChildrenCount());
                                    list.add(temp);
                                    Collections.sort(list);
                                    adapter.notifyDataSetChanged();
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
