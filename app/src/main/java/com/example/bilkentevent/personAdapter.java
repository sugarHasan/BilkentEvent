package com.example.bilkentevent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;

public class personAdapter extends ArrayAdapter<Person> {

    ImageView image;
    Context context;
    public personAdapter(Context context , int resourceId , List<Person>items){
        super(context,resourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        // Get the data item for this position
        Person e = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listperson, parent, false);
        }

        String getStorage = "profileImages/"+e.getPersonID();
        image = (ImageView) convertView.findViewById(R.id.image);

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

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView motto = (TextView) convertView.findViewById(R.id.motto);

        // Populate the data into the template view using the data object
        name.setText(e.getName());
        motto.setText("Motto will be here..");



        return convertView;
    }



}
