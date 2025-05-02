package com.example.healthup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthup.Contacts.ContactsActivity;
import com.example.healthup.Locations.LocationsActivity;
import com.example.healthup.MemoryDAO.MemoryInitializer;
import com.example.healthup.dao.Initializer;

public class MainMenuActivity extends AppCompatActivity {

    private Button sos_btn;
    private Button returnHome_btn;
    private Button contacts_btn;
    private Button locations_btn;
    private Button pills_btn;
    private Button profile_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        sos_btn=findViewById(R.id.menu_sos_btn);
        returnHome_btn=findViewById(R.id.menu_returnhome_btn);
        contacts_btn=findViewById(R.id.menu_contacts_btn);
        locations_btn=findViewById(R.id.menu_locations_btn);
        pills_btn=findViewById(R.id.menu_pills_btn);
        profile_btn=findViewById(R.id.menu_profile_btn);

        Initializer initializer = new MemoryInitializer();
        initializer.prepareData();

        sos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        returnHome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ContactsActivity.class);
                startActivity(intent);
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

            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}