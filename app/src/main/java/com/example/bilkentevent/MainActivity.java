package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private arrayAdapter arrayAdapter;
    List<ClubEvent> rowItems;
    private Button bProfile , bLogout, bCalendar;
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;
    private String currentUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        rowItems = new ArrayList<ClubEvent>();

            final DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs");
            getter.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        final String clubId = dataSnapshot.getKey();
                        for (DataSnapshot childSnapshot : dataSnapshot.child("Events").getChildren()) {
                            if(childSnapshot.exists() && !childSnapshot.child("Connections").child("Pass").hasChild(currentUid) && !childSnapshot.child("Connections").child("Attend").hasChild(currentUid))    {
                                HashMap<String, Object> datas = (HashMap<String, Object>) childSnapshot.child("Profile").getValue();
                                if(datas==null)
                                    return;
                                String name = (String)datas.get("Club Name");
                                String id = (String)datas.get("Event Email");
                                String day = (String)datas.get("Day");
                                String month = (String)datas.get("Month");
                                String year = (String)datas.get("Year");
                                String topic = (String)datas.get("Topic");
                                String location = (String)datas.get("Location");

                                if(isPast(day,month,year)==false){
                                    DatabaseReference r = childSnapshot.getRef();
                                    r.child("Profile").child("Passed").setValue(true);
                                }
                                else {
                                    ClubEvent temp = new ClubEvent(new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)),new Time(16, 00), new Time(18, 00),topic, clubId, childSnapshot.getKey(), location,(int)childSnapshot.child("Connections").child("Attend").getChildrenCount());
                                    rowItems.add(temp);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                        Collections.sort(rowItems);
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

        arrayAdapter = new arrayAdapter(this, R.layout.item,rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                ClubEvent obj = (ClubEvent) dataObject;
                String clubId = obj.getClubID();
                String eventId = obj.getEventID();
                userDb.child("Person").child(currentUid).child("Connections").child(clubId).child("Pass").child(eventId).setValue(true);
                userDb.child("Clubs").child(clubId).child("Events").child(eventId).child("Connections").child("Pass").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this , "Maybe, next time?",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                ClubEvent obj = (ClubEvent) dataObject;
                String clubId = obj.getClubID();
                String eventId = obj.getEventID();
                userDb.child("Person").child(currentUid).child("Connections").child(clubId).child("Attend").child(eventId).setValue(true);
                userDb.child("Clubs").child(clubId).child("Events").child(eventId).child("Connections").child("Attend").child(currentUid).setValue(true);

                Toast.makeText(MainActivity.this , "See you there!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

                //arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                ClubEvent clubEvent = (ClubEvent)dataObject;
                Cards card = new Cards(clubEvent.getClubID(),clubEvent.getEventID());
                Toast.makeText(MainActivity.this , "Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (MainActivity.this , EventActivity.class);
                intent.putExtra("Event", card);
                startActivity(intent);
                finish();
                return;


            }
        });


        bProfile = (Button) findViewById(R.id.profile);
        bProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (MainActivity.this , ProfileActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        bLogout = (Button) findViewById(R.id.logout);
        bLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                logoutUser(view);
            }
        });

        bCalendar = (Button) findViewById(R.id.calendar);
        bCalendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (MainActivity.this , CalendarActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }



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

    public void logoutUser(View view){
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
        startActivity(intent);
        finish();
        return;

    }



}
