package com.example.healthup;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;

import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.MemoryInitializer;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.dao.Initializer;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Location;
import com.example.healthup.domain.User;

import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private Button save_btn;
    private EditText startZip_edt;
    private EditText startCity_edt;
    private EditText startAddress_edt;
    private EditText startSurname_edt;
    private EditText startName_edt;
    private Spinner spinnerBloodType;
    private Spinner spinnerRhFactor;
    private String surname;
    private String name;
    private String address;
    private String city;
    private String zipcode;
    private String bloodType;
    private String bloodRhFactor;
    private UserDAO userDAO;
    private User user;
    private LocationDAO locationDAO;
    private Location location;
    private ImageButton menuButton;
    private String mAnswer;

    String[] permissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private void askPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please give all the permissions to the app in order to continue.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    private boolean arePermissionsGranted() {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void applyColorBlindTheme() {
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putString("theme", "colorblind")
                .apply();

        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuButton = findViewById(R.id.menuButton);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            ImageView background = findViewById(R.id.startScreenBackground);
            background.setImageResource(R.drawable.blackbackground);

            int whiteColor = ContextCompat.getColor(this, android.R.color.white);

            int[] textViewIds = {
                    R.id.startScreenTextName, R.id.startTextSurname, R.id.startTextAddress,
                    R.id.startTextCity, R.id.startTextZip, R.id.startScreenBlood
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            ImageView icon = findViewById(R.id.startScreenImg);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);

            // Εδώ το σωστό reference
            menuButton.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }



        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuButton);
            popupMenu.getMenuInflater().inflate(R.menu.settings_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.light_mode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    return true;
                } else if (id == R.id.dark_mode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        Initializer initializer = new MemoryInitializer();
        initializer.prepareData();

        save_btn = findViewById(R.id.main_save_btn);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        spinnerRhFactor = findViewById(R.id.spinnerRhFactor);
        startZip_edt = findViewById(R.id.startZip_edt);
        startCity_edt = findViewById(R.id.startCity_edt);
        startSurname_edt = findViewById(R.id.startSurname_edt);
        startAddress_edt = findViewById(R.id.startAddress_edt);
        startName_edt = findViewById(R.id.startName_edt);

        String[] bloodTypes = {"Type","A", "B", "AB", "O"};
        String[] rhFactors = {"Rh","+", "-"};

//        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
//        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerBloodType.setAdapter(bloodTypeAdapter);
//
//        ArrayAdapter<String> rhFactorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rhFactors);
//        rhFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerRhFactor.setAdapter(rhFactorAdapter);

        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                bloodTypes
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setTextColor(Color.BLACK);
                return view;
            }
        };
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(bloodTypeAdapter);


        ArrayAdapter<String> rhFactorAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                rhFactors
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setTextColor(Color.BLACK);
                return view;
            }
        };
        rhFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRhFactor.setAdapter(rhFactorAdapter);



        save_btn.setOnClickListener(view -> {
            boolean fieldsError = true;

            name = startName_edt.getText().toString();
            surname = startSurname_edt.getText().toString();
            address = startAddress_edt.getText().toString();
            city = startCity_edt.getText().toString();
            zipcode = startZip_edt.getText().toString();
            bloodType = spinnerBloodType.getSelectedItem().toString();
            bloodRhFactor = spinnerRhFactor.getSelectedItem().toString();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter your name.",Toast.LENGTH_SHORT).show();
            } else if (surname.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter your surname.",Toast.LENGTH_SHORT).show();
            } else if (address.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter your address.",Toast.LENGTH_SHORT).show();
            } else if (city.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter the city of your home.",Toast.LENGTH_SHORT).show();
            } else if (zipcode.isEmpty()) {
                Toast.makeText(MainActivity.this,"Please enter your zipcode.",Toast.LENGTH_SHORT).show();
            }else if (bloodType.equals("Type")) {
                Toast.makeText(MainActivity.this,"Please enter your blood type.",Toast.LENGTH_SHORT).show();
            }else if (bloodRhFactor.equals("Rh")) {
                Toast.makeText(MainActivity.this,"Please enter the Rh team of your blood type.",Toast.LENGTH_SHORT).show();
            }else{
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                String addressStr = address + ", " + city + ", " + zipcode;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address geoaddress = addresses.get(0);
                        double latitude = geoaddress.getLatitude();
                        double longitude = geoaddress.getLongitude();
                        location = new Location("Home",latitude,longitude,address,city,zipcode);
                        fieldsError = false;
                    } else {
                        Toast.makeText(MainActivity.this,"We could not find your location. Please make sure that you have entered the correct address!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(!fieldsError) {
                if (!arePermissionsGranted()) {
                    askPermissions();
                } else{
                    user = new User(name,surname,bloodType,bloodRhFactor,address,city,zipcode);
                    userDAO = new UserMemoryDAO();
                    userDAO.editUser(user);
                    locationDAO = new LocationMemoryDAO();
                    locationDAO.save(location);
                    Intent intent = new Intent(this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data); Log.i("DEMO-REQUESTCODE",
                Integer.toString(requestCode)); Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null){
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mAnswer = text.get(0);
            Log.i("DEMO-ANSWER", text.get(0));


        }
        else{
            System.out.println("Recognizer API error");
        }
    }
}