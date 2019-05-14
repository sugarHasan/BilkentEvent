package com.example.bilkentevent;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.annotation.NonNull;

/**
 * This is an adapter for the main page to hold event cards.
 * @author Hasan Yıldırım
 * @version
 */
public class AdapterMain extends ArrayAdapter<ClubEvent> {

    Context context;
    public AdapterMain(Context context , int resourceId , List<ClubEvent>items){
        super(context,resourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        ClubEvent event = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item , parent , false);
        }

        final ImageView image1 = (ImageView) convertView.findViewById(R.id.eventImage);
        String getStorage = "images/"+event.getEventID();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child(getStorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                //System.out.println(uri.toString());
                Picasso.get().load(uri.toString()).into(image1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.get().load("https://media.wired.com/photos/5b17381815b2c744cb650b5f/master/w_1164,c_limit/GettyImages-134367495.jpg").into(image1);
            }
        });
        return convertView;
    }



}
