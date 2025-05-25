package com.example.healthup.Locations;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.HttpRequest;
import com.example.healthup.domain.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewLocationActivity extends AppCompatActivity {

    private TextView name_txt;
    private TextView address_txt;
    private TextView zipcode_txt;
    private TextView city_txt;
    private ImageButton homeButton;
    private Location location;
    private Context context = this;
    private ImageButton voiceViewLocation_btn;


    @Override
    protected void onResume() {
        super.onResume();
        LocationDAO locationDAO = new LocationMemoryDAO();
        Location locationNew = locationDAO.findById(location.getId());
        name_txt.setText(locationNew.getName());
        address_txt.setText(locationNew.getStreet());
        zipcode_txt.setText(locationNew.getZipcode());
        city_txt.setText(locationNew.getCity());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_txt = findViewById(R.id.ViewLocationDisplayName);
        address_txt = findViewById(R.id.ViewLocationDisplayAddress);
        zipcode_txt = findViewById(R.id.ViewLocationDisplayZipCode);
        city_txt = findViewById(R.id.ViewLocationDisplayCity);
        homeButton = findViewById(R.id.homeButtonViewLocation);

        voiceViewLocation_btn = findViewById(R.id.voiceRecViewLocation);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.ViewLocationsBackground);
            if (background != null) {
                background.setImageResource(R.drawable.locations_dark_screen);
            }

            int[] textViewIds = {
                    R.id.LocationViewName, R.id.LocationViewAddress, R.id.LocationViewZipCode,
                    R.id.LocationViewCity
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            ImageView icon = findViewById(R.id.imageView4);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }


        voiceViewLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            }
        });

        location = (Location) getIntent().getExtras().get("Location");

        if (location == null) {
            Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        name_txt.setText(location.getName());
        address_txt.setText(location.getStreet());
        zipcode_txt.setText(location.getZipcode());
        city_txt.setText(location.getCity());

        FloatingActionButton directions_btn = findViewById(R.id.directionsLocation_btn);
        FloatingActionButton delete_btn = findViewById(R.id.deleteLocation_btn);
        FloatingActionButton edit_btn = findViewById(R.id.editLocation_btn);

        directions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/dir/?api=1"
                        + "&destination=" + location.getLat() + "," + location.getLon()
                        + "&travelmode=driving";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Ensure it opens in Google Maps

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Το Google Maps δεν είναι εγκατεστημένο", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(location.getId()==1)) {
                    new AlertDialog.Builder(context)
                            .setTitle("Επιβεβαίωση Διαγραφής")
                            .setMessage("Θέλετε σίγουρα να διαγράψετε την τοποθεσία " + location.getName() + "?")
                            .setPositiveButton("Ναι", (dialog, which) -> {
                                LocationDAO locationDAO = new LocationMemoryDAO();
                                locationDAO.delete(location);
                                Toast.makeText(context, "Η τοποθεσία " + location.getName() + " διαγράφηκε!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                            .show();
                }else{
                    Toast.makeText(context, "Δεν επιτρέπεται η διαγραφή της τοποθεσίας \"ΣΠΙΤΙ\", αλλά μόνο η επεξεργασία της!", Toast.LENGTH_LONG).show();
                }
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditLocationActivity.class);
                intent.putExtra("Location",location);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/viewlocation";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String action = json.getString("action");

                            if (action.equals("edit")) {
                                Intent intent = new Intent(ViewLocationActivity.this, EditLocationActivity.class);
                                intent.putExtra("Location", location);
                                startActivity(intent);
                            } else if (action.equals("goto")) {
                                String uri = "https://www.google.com/maps/dir/?api=1"
                                        + "&destination=" + location.getLat() + "," + location.getLon()
                                        + "&travelmode=driving";

                                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                mapsIntent.setPackage("com.google.android.apps.maps");

                                if (mapsIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapsIntent);
                                } else {
                                    Toast.makeText(context, "Το Google Maps δεν είναι εγκατεστημένο", Toast.LENGTH_SHORT).show();
                                }
                            } else if (action.equals("delete")) {
                                if (location.getId() == 1) {
                                    Toast.makeText(context, "Δεν επιτρέπεται η διαγραφή της τοποθεσίας \"ΣΠΙΤΙ\", αλλά μόνο η επεξεργασία της!", Toast.LENGTH_LONG).show();
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Επιβεβαίωση Διαγραφής")
                                            .setMessage("Θέλετε σίγουρα να διαγράψετε την τοποθεσία " + location.getName() + ";")
                                            .setPositiveButton("Ναι", (dialog, which) -> {
                                                LocationDAO locationDAO = new LocationMemoryDAO();
                                                locationDAO.delete(location);
                                                Toast.makeText(context, "Η τοποθεσία διαγράφηκε!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            })
                                            .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                                            .show();
                                }
                            } else if (action.equals("menu")) {
                                Intent intent = new Intent(ViewLocationActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(context, "Δεν αναγνωρίστηκε ενέργεια.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("HTTP_RESPONSE", "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("HTTP_ERROR", error);
                        Toast.makeText(context, "Σφάλμα κατά την αποστολή του αιτήματος", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Σφάλμα αναγνώρισης φωνής", Toast.LENGTH_SHORT).show();
        }
    }


}