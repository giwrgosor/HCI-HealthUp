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

import com.example.healthup.MainActivity;
import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditLocationActivity extends AppCompatActivity {

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

        Location location = (Location) getIntent().getExtras().get("Location");

        EditText name_edt = findViewById(R.id.editLocationName);
        EditText address_edt = findViewById(R.id.editLocationAddress);
        EditText zip_edt = findViewById(R.id.editLocationZipCode);
        EditText city_edt = findViewById(R.id.editLocationCity);
        ImageButton homeButtonEditLocation = findViewById(R.id.homeButtonEditLocation);

        ImageButton voiceViewLocation_btn = findViewById(R.id.voiceRecEditLocation);

        if(location.getId() == 1){
            name_edt.setFocusable(false);
            name_edt.setClickable(false);
            name_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(EditLocationActivity.this,"Μόνο η διεύθυνση της τοποθεσίας \"Σπίτι\" μπορεί να μεταβληθεί!",Toast.LENGTH_LONG).show();
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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
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
                    Toast.makeText(EditLocationActivity.this, "Παρακαλώ συμπληρώστε το Όνομα της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(address.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Παρακαλώ συμπληρώστε τη Διεύθυνση της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(zip.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Παρακαλώ συμπληρώστε τον Ταχυδρομικό Κώδικα της τοποθεσίας.", Toast.LENGTH_SHORT).show();
                }
                else if(city.isEmpty()){
                    Toast.makeText(EditLocationActivity.this, "Παρακαλώ συμπληρώστε την Πόλη της τοποθεσίας.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditLocationActivity.this, "Η τοποθεσία ενημερώθηκε επιτυχώς!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditLocationActivity.this,"Δεν βρέθηκε η τοποθεσία. Βεβαιωθείτε ότι εισάγατε σωστά τα στοιχεία της τοποθεσίας!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}