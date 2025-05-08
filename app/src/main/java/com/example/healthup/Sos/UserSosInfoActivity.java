package com.example.healthup.Sos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
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
    private String message = "Ενεργοποιήθηκε η λειτουργία έκτακτης ανάγκης για τον χρήστη: ";
    private String numberCalled;

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
        message = message + user.getName() + " " + user.getSurname() + ". Έχει κληθεί το " + numberCalled +". ";
        if (checkPermissions()) {
            getLastLocationAndSendSms();
        } else {
            requestPermissions();
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
                Toast.makeText(this, "Η αποστολή των SMS απέτυχε. Θα πρέπει να επιτρέψετε στην εφαρμογή να στείλει τα απαραίτητα SMS.", Toast.LENGTH_SHORT).show();
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
                message = message + "Η τοποθεσία του χρήστη είναι (Google Maps): https://www.google.com/maps?q="+ lat + ","+ lon;
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Δεν δώσατε άδεια στην εφαρμογή να χρησιμοποιήσει την τοποθεσία σας.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ContactsDAO contactsDAO = new ContactsMemoryDAO();
            ArrayList<Contact> contacts = contactsDAO.findAll();
            ArrayList<String> phoneNumbers = new ArrayList<>();
            for(Contact contact: contacts){
                if(contact.isEmergency()){
                    phoneNumbers.add(contact.getPhone());
                }
            }
            for (String number : phoneNumbers) {
                smsManager.sendTextMessage(number, null, message, null, null);
            }
            Toast.makeText(this, "Στάλθηκαν τα SMS με επιτυχία!", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "Απορρίφθηκε η άδεια να σταλθεί SMS", Toast.LENGTH_SHORT).show();
        }
    }
}