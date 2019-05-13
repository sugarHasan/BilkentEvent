package com.example.bilkentevent;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PersonActivity extends AppCompatActivity {
    private TextView name, motto;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent i = getIntent();
        final Person p = (Person) i.getSerializableExtra("Person");
        final String personID = p.getPersonID();


        image = (ImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        motto = (TextView) findViewById(R.id.motto);

        name.setText(p.getName());
        motto.setText(p.getMotto());
        String getStorage = "images/"+personID;


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child(getStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri.toString()).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/bilkent-event.appspot.com/o/profileImages%2FcXfPO8N9Ybf5K35y0sRT5fs46p33?alt=media&token=e11f7e6f-1ccb-4872-830d-b92b3fceb270").into(image);
            }
        });

    }
}
