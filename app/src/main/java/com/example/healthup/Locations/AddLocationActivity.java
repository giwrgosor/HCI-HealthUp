package com.example.healthup.Locations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity {

    private EditText name_edt;
    private EditText address_edt;
    private EditText zip_edt;
    private EditText city_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_edt = findViewById(R.id.addLocationName);
        address_edt = findViewById(R.id.addLocationAddress);
        zip_edt = findViewById(R.id.addLocationZipCode);
        city_edt = findViewById(R.id.addLocationCity);
        Button save = findViewById(R.id.addLocation_btn);
        ImageButton homeButtonAddLocation = findViewById(R.id.homeButtonAddLocation);
        ImageButton voiceAddLocation_btn = findViewById(R.id.voiceRecAddLocation);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
            String zip = intent.getStringExtra("zip");
            String city = intent.getStringExtra("city");

            if (name != null && !name.equals("null")) {
                name_edt.setText(name);
            }
            if (address != null && !address.equals("null")) {
                address_edt.setText(address);
            }
            if (zip != null && !zip.equals("null")) {
                zip_edt.setText(zip);
            }
            if (city != null && !city.equals("null")) {
                city_edt.setText(city);
            }
        }

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.AddLocationsBackground);
            if (background != null) {
                background.setImageResource(R.drawable.locations_dark_screen);
            }

            int[] textViewIds = {
                    R.id.LocationAddName, R.id.LocationAddAddress, R.id.LocationAddZipCode,
                    R.id.LocationAddCity
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            ImageView icon = findViewById(R.id.addImageView4);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        voiceAddLocation_btn.setOnClickListener(new View.OnClickListener() {
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

        homeButtonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddLocationActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(name_edt.getText());
                String address = String.valueOf(address_edt.getText());
                String zip = String.valueOf(zip_edt.getText());
                String city = String.valueOf(city_edt.getText());
                LocationDAO locationDAO = new LocationMemoryDAO();

                if(name.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Please enter the location name.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Please enter the location address.", Toast.LENGTH_SHORT).show();
                }
                else if(zip.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Please enter the zip code of the location.", Toast.LENGTH_SHORT).show();
                }
                else if(city.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Please enter the city of the location.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Geocoder geocoder = new Geocoder(AddLocationActivity.this, Locale.getDefault());
                    String addressStr = address + ", " + city + ", " + zip;
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address geoaddress = addresses.get(0);
                            double latitude = geoaddress.getLatitude();
                            double longitude = geoaddress.getLongitude();

                            locationDAO.save(new Location(name,latitude,longitude,address,city,zip));
                            Toast.makeText(AddLocationActivity.this, "Location added successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddLocationActivity.this,"Location not found. Please make sure you entered the address correctly!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
            String url = userDAO.getUrl() + "/addlocation";

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
                                if (!name.equals("null")) {
                                    name_edt.setText(name);
                                } else {
                                    name_edt.setText("");
                                }

                                if (!address.equals("null")) {
                                    address_edt.setText(address);
                                } else {
                                    address_edt.setText("");
                                }

                                if (!zip.equals("null")) {
                                    zip_edt.setText(zip);
                                } else {
                                    zip_edt.setText("");
                                }

                                if (!city.equals("null")) {
                                    city_edt.setText(city);
                                } else {
                                    city_edt.setText("");
                                }
                            }

                            if (action.equals("menu")){
                                Intent intent = new Intent(AddLocationActivity.this, MainMenuActivity.class);
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