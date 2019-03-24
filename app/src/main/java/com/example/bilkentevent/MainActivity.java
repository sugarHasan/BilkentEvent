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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Cards[] cardsData;
    private arrayAdapter arrayAdapter;
    private ListView listView;
    List<Cards> rowItems;



    private ArrayList<Object> allClubs;

    private int i;
    private Button bProfile , bLogout;

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

        rowItems = new ArrayList<Cards>();


        getEvents();



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
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                userDb.child("Person").child(currentUid).child("Connections").child("Pass").child(userId).setValue(true);
                userDb.child("Club").child(userId).child("Connections").child("Pass").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this , "Maybe, next time?",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserID();
                userDb.child("Person").child(currentUid).child("Connections").child("Attend").child(userId).setValue(true);
                userDb.child("Club").child(userId).child("Connections").child("Attend").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this , "See you there!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                Toast.makeText(MainActivity.this , "Clicked",Toast.LENGTH_SHORT).show();
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

    }

    public void getEvents(){

        DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Club");
        getter.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("Pass").hasChild(currentUid) && !dataSnapshot.child("Connections").child("Attend").hasChild(currentUid) ){
                    HashMap<String, Object> datas = (HashMap<String, Object>) dataSnapshot.child("profile").getValue();
                    if(datas==null)
                        return;
                    String name = (String)datas.get("Club Name");
                    String id = (String)datas.get("Event Email");
                    String day = (String)datas.get("Day");
                    String month = (String)datas.get("Month");
                    String year = (String)datas.get("Year");
                    String topic = (String)datas.get("Topic");
                    String location = (String)datas.get("Location");


                    Cards item1 = new Cards(name, dataSnapshot.getKey());
                    rowItems.add(item1);
                    arrayAdapter.notifyDataSetChanged();
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

    public void logoutUser(View view){
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this , LoginRegisterActivity.class);
        startActivity(intent);
        finish();
        return;

    }


}