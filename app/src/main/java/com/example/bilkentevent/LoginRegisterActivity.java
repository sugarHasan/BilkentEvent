package com.example.bilkentevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button bRegister, bLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        bLogin = (Button) findViewById(R.id.login);
        bRegister = (Button) findViewById(R.id.register);

        bLogin.setOnClickListener(new View.OnClickListener(){
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
    }
}
