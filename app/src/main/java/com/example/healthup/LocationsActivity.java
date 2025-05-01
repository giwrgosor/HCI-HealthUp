package com.example.healthup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.domain.Location;

import java.util.ArrayList;


public class LocationsActivity extends AppCompatActivity {

    private ListView locationsListView;
    private ImageView homeFromLocations;
    private LocationsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_locations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        locationsListView = (ListView) findViewById(R.id.locationsListView);
        homeFromLocations = findViewById(R.id.homeButtonLocations);

        homeFromLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            }
        });


        ArrayList<Location> locations = new ArrayList<>();
        Location loc = new Location("Test",37.8787182,23.7559329,"Test","test","test");
        Location loc1 = new Location("Test1",10,15,"Test","test","test");
        Location loc2 = new Location("Test2",10,15,"Test","test","test");
        locations.add(loc);
        locations.add(loc1);
        locations.add(loc2);
        adapter = new LocationsListViewAdapter(getLayoutInflater(),locations, this);
        locationsListView.setAdapter(adapter);



    }
}