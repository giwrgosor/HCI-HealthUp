package com.example.healthup.Locations;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        EditText name_edt = findViewById(R.id.EditLocationDisplayName);
        EditText address_edt = findViewById(R.id.EditLocationDisplayAddress);
        EditText zip_edt = findViewById(R.id.EditLocationDisplayZipCode);
        EditText city_edt = findViewById(R.id.EditLocationDisplayCity);

        name_edt.setText(location.getName());
        address_edt.setText(location.getStreet());
        zip_edt.setText(location.getZipcode());
        city_edt.setText(location.getCity());

        Button save = findViewById(R.id.editLocationSave);

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

                            Location newLocation = new Location(name,latitude,longitude,address,city,zip);

                            locationDAO.editLocation(location,newLocation);
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