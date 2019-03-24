package com.example.bilkentevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button bRegister, logina, bClubLogin ,bClubRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        logina = (Button) findViewById(R.id.login);
        bRegister = (Button) findViewById(R.id.register);
        bClubLogin = (Button) findViewById(R.id.clubLogin);
        bClubRegister = (Button) findViewById(R.id.clubRegister);

        logina.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginRegisterActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginRegisterActivity.this , RegisterActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        bClubLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginRegisterActivity.this , AdminChecker.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        bClubRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (LoginRegisterActivity.this , AdminChecker.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
