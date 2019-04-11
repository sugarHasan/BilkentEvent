package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EventActivity extends AppCompatActivity {

    private TextView name, location;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        Intent i = getIntent();
        Cards temp = (Cards)i.getSerializableExtra("Event");


        name = (TextView) findViewById(R.id.eventName);
        location = (TextView) findViewById(R.id.eventLocation);

        final DatabaseReference getter = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(temp.getUserID()).child("Events").child(temp.getEventID());


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
                name.setText(sTopic);
                location.setText(sLocation);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        image = (ImageView) findViewById(R.id.eventImage);


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
                //Picasso.get().load("").into(image);
            }
        });




    }
}
