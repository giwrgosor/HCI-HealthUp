package com.example.healthup.Sos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.R;
import com.example.healthup.domain.EmergencyCallTracker;

public class EmergencySelectionActivity extends AppCompatActivity {

    private LinearLayout police_btn;
    private LinearLayout ambulance_btn;
    private LinearLayout fire_btn;
    private EmergencyCallTracker callTracker;
    private ImageButton homeButtonEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.emergencyProfileBackground);
            if (background != null) {
                background.setImageResource(R.drawable.sos_dark_screen);
            }

        }

        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        callTracker = new EmergencyCallTracker(this);
        telephonyManager.listen(callTracker, PhoneStateListener.LISTEN_CALL_STATE);

        police_btn = findViewById(R.id.police_btn);
        ambulance_btn = findViewById(R.id.ambulance_btn);
        fire_btn = findViewById(R.id.firedepartment_btn);
        homeButtonEmergency = findViewById(R.id.homeButtonEmergency);

        police_btn.setOnClickListener(v -> showConfirmationDialog("Αστυνομία","tel:100"));
        ambulance_btn.setOnClickListener(v -> showConfirmationDialog("ΕΚΑΒ","tel:166"));
        fire_btn.setOnClickListener(v -> showConfirmationDialog("Πυροσβεστική","tel:199"));
        homeButtonEmergency.setOnClickListener(v -> startActivity(new Intent(EmergencySelectionActivity.this, MainMenuActivity.class)));
    }

    private void showConfirmationDialog(String serviceName, String phoneNumber) {
        new AlertDialog.Builder(this)
                .setTitle("Επιβεβαίωση Κλήσης")
                .setMessage("Θέλετε σίγουρα να καλέσετε το/την " + serviceName + "?")
                .setPositiveButton("Yes", (dialog, which) -> callEmergency(phoneNumber))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void callEmergency(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);
    }
}