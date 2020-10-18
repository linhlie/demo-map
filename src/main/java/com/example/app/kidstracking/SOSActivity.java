package com.example.app.kidstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.kidstracking.model.NotiDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class SOSActivity extends AppCompatActivity {

    Button button;

    Button stop;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/Notification");

    DatabaseReference check = database.getReference("server/saving-data/check");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        button = (Button)findViewById(R.id.SOS);

        stop= (Button)findViewById(R.id.Stop_SOS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                ref.child(String.valueOf("sos - "+currentTime)).setValue(new NotiDTO("Nguy hiểm!"));
                check.child(String.valueOf("check")).setValue(true);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                ref.child(String.valueOf("stop - "+currentTime)).setValue(new NotiDTO("An toàn!"));
                check.child(String.valueOf("check")).setValue(false);
            }
        });
    }
}