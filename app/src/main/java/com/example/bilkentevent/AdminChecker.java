package com.example.bilkentevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminChecker extends AppCompatActivity {

    private EditText etAdminTest;
    private Button bCheck;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_checker);


        bCheck = (Button)findViewById(R.id.check);
        etAdminTest = (EditText)findViewById(R.id.adminTest);


        bCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                final String adminTest = etAdminTest.getText().toString();
                if(adminTest.equals("admin123")) {
                    mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        mAuth.signOut();
                        Intent intent = new Intent (AdminChecker.this , MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    else {
                        Intent intent = new Intent(AdminChecker.this, ClubLoginActivity.class);

                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                else{
                    Toast.makeText(AdminChecker.this, "Your admin code is incorrect!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
