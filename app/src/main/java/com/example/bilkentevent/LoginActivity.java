package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is the first page user sees. He can 
 */
public class LoginActivity extends AppCompatActivity {

    private Button bLogin;
    private EditText etUsername, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthStateListener;
    private Button signUp;
    private Button eventOrganizer;
    private TextView passwordForgot;
    private RelativeLayout welcomeSign,userInfo;
    private Animation upToDown,downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        fireBaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent (LoginActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        bLogin = (Button) findViewById(R.id.loginButton);
        etUsername = (EditText)findViewById(R.id.email);
        etPassword = (EditText)findViewById(R.id.password);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signUp = findViewById(R.id.signUpButton);
        eventOrganizer = findViewById(R.id.eventOrganizerButton);
        passwordForgot = findViewById(R.id.passwordForgotText);
        welcomeSign = (RelativeLayout)findViewById(R.id.welcomeSignLayout);
        userInfo = (RelativeLayout)findViewById(R.id.userInfoLayout);
        upToDown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downToUp =  AnimationUtils.loadAnimation(this,R.anim.downtoup);
        welcomeSign.setAnimation(upToDown);
        userInfo.setAnimation(downToUp);

        passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });
        eventOrganizer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginActivity.this , ClubRegisterActivity.class);
                startActivity(intent);
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