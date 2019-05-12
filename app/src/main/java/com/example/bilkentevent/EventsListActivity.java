package com.example.bilkentevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventsListActivity extends AppCompatActivity {

    private Button bAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        bAddEvent = (Button) findViewById(R.id.addButton);


        bAddEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)  {
                Intent intent = new Intent (EventsListActivity.this , AddEventActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }


}
