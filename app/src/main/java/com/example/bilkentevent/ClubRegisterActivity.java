package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClubRegisterActivity extends AppCompatActivity {

    private Button bRegister;
    private EditText etUsername, etPassword, etName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_register);

        mAuth = FirebaseAuth.getInstance();
        fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent (ClubRegisterActivity.this , LoginRegisterActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        bRegister = (Button) findViewById(R.id.register);
        etName = (EditText) findViewById(R.id.name);
        etUsername = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);






        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(ClubRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(ClubRegisterActivity.this, "Sign up error!!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Clubs").child(userId).child("profile");
                            HashMap<String, Object> datas = new HashMap<String, Object>();
                            datas.put("Club Name" ,name);
                            datas.put("Club password" , password);
                            datas.put("Club Email" , name);
                            currentUserDb.setValue(datas);
                            Toast.makeText(ClubRegisterActivity.this, "Sign up is completed, you can login.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireBaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireBaseAuthStateListener);
    }
}
