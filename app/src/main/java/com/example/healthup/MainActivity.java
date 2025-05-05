package com.example.healthup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;

import com.example.healthup.MemoryDAO.MemoryInitializer;
import com.example.healthup.dao.Initializer;

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


    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE
        };

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
                    Toast.makeText(this, "Παρακαλώ, αποδεχτείτε όλους τους όρους για να συνεχίσετε.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    private boolean arePermissionsGranted() {
        String[] permissions = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

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






        String[] bloodTypes = {"Τύπος","A", "B", "AB", "O"};
        String[] rhFactors = {"Rh","+", "-"};


        ArrayAdapter<String> bloodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);
        bloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(bloodTypeAdapter);

        ArrayAdapter<String> rhFactorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rhFactors);
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
            Log.d("MainActivity","Perase tous orismous");
            if (name.isEmpty()) {
                Log.d("MainActivity","Mphke if");
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε το όνομα σας.",Toast.LENGTH_SHORT).show();
            } else if (surname.isEmpty()) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε το επώνυμο σας.",Toast.LENGTH_SHORT).show();
            } else if (address.isEmpty()) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε τη διεύθυνση σας.",Toast.LENGTH_SHORT).show();
            } else if (city.isEmpty()) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε τη πόλη σας.",Toast.LENGTH_SHORT).show();
            } else if (zipcode.isEmpty()) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε το ταχ. κωδ. σας.",Toast.LENGTH_SHORT).show();
            }else if (bloodType.equals("Τύπος")) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε τον τύπο αίματος σας.",Toast.LENGTH_SHORT).show();
            }else if (bloodRhFactor.equals("Rh")) {
                Toast.makeText(MainActivity.this,"Παρακαλώ συμπληρώστε τον παράγοντα Rh του αίματος σας.",Toast.LENGTH_SHORT).show();
            }else{
                fieldsError = false;
            }

            if(!fieldsError) {
                if (!arePermissionsGranted()) {
                    askPermissions();
                } else{
                    Intent intent = new Intent(this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}