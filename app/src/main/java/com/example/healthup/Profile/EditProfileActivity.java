package com.example.healthup.Profile;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Location;
import com.example.healthup.domain.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameOfEdt;
    private EditText surnameOfEdt;
    private EditText addressOfEdt;
    private EditText cityOfEdt;
    private EditText zipOfEdt;

    private Spinner bloodTypeSpinner;
    private Spinner rhFactorSpinner;
    private UserDAO userDAO;
    private User user;
    private Button saveBtn;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] bloodTypes = {"Τύπος","A", "B", "AB", "O"};
        String[] rhFactors = {"Rh","+", "-"};

        nameOfEdt = findViewById(R.id.editProfile_name_edt);
        surnameOfEdt = findViewById(R.id.editProfile_surname_edt);
        addressOfEdt = findViewById(R.id.editProfile_address_edt);
        cityOfEdt = findViewById(R.id.editProfile_city_edt);
        zipOfEdt = findViewById(R.id.editProfile_zip_edt);
        saveBtn = findViewById(R.id.editProfile_save_btn);
        bloodTypeSpinner = findViewById(R.id.editProfile_spinnerBloodType);
        rhFactorSpinner = findViewById(R.id.editProfile_spinnerRhFactor);

        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(bloodTypeAdapter);

        ArrayAdapter<String> rhFactorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rhFactors);
        rhFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rhFactorSpinner.setAdapter(rhFactorAdapter);

        userDAO = new UserMemoryDAO();
        user = userDAO.getUser();

        nameOfEdt.setText(user.getName());
        surnameOfEdt.setText(user.getSurname());
        addressOfEdt.setText(user.getAddress());
        cityOfEdt.setText(user.getCity());
        zipOfEdt.setText(user.getZipcode());

        bloodTypeSpinner.setSelection(Arrays.asList(bloodTypes).indexOf(user.getBloodType()));
        rhFactorSpinner.setSelection(Arrays.asList(rhFactors).indexOf(user.getBloodRhFactor()));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fieldsError = true;

                String name = nameOfEdt.getText().toString();
                String surname = surnameOfEdt.getText().toString();
                String address = addressOfEdt.getText().toString();
                String city = cityOfEdt.getText().toString();
                String zipcode = zipOfEdt.getText().toString();
                String bloodType = bloodTypeSpinner.getSelectedItem().toString();
                String bloodRhFactor = rhFactorSpinner.getSelectedItem().toString();

                if (name.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε το όνομα σας.", Toast.LENGTH_SHORT).show();
                } else if (surname.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε το επώνυμο σας.", Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε τη διεύθυνση σας.", Toast.LENGTH_SHORT).show();
                } else if (city.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε τη πόλη σας.", Toast.LENGTH_SHORT).show();
                } else if (zipcode.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε το ταχ. κωδ. σας.", Toast.LENGTH_SHORT).show();
                } else if (bloodType.equals("Τύπος")) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε τον τύπο αίματος σας.", Toast.LENGTH_SHORT).show();
                } else if (bloodRhFactor.equals("Rh")) {
                    Toast.makeText(EditProfileActivity.this, "Παρακαλώ συμπληρώστε τον παράγοντα Rh του αίματος σας.", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
                    String addressStr = address + ", " + city + ", " + zipcode;

                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address geoAddress = addresses.get(0);
                            double latitude = geoAddress.getLatitude();
                            double longitude = geoAddress.getLongitude();

                            location = new Location("Σπίτι", latitude, longitude, address, city, zipcode);
                            fieldsError = false;
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Δεν βρέθηκε η τοποθεσία. Βεβαιωθείτε ότι εισάγατε σωστά τα στοιχεία της τοποθεσίας!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(!fieldsError) {
                    User editedUser = new User(name,surname,bloodType,bloodRhFactor,address,city,zipcode);
                    userDAO.editUser(editedUser);
                    LocationDAO locationDAO = new LocationMemoryDAO();
                    Location oldLocation = locationDAO.findById(1);
                    locationDAO.editLocation(oldLocation, location);
                    Toast.makeText(EditProfileActivity.this,"Το προφίλ σας ενημερώθηκε με επιτυχία!", Toast.LENGTH_LONG).show();
                    finish();
                }




            }



        });
    }
}