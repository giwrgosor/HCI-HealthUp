package com.example.healthup.Locations;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity {

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

        EditText name_edt = findViewById(R.id.addLocationName);
        EditText address_edt = findViewById(R.id.addLocationAddress);
        EditText zip_edt = findViewById(R.id.addLocationZipCode);
        EditText city_edt = findViewById(R.id.addLocationCity);
        Button save = findViewById(R.id.addLocation_btn);
        ImageButton homeButtonAddLocation = findViewById(R.id.homeButtonAddLocation);
        ImageButton voiceAddLocation_btn = findViewById(R.id.voiceRecAddLocation);

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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
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
                    Toast.makeText(AddLocationActivity.this, "Παρακαλώ συμπληρώστε το Όνομα της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Παρακαλώ συμπληρώστε τη Διεύθυνση της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(zip.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Παρακαλώ συμπληρώστε τον Ταχυδρομικό Κώδικα της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(city.isEmpty()){
                    Toast.makeText(AddLocationActivity.this, "Παρακαλώ συμπληρώστε την Πόλη της τοποθεσίας.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddLocationActivity.this, "Η τοποθεσία προστέθηκε με επιτυχία!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddLocationActivity.this,"Δεν βρέθηκε η τοποθεσία. Βεβαιωθείτε ότι εισάγατε σωστά τα στοιχεία της τοποθεσίας!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}