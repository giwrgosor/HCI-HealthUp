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

import com.example.healthup.MainActivity;
import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.HttpRequest;
import com.example.healthup.domain.Location;
import com.example.healthup.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditLocationActivity extends AppCompatActivity {
    private EditText name_edt;
    private EditText address_edt;
    private EditText zip_edt;
    private EditText city_edt;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        location = (Location) getIntent().getExtras().get("Location");

        name_edt = findViewById(R.id.editLocationName);
        address_edt = findViewById(R.id.editLocationAddress);
        zip_edt = findViewById(R.id.editLocationZipCode);
        city_edt = findViewById(R.id.editLocationCity);
        ImageButton homeButtonEditLocation = findViewById(R.id.homeButtonEditLocation);

        ImageButton voiceViewLocation_btn = findViewById(R.id.voiceRecEditLocation);

        if(location.getId() == 1){
            name_edt.setFocusable(false);
            name_edt.setClickable(false);
            name_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EditLocationActivity.this,"Only the address of the \"Home\" location can be modified!",Toast.LENGTH_LONG).show();
                }
            });
        }

        name_edt.setText(location.getName());
        address_edt.setText(location.getStreet());
        zip_edt.setText(location.getZipcode());
        city_edt.setText(location.getCity());

        Button save = findViewById(R.id.editLocationSave);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.EditLocationsBackground);
            if (background != null) {
                background.setImageResource(R.drawable.locations_dark_screen);
            }

            int[] textViewIds = {
                    R.id.LocationEditName, R.id.LocationEditAddress, R.id.LocationEditZipCode,
                    R.id.LocationEditCity
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            ImageView icon = findViewById(R.id.editImageView4);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        voiceViewLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What would you like to do?");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        homeButtonEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditLocationActivity.this, MainMenuActivity.class)       ;
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
                    Toast.makeText(EditLocationActivity.this, "Please enter the location name.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Please enter the location address.", Toast.LENGTH_SHORT).show();
                }
                else if(zip.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Please enter the location zip code.", Toast.LENGTH_SHORT).show();
                }
                else if(city.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Please enter the location city.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Geocoder geocoder = new Geocoder(EditLocationActivity.this, Locale.getDefault());
                    String addressStr = address + ", " + city + ", " + zip;
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address geoaddress = addresses.get(0);
                            double latitude = geoaddress.getLatitude();
                            double longitude = geoaddress.getLongitude();

                            locationDAO.editLocation(location,name,latitude,longitude,address,city,zip);
                            if(location.getId()==1){
                                UserDAO userDAO = new UserMemoryDAO();
                                User user = userDAO.getUser();
                                user.setCity(city);
                                user.setAddress(address);
                                user.setZipcode(zip);
                            }
                            Toast.makeText(EditLocationActivity.this, "Location updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditLocationActivity.this,"Location not found. Please ensure the location details are correct!", Toast.LENGTH_SHORT).show();
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
            String url = userDAO.getUrl() + "/editlocation";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);

                            String action = json.getString("action");

                            if (action.equals("edit")) {

                                if (json.has("name")) {
                                    String name = json.getString("name");
                                    if (!name.equals("null") && !name.equals(location.getName())) {
                                        name_edt.setText(name);
                                    }
                                }

                                if (json.has("address")) {
                                    String address = json.getString("address");
                                    if (!address.equals("null") && !address.equals(location.getStreet())) {
                                        address_edt.setText(address);
                                    }
                                }

                                if (json.has("zip")) {
                                    String zip = json.getString("zip");
                                    if (!zip.equals("null") && !zip.equals(location.getZipcode())) {
                                        zip_edt.setText(zip);
                                    }
                                }

                                if (json.has("city")) {
                                    String city = json.getString("city");
                                    if (!city.equals("null") && !city.equals(location.getCity())) {
                                        city_edt.setText(city);
                                    }
                                }
                            }

                            if (action.equals("menu")){
                                Intent intent = new Intent(EditLocationActivity.this, MainMenuActivity.class);
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