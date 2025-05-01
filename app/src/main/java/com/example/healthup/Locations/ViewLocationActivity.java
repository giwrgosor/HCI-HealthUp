package com.example.healthup.Locations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewLocationActivity extends AppCompatActivity {

    private TextView name_txt;
    private TextView address_txt;
    private TextView zipcode_txt;
    private TextView city_txt;
    private ImageView homeButton;
    private Context context = this;


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

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            }
        });

        Location location = (Location) getIntent().getExtras().get("Location");

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
                new AlertDialog.Builder(context)
                        .setTitle("Επιβεβαίωση Διαγραφής")
                        .setMessage("Θέλετε σίγουρα να διαγράψετε την τοποθεσία " + location.getName() + "?")
                        .setPositiveButton("Ναι", (dialog, which) -> {
                            LocationDAO locationDAO = new LocationMemoryDAO();
                            Log.d("ViewLocationActivity","Prin to delete exei: " + locationDAO.findAll().size());
                            locationDAO.delete(location);
                            Log.d("ViewLocationActivity","Meta to delete exei: " + locationDAO.findAll().size());
                            Toast.makeText(context, "Η τοποθεσία " + location.getName() + " διαγράφηκε!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, LocationsActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Όχι", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

    }
}