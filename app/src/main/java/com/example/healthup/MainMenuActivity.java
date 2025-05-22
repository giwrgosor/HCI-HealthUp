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
import com.example.healthup.Pills.PillScheduleActivity;
import com.example.healthup.Profile.DisplayProfileActivity;
import com.example.healthup.Sos.EmergencySelectionActivity;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.FetchWeatherForecast;

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

        ImageButton menuBtn = findViewById(R.id.mainMenuButton);
        ImageButton voiceBtn = findViewById(R.id.voiceRecMain);

        int blackColor = ContextCompat.getColor(this, android.R.color.black);
        menuBtn.setColorFilter(blackColor, PorterDuff.Mode.SRC_IN);
        voiceBtn.setColorFilter(blackColor, PorterDuff.Mode.SRC_IN);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            int whiteColor = getResources().getColor(android.R.color.white);

            TextView dateText = findViewById(R.id.date_text);
            TextView weatherTemp = findViewById(R.id.weather_temperature);
            dateText.setTextColor(whiteColor);
            weatherTemp.setTextColor(whiteColor);

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

            menuBtn.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
            voiceBtn.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        menuButton = findViewById(R.id.mainMenuButton);
        menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainMenuActivity.this, menuButton);
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

        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
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
                weatherTempText.setText("Δεν βρέθηκε τοποθεσία.");
            }

        } catch (SecurityException e) {
            Toast.makeText(this, "Δεν δώσατε άδεια τοποθεσίας.", Toast.LENGTH_SHORT).show();
        }


        TextView dateText = findViewById(R.id.date_text);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM", new Locale("el", "GR"));
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
                LocationDAO locationDAO = new LocationMemoryDAO();
                com.example.healthup.domain.Location location = locationDAO.findById(1);
                String uri = "https://www.google.com/maps/dir/?api=1"
                        + "&destination=" + location.getLat() + "," + location.getLon()
                        + "&travelmode=driving";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Ensure it opens in Google Maps
                Context context = getApplicationContext();
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Το Google Maps δεν είναι εγκατεστημένο", Toast.LENGTH_SHORT).show();
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