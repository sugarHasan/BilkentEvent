package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * This interface make clubs be able to create new events that will be displayed in calendar.
 * @author Hasan Yıldırım & İbrahim Karakoç (some minor changes)
 * @version 10/05/19
 */
public class AddEventActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userDb;

    private StorageReference mStorageRef;

    private EditText etYear,etDay,etMonth,etTopic,etLocation,etStartTime,etEndTime;

    private HashMap<String,Object> getValue;
    private String currentUid;
    private Button bRegister;

    private ImageView mImage;
    private Uri resultUri;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_add_event);



        userDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        bRegister = (Button) findViewById(R.id.add);

        etYear = (EditText) findViewById(R.id.year);
        etDay = (EditText) findViewById(R.id.day);
        etMonth = (EditText) findViewById(R.id.month);
        etTopic = (EditText) findViewById(R.id.topic);
        etLocation = (EditText) findViewById(R.id.location);
        etStartTime = (EditText) findViewById(R.id.startTime);
        etEndTime = (EditText) findViewById(R.id.endTime);

        mImage = (ImageView) findViewById(R.id.eventImage);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String den = RandomString.generate(10);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(currentUid).child("Events").child(den).child("Profile");

                mStorageRef = FirebaseStorage.getInstance().getReference();

                final String year = etYear.getText().toString();
                final String month = etMonth.getText().toString();
                final String day = etDay.getText().toString();
                final String topic = etTopic.getText().toString();
                final String location = etLocation.getText().toString();
                final String startTime = etStartTime.getText().toString();
                final String endTime = etEndTime.getText().toString();
                HashMap<String, Object> datas = new HashMap<String, Object>();
                datas.put("Topic" ,topic);
                datas.put("Location" , location);
                datas.put("Month" , month);
                datas.put("Day" ,day);
                datas.put("Year" , year);
                datas.put("Start Time" , startTime);
                datas.put("End Time", endTime);
                datas.put("Passed" , false);
                datas.put("Active" , true);

                ref.setValue(datas);
                uploadImage(den);
                finish();
                }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



    private void uploadImage(String eventID) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");

            StorageReference ref = storageReference.child("images/" + eventID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
    }
}


class RandomString {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates random string of given length from Base65 alphabet (numbers, lowercase letters, uppercase letters).
     * @param count length
     * @return random string of given length
     */
    public static String generate(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
