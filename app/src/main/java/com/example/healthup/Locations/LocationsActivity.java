package com.example.healthup.Locations;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class LocationsActivity extends AppCompatActivity {

    private ListView locationsListView;
    private ImageButton homeFromLocations;
    private LocationsListViewAdapter adapter;
    private ImageButton voiceLocations_btn;

    @Override
    protected void onResume() {
        super.onResume();
        LocationDAO locationDAO = new LocationMemoryDAO();
        locationsListView.setAdapter(new LocationsListViewAdapter(getLayoutInflater(),locationDAO.findAll(),this));
    }

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
        voiceLocations_btn = findViewById(R.id.voiceRecLocations);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            ImageView background = findViewById(R.id.locationsBackground);
            if (background != null) {
                background.setImageResource(R.drawable.locations_dark_screen);
            }
        }

        voiceLocations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        homeFromLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton addLocation_btn = findViewById(R.id.addLocation);

        addLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                startActivity(intent);
            }
        });


        LocationDAO locationDAO = new LocationMemoryDAO() ;
        adapter = new LocationsListViewAdapter(getLayoutInflater(),locationDAO.findAll(), this);
        locationsListView.setAdapter(adapter);

    }
}