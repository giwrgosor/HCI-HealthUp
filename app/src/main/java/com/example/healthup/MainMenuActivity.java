package com.example.healthup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.location.Location;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.Contacts.ContactsActivity;
import com.example.healthup.Locations.LocationsActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.Pills.PillScheduleActivity;
import com.example.healthup.Profile.DisplayProfileActivity;
import com.example.healthup.Sos.EmergencySelectionActivity;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.FetchWeatherForecast;
import com.example.healthup.domain.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMenuActivity extends AppCompatActivity {

    private FrameLayout sos_btn;
    private FrameLayout returnHome_btn;
    private FrameLayout locations_btn;
    private FrameLayout pills_btn;
    private FrameLayout profile_btn;
    private FrameLayout contacts_btn;
    private TextView weatherTempText;
    private ImageView weatherIcon;
    private ImageButton voice_btn;
    private ImageButton menuButton;
    private String mAnswer;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contacts_btn=findViewById(R.id.menu_contacts_btn);
        weatherIcon = findViewById(R.id.weather_icon);
        weatherTempText = findViewById(R.id.weather_temperature);
        voice_btn = findViewById(R.id.voiceRecMain);
        menuButton = findViewById(R.id.mainMenuButton);

        int blackColor = ContextCompat.getColor(this, android.R.color.black);
        menuButton.setColorFilter(blackColor, PorterDuff.Mode.SRC_IN);
        voice_btn.setColorFilter(blackColor, PorterDuff.Mode.SRC_IN);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            int whiteColor = getResources().getColor(android.R.color.white);

            TextView dateText = findViewById(R.id.date_text);
            dateText.setTextColor(whiteColor);
            weatherTempText.setTextColor(whiteColor);

            ImageView background = findViewById(R.id.background);
            if (background != null) {
                background.setImageResource(R.drawable.basic_dark_screen);
            }

            int[] textViewIds = {
                    R.id.callTxt, R.id.homeTxt, R.id.mapTxt,
                    R.id.contactTxt, R.id.pillTxt, R.id.profilTxt
            };


            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            int[] lineIds = {
                    R.id.line1, R.id.line2, R.id.line3
            };

            for (int id : lineIds) {
                View line = findViewById(id);
                if (line != null) {
                    line.setBackgroundColor(whiteColor);
                }
            }

            menuButton.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
            voice_btn.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainMenuActivity.this, this.menuButton);
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

        this.voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What would you like to do?");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });


        contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        sos_btn=findViewById(R.id.menu_sos_btn);
        returnHome_btn=findViewById(R.id.menu_returnhome_btn);
        contacts_btn=findViewById(R.id.menu_contacts_btn);
        locations_btn=findViewById(R.id.menu_locations_btn);
        pills_btn=findViewById(R.id.menu_pills_btn);
        profile_btn=findViewById(R.id.menu_profile_btn);

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

//                FetchWeatherForecast weatherTask = new FetchWeatherForecast(lat, lon, weatherTextView);
//                weatherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "0000");

                new FetchWeatherForecast(lat, lon) {
                    @Override
                    protected void onPostExecute(String[] result) {
                        if (result != null && result.length >= 2) {

                            String regex = "[-]";
                            String description = result[0].toLowerCase(Locale.ROOT);
                            String[] myArray = description.split(regex);
                            String[] weatherArray = new String[myArray.length];

                            for (int i = 0; i < myArray.length; i++) {
                                weatherArray[i] = myArray[i].trim();
                            }


                            weatherTempText.setText(weatherArray[2]);

                            if (weatherArray[1].contains("cloud")) {
                                weatherIcon.setImageResource(R.drawable.ic_cloudy);
                            } else if (weatherArray[1].contains("thunderstorm")){
                                weatherIcon.setImageResource(R.drawable.ic_thunderstorm);
                            } else if (weatherArray[1].contains("snow")){
                                weatherIcon.setImageResource(R.drawable.ic_snow);
                            } else if (weatherArray[1].contains("drizzle")){
                                weatherIcon.setImageResource(R.drawable.ic_drizzle);
                            } else if (weatherArray[1].contains("sun") || description.contains("clear")) {
                                weatherIcon.setImageResource(R.drawable.ic_sunny);
                            } else if (weatherArray[1].contains("rain")) {
                                weatherIcon.setImageResource(R.drawable.ic_rainy);
                            } else {
                                weatherIcon.setImageResource(R.drawable.ic_weather_default);
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "0000");


            } else {
                weatherTempText.setText("Location not found.");
            }

        } catch (SecurityException e) {
            Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_SHORT).show();
        }


        TextView dateText = findViewById(R.id.date_text);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM", Locale.ENGLISH);
        String currentDateAndTime = sdf.format(new Date());
        dateText.setText(currentDateAndTime);

        sos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, EmergencySelectionActivity.class);
                startActivity(intent);
            }
        });

        returnHome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This opens Google maps app only
                LocationDAO locationDAO = new LocationMemoryDAO();
                com.example.healthup.domain.Location location = locationDAO.findById(1);
                String uri = "https://www.google.com/maps/dir/?api=1"
                        + "&destination=" + location.getLat() + "," + location.getLon()
                        + "&travelmode=driving";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                Context context = getApplicationContext();
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    //
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    view.getContext().startActivity(intent);
                }
            }
        });

        locations_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
                startActivity(intent);
            }
        });

        pills_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PillScheduleActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, DisplayProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void applyColorBlindTheme() {
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putString("theme", "colorblind")
                .apply();

        recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/mainscreen";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String action = json.getString("action");

                            if (action.equals("sos")) {
                                Intent intent = new Intent(MainMenuActivity.this, EmergencySelectionActivity.class);
                                startActivity(intent);
                            } else if (action.equals("return_home")) {
                                LocationDAO locationDAO = new LocationMemoryDAO();
                                com.example.healthup.domain.Location location = locationDAO.findById(1);
                                String uri = "https://www.google.com/maps/dir/?api=1"
                                        + "&destination=" + location.getLat() + "," + location.getLon()
                                        + "&travelmode=driving";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                intent.setPackage("com.google.android.apps.maps");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainMenuActivity.this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
                                }
                            } else if (action.equals("locations")) {
                                Intent intent = new Intent(MainMenuActivity.this, LocationsActivity.class);
                                startActivity(intent);
                            } else if (action.equals("contacts")) {
                                Intent intent = new Intent(MainMenuActivity.this, ContactsActivity.class);
                                startActivity(intent);
                            } else if (action.equals("pills")) {
                                Intent intent = new Intent(MainMenuActivity.this, PillScheduleActivity.class);
                                startActivity(intent);
                            } else if (action.equals("profile")) {
                                Intent intent = new Intent(MainMenuActivity.this, DisplayProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainMenuActivity.this, "Command not recognized.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("HTTP_RESPONSE", "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("HTTP_ERROR", error);
                        Toast.makeText(MainMenuActivity.this, "Error sending request", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(MainMenuActivity.this, "Voice recognition error", Toast.LENGTH_SHORT).show();
        }
    }

}