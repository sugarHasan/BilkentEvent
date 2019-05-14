package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;

/**
 * An interface to display a particular event.
 * @author Hasan Yıldırım & H. İbrahim Karakoç
 * @version 14/05-19
 */
public class EventActivity extends AppCompatActivity {

    private TextView name, location, date;
    private Button addEvent;
    private ImageView image;
    private FirebaseAuth mAuth;
    private DatabaseReference userDb;
    private String currentUid;
    private  ArrayList<String> participants;
    private ArrayList<Person> list;
    AdapterPerson adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent i = getIntent();
        final Cards temp = (Cards)i.getSerializableExtra("Event");
        final ListView listView =  (ListView)findViewById(R.id.listview);
        addEvent = (Button) findViewById(R.id.addEvent);
        image = (ImageView) findViewById(R.id.eventImage);
        name = (TextView) findViewById(R.id.eventName);
        location = (TextView) findViewById(R.id.eventLocation);
        date = (TextView) findViewById(R.id.eventTime);
        participants = new ArrayList<String>();
        list = new ArrayList<Person>();
        adapter = new AdapterPerson(EventActivity.this, R.layout.listperson,list);

        listView.setAdapter(adapter);

        final DatabaseReference personGetter = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(temp.getClubID()).child("Events").child(temp.getEventID()).child("Connections").child("Attend");


        // This is for displaying the participants.
        personGetter.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {

                        final String personID = dataSnapshot.getKey();
                        final DatabaseReference getPerson = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(personID);
                        getPerson.child("Profile").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                // Gets the information of users and adds them to an adapter.
                                HashMap<String, Object> datas = (HashMap<String, Object>) snapshot.getValue();
                                if(datas==null)
                                    return;
                                String sName = (String)datas.get("Name");
                                String sMotto = (String)datas.get("Motto");
                                Person person = new Person(personID,sName);
                                person.setMotto(sMotto);
                                list.add(person);
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


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

        final DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(temp.getClubID()).child("Events").child(temp.getEventID());

        // This is for displaying the title, location and date&time of the event.
        getter.child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> datas = (HashMap<String, Object>) snapshot.getValue();
                if(datas==null)
                    return;
                String sName = (String)datas.get("Club Name");
                String sid = (String)datas.get("Event Email");
                String sday = (String)datas.get("Day");
                String smonth = (String)datas.get("Month");
                String syear = (String)datas.get("Year");
                String sTopic = (String)datas.get("Topic");
                String sLocation = (String)datas.get("Location");
                String sStart = (String)datas.get("Start Time");
                String sEnd = (String)datas.get("End Time");
                name.setText(sTopic);
                location.setText(sLocation);
                date.setText (sday + " " + dateConverter(smonth) + " " + syear + "   " + sStart + " - " + sEnd);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });






        // This is for displaying the picture of the event.
        String getStorage = "images/"+temp.getEventID();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(getStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                //System.out.println(uri.toString());
                Picasso.get().load(uri.toString()).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                //System.out.println("error");
                Picasso.get().load("https://media.wired.com/photos/5b17381815b2c744cb650b5f/master/w_1164,c_limit/GettyImages-134367495.jpg").into(image);
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                userDb = FirebaseDatabase.getInstance().getReference().child("Users");
                mAuth = FirebaseAuth.getInstance();
                currentUid = mAuth.getCurrentUser().getUid();
                userDb.child("Person").child(currentUid).child("Connections").child(temp.getClubID()).child("Attend").child(temp.getEventID()).setValue(true);
                userDb.child("Clubs").child(temp.getClubID()).child("Events").child(temp.getEventID()).child("Connections").child("Attend").child(currentUid).setValue(true);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person p = list.get(position);
                Intent intent = new Intent(EventActivity.this, PersonActivity.class);
                intent.putExtra("Person", p);
                startActivity(intent);
            }

        });

    }

    private void getParticipants() {
        for(int q = 0 ; q < participants.size(); q++){
            final String personID = participants.get(q);
            final DatabaseReference getPerson = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(personID);


            getPerson.child("Profile").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    HashMap<String, Object> datas = (HashMap<String, Object>) snapshot.getValue();
                    if(datas==null)
                        return;
                    String sName = (String)datas.get("Name");
                    Person person = new Person(personID,sName);
                    list.add(person);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        final ListView listView =  (ListView)findViewById(R.id.listview);
        adapter = new AdapterPerson(EventActivity.this, R.layout.listperson,list);
        listView.setAdapter(adapter);


    }

    public static final String[] months = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public String dateConverter (String month) {
        return months[Integer.parseInt(month) - 1];
    }

}

