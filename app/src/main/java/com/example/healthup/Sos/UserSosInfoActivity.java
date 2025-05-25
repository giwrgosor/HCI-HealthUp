package com.example.healthup.Sos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.MemoryDAO.PillsMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Contact;
import com.example.healthup.domain.Pill;
import com.example.healthup.domain.User;

import java.text.MessageFormat;
import java.util.ArrayList;

public class UserSosInfoActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private String message = "Emergency mode enabled for the user: ";
    private String numberCalled;
    private ImageButton homeButtonEmergencyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_sos_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserDAO userDAO = new UserMemoryDAO();
        User user = userDAO.getUser();
        ((TextView) findViewById(R.id.emergenyNameSurname_txt)).setText(MessageFormat.format("{0} {1}", user.getName(), user.getSurname()));
        ((TextView) findViewById(R.id.emergencyAddress_txt)).setText(user.getAddress());
        ((TextView) findViewById(R.id.emergencyCityZipcode_txt)).setText(String.format("%s, %s", user.getCity(), user.getZipcode()));
        homeButtonEmergencyProfile = findViewById(R.id.homeButtonEmergencyProfile);

        homeButtonEmergencyProfile.setOnClickListener(v -> startActivity(new Intent(UserSosInfoActivity.this, MainMenuActivity.class)));

        PillsDAO pillsDAO = new PillsMemoryDAO();
        ArrayList<Pill> pills = pillsDAO.findAll();
        ArrayList<Pill> filteredPills = new ArrayList<>();
        for(Pill pill : pills){
            if(pill.getTimesPerWeek() != 0){
                filteredPills.add(pill);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.pillListRecyclerView);
        TextView recyclerTitle = findViewById(R.id.pillsTitle);

        if (filteredPills.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            recyclerTitle.setVisibility(View.GONE);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            EmergencyPillAdapter adapter = new EmergencyPillAdapter(filteredPills);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerTitle.setVisibility(View.VISIBLE);
        }

        numberCalled = getIntent().getStringExtra("PhoneNum");
        message = message + user.getName() + " " + user.getSurname() + ". The" + numberCalled +" has been called to his location. ";
        if (checkPermissions()) {
            getLastLocationAndSendSms();
        } else {
            requestPermissions();
        }

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.emergencyProfileBackground);
            if (background != null) {
                background.setImageResource(R.drawable.sos_dark_screen);
            }

            int[] textViewIds = {
                    R.id.emergenyNameSurname, R.id.emergencyAddress, R.id.emergencyCity, R.id.pillsTitle
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            ImageView icon = findViewById(R.id.emergencyProfileImg);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

    }

    private boolean checkPermissions() {
        boolean isSmsGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        boolean isLocationGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return isSmsGranted && isLocationGranted;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLastLocationAndSendSms();
            } else {
                Toast.makeText(this, "Failed to sent the SMS. You must allow the app to send SMS by accepting the permission message.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocationAndSendSms() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                message = message + "The users location is (Google Maps): https://www.google.com/maps?q="+ lat + ","+ lon;
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "You did not give permission to access your location", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ContactsDAO contactsDAO = new ContactsMemoryDAO();
            ArrayList<Contact> contacts = contactsDAO.findAll();
            ArrayList<String> phoneNumbers = new ArrayList<>();
            ArrayList<String> parts = smsManager.divideMessage(message);
            for(Contact contact: contacts){
                if(contact.isEmergency()){
                    phoneNumbers.add("+30" + contact.getPhone());
                }
            }
            for (String number : phoneNumbers) {
                smsManager.sendMultipartTextMessage(number, null, parts, null, null);            }
            Toast.makeText(this, "The SMS were send with success!", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission to send SMS was denied.", Toast.LENGTH_SHORT).show();
        }
    }
}