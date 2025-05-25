package com.example.healthup.Sos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.EmergencyCallTracker;
import com.example.healthup.domain.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmergencySelectionActivity extends AppCompatActivity {

    private LinearLayout police_btn;
    private LinearLayout ambulance_btn;
    private LinearLayout fire_btn;
    private EmergencyCallTracker callTracker;
    private ImageButton homeButtonEmergency;
    private ImageButton voiceEmergencySelection_btn;
    private String voiceText;

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

        voiceEmergencySelection_btn = findViewById(R.id.voiceRecEmergencySelection);

        police_btn.setOnClickListener(v -> showConfirmationDialog("Police","tel:100"));
        ambulance_btn.setOnClickListener(v -> showConfirmationDialog("Ambulance","tel:166"));
        fire_btn.setOnClickListener(v -> showConfirmationDialog("Fire Department","tel:199"));
        homeButtonEmergency.setOnClickListener(v -> startActivity(new Intent(EmergencySelectionActivity.this, MainMenuActivity.class)));

        voiceEmergencySelection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please tell us which service you would like to call.");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

    }

    private void showConfirmationDialog(String serviceName, String phoneNumber) {
        new AlertDialog.Builder(this)
                .setTitle("Call Confirmation")
                .setMessage("Are you sure you want to call the " + serviceName + "?")
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DEMO-REQUESTCODE",
                Integer.toString(requestCode));
        Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/soscall";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String service = json.getString("service");
                            service = service.equals("null") ? "" : service;
                            if(service.equalsIgnoreCase("ambulance")){
                                callEmergency("tel:166");
                            }else if(service.equalsIgnoreCase("police")){
                                callEmergency("tel:100");
                            }else if(service.equalsIgnoreCase("firedepartment")){
                                callEmergency("tel:199");
                            }else{
                                handleError("We could not understand your request. Please try again.");
                            }
                        } catch (JSONException e) {
                            Log.e("HTTP_RESPONSE", "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error", error);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Recognizer API error");
        }
    }

    public void handleError(String msg){
        Toast.makeText(EmergencySelectionActivity.this,msg,Toast.LENGTH_LONG).show();
    }
}