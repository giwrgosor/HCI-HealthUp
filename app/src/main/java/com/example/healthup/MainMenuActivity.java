package com.example.healthup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthup.Contacts.ContactsActivity;
import com.example.healthup.Locations.LocationsActivity;
import com.example.healthup.MemoryDAO.MemoryInitializer;
import com.example.healthup.Pills.DisplayPillsActivity;
import com.example.healthup.Pills.PillScheduleActivity;
import com.example.healthup.Profile.DisplayProfileActivity;
import com.example.healthup.Sos.EmergencySelectionActivity;
import com.example.healthup.dao.Initializer;

public class MainMenuActivity extends AppCompatActivity {

    private FrameLayout sos_btn;
    private FrameLayout returnHome_btn;
    private FrameLayout locations_btn;
    private FrameLayout pills_btn;
    private FrameLayout profile_btn;
    private FrameLayout contacts_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        contacts_btn=findViewById(R.id.menu_contacts_btn);

        contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        sos_btn=findViewById(R.id.menu_sos_btn);
        returnHome_btn=findViewById(R.id.menu_returnhome_btn);
        contacts_btn=findViewById(R.id.menu_contacts_btn);
        locations_btn=findViewById(R.id.menu_locations_btn);
        pills_btn=findViewById(R.id.menu_pills_btn);
        profile_btn=findViewById(R.id.menu_profile_btn);

        sos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, EmergencySelectionActivity.class);
                startActivity(intent);
            }
        });

        returnHome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        locations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
                startActivity(intent);
            }
        });

        pills_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PillScheduleActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, DisplayProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}