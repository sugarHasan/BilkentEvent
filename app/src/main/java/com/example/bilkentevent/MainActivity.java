package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * This is the main page. It displays the event cards, and has buttons to other screens.
 * @author Hasan Yıldırım, İbrahim Karakoç (minor contribution)
 * @version 13/05/19
 */
public class MainActivity extends AppCompatActivity {

    private AdapterMain arrayAdapter;
    List<ClubEvent> rowItems;
    private Button bProfile , bLogout, bCalendar;
    private TextView evName, evDate;
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;
    private String currentUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        evName = (TextView) findViewById(R.id.eveName);
        evDate = (TextView) findViewById(R.id.eveDate);
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        rowItems = new ArrayList<ClubEvent>();

        // This is to create event cards.
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
                            String start = (String)datas.get("Start Time");
                            String end = (String)datas.get("End Time");
                            String day = (String)datas.get("Day");
                            String month = (String)datas.get("Month");
                            String year = (String)datas.get("Year");
                            String topic = (String)datas.get("Topic");
                            String location = (String)datas.get("Location");
                            boolean active =  (boolean)datas.get("Active");
                            boolean past =  (boolean)datas.get("Passed");

                            if((active == true) && (past == false)) {

                                if (isPast(day, month, year) == false) {
                                    DatabaseReference r = childSnapshot.getRef();
                                    r.child("Profile").child("Passed").setValue(true);
                                } else {
                                    ClubEvent temp = new ClubEvent(new Date(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)), start, end, topic, clubId, childSnapshot.getKey(), location, (int) childSnapshot.child("Connections").child("Attend").getChildrenCount());
                                    rowItems.add(temp);
                                    arrayAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        }
                    Collections.sort(rowItems);

                    // After creating the event cards, this part takes the first card's info
                    // and displays it in the screen.
                    if ( rowItems.size() >= 1 ) {
                        ClubEvent obj = (ClubEvent) rowItems.get(0);
                        evName.setText(obj.getTopic());
                        evDate.setText(obj.getDayOfEvent().getDay() + " " + dateConverter(obj.getDayOfEvent().getMonth() + "") + " " + obj.getDayOfEvent().getYear() + "   " + obj.getStartTime() + " - " + obj.getFinishTime());
                    }
                    else {
                        evName.setText("There is no upcoming event.");
                        evDate.setText("");
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

        // This part is to make cards swappable.
        arrayAdapter = new AdapterMain(this, R.layout.item,rowItems);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            // We need to remove the swapped cards.
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();

                // After swapping a card, displayed info is changed.
                if ( rowItems.size() >= 1 ) {
                    ClubEvent obj = (ClubEvent) rowItems.get(0);
                    evName.setText(obj.getTopic());
                    evDate.setText(obj.getDayOfEvent().getDay() + " " + dateConverter(obj.getDayOfEvent().getMonth() + "") + " " + obj.getDayOfEvent().getYear() + "   " + obj.getStartTime() + " - " + obj.getFinishTime());
                }
                else {
                    evName.setText("There is no upcoming event.");
                    evDate.setText("");
                }
            }

            // Swapping left means the user will not participate.
            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                ClubEvent obj = (ClubEvent) dataObject;
                String clubId = obj.getClubID();
                String eventId = obj.getEventID();

                // In order not to show user the same event card again, we change the booleans in our database.
                userDb.child("Person").child(currentUid).child("Connections").child(clubId).child("Pass").child(eventId).setValue(true);
                userDb.child("Clubs").child(clubId).child("Events").child(eventId).child("Connections").child("Pass").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this , "Maybe, next time?",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                ClubEvent obj = (ClubEvent) dataObject;
                String clubId = obj.getClubID();
                String eventId = obj.getEventID();
                // In order not to show user the same event card again, we change the booleans in our database.
                // Also, we need this to display the user in event page.
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
                startActivity(intent);;
            }
        });


        bProfile = (Button) findViewById(R.id.profile);
        bProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (MainActivity.this , ProfileActivity.class);
                startActivity(intent);
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

    }

    // This method is used when displaying the info. Instead of the number of the months, it shows the name.
    public static final String[] months = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public String dateConverter (String month) {
        return months[Integer.parseInt(month) - 1];
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }



}
