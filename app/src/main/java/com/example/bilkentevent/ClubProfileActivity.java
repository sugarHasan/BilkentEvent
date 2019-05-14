package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClubProfileActivity extends AppCompatActivity {

    private TextView mNameField, mMottoField;

    private Button bAddEvent, bGetEvents;

    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private AdapterEvent adapter;
    private String userId, name, motto;
    private ListView listView;
    EventBox box;
    private Uri resultUri;
    private ArrayList<Event> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_profile);
        box = new EventBox();
        list = new ArrayList<Event>();
        mNameField = (TextView) findViewById(R.id.name);


        mProfileImage = (ImageView) findViewById(R.id.profileImage);


        bAddEvent = (Button) findViewById(R.id.addEvent);
        bGetEvents = (Button) findViewById(R.id.getEvents);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(userId).child("Profile");



        bAddEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (ClubProfileActivity.this , AddEventActivity.class);
                startActivity(intent);
            }
        });


        bGetEvents.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                if(list.isEmpty() == false) {
                    adapter = new AdapterEvent(ClubProfileActivity.this, R.layout.listevent,list);
                    listView.setAdapter(adapter);
                }
            }
        });


        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String currentUID = mAuth.getUid();
        adapter = new AdapterEvent(ClubProfileActivity.this, R.layout.listevent,list);

        ClubEvent temp = new ClubEvent(new Date(12,12,2019), "","","","","","",12);
        box.addEvent(temp);
        getEvents();
        adapter.notifyDataSetChanged();
        listView =  (ListView)findViewById(R.id.list);
        getUserInfo();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = list.get(position);


                ClubEvent clubEvent = (ClubEvent)e;

                Cards card = new Cards(clubEvent.getClubID(), clubEvent.getEventID());
                Intent intent = new Intent(ClubProfileActivity.this, EventActivity.class);
                intent.putExtra("Event", card);
                startActivity(intent);

            }
        });
    }

    private void getEvents() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String currentUID = mAuth.getUid();
        final DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(currentUID).child("Events");
        getter.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println(dataSnapshot.getKey());
                if (dataSnapshot.exists() ) {

                    String eventID = dataSnapshot.getKey();
                    System.out.println("asd");
                    HashMap<String, Object> datas = (HashMap<String, Object>) dataSnapshot.child("Profile").getValue();
                    if(datas==null)
                        return;
                    String startTime = (String) datas.get("Start Time");
                    String endTime = (String) datas.get("End Time");
                    String day = (String) datas.get("Day");
                    String month = (String) datas.get("Month");
                    String year = (String) datas.get("Year");
                    String topic = (String) datas.get("Topic");
                    String location = (String) datas.get("Location");
                    boolean active =  (boolean)datas.get("Active");

                    if(isPast(day,month,year) == false){
                        DatabaseReference r = dataSnapshot.getRef();
                        r.child("Passed").setValue(true);
                    }

                    int dayOfEvent = Integer.parseInt(day);
                    int monthOfEvent = Integer.parseInt(month);
                    int yearOfEvent = Integer.parseInt(year);
                    Date date = new Date(dayOfEvent,monthOfEvent,yearOfEvent);
                    ClubEvent temp = new ClubEvent(date, startTime, endTime, topic, currentUID, eventID, location, (int) dataSnapshot.child("Connections").child("Attend").getChildrenCount());
                    list.add(temp);
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








    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Club Name")!=null){
                        name = map.get("Club Name").toString();
                        mNameField.setText(name);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String getStorage = "images/"+userId;



        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child(getStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(mProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ClubProfileActivity.super.onBackPressed();
                    }
                }).create().show();
        mAuth.signOut();
    }


}











