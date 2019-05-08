package com.example.bilkentevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button send;
    private Button login;
    private Button signUp;
    private EditText email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        send = findViewById(R.id.sendEmail);
        login = findViewById(R.id.loginForgot);
        signUp = findViewById(R.id.signUpForgot);
        email = findViewById(R.id.resetEmail);
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,RegisterActivity.class));
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpEmail = email.getText().toString().trim();
                if(tmpEmail.equals(""))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter a the email you registered with",Toast.LENGTH_SHORT).show();
                }else
                {
                    firebaseAuth.sendPasswordResetEmail(tmpEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this,"Email has been sent to ypur email to reset your password",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                            }else
                            {
                                Toast.makeText(ForgotPasswordActivity.this,"Error sending the email please try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
