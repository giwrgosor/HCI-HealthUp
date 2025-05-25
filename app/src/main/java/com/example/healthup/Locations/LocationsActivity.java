package com.example.healthup.Locations;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.HttpRequest;
import com.example.healthup.domain.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What do you want?");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data); Log.i("DEMO-REQUESTCODE",
                Integer.toString(requestCode)); Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));

        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/location";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);

                            String action = json.getString("action");
                            String name = json.getString("name");
                            String address = json.getString("address");
                            String zip = json.getString("zip");
                            String city = json.getString("city");

                            if (action.equals("add")){
                                Intent intent = new Intent(LocationsActivity.this, AddLocationActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("address", address);
                                intent.putExtra("zip", zip);
                                intent.putExtra("city", city);
                                startActivity(intent);
                            }

                            if (action.equals("view")){
                                LocationDAO locationDAO = new LocationMemoryDAO();
                                Location selectedLocation = locationDAO.findByName(name);

                                if (selectedLocation != null) {
                                    Intent intent = new Intent(LocationsActivity.this, ViewLocationActivity.class);
                                    intent.putExtra("Location", selectedLocation);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LocationsActivity.this, "Location not found.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (action.equals("search")){
                                LocationDAO locationDAO = new LocationMemoryDAO();
                                Location location = locationDAO.findByName(name);

                                if (location != null) {
                                    String uri = "https://www.google.com/maps/dir/?api=1"
                                            + "&destination=" + location.getLat() + "," + location.getLon()
                                            + "&travelmode=driving";

                                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    mapsIntent.setPackage("com.google.android.apps.maps");

                                    if (mapsIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(mapsIntent);
                                    } else {
                                        Toast.makeText(LocationsActivity.this, "Το Google Maps δεν είναι εγκατεστημένο", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LocationsActivity.this, "Η τοποθεσία δεν βρέθηκε", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (action.equals("menu")){
                                Intent intent = new Intent(LocationsActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            }


                        } catch (JSONException e) {
                            android.util.Log.e("HTTP_RESPONSE", "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("Error", error);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Recognizer API error");
        }
    }


}