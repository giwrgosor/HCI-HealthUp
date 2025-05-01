package com.example.healthup.Locations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.R;
import com.example.healthup.domain.Location;

public class ViewLocationActivity extends AppCompatActivity {

    private TextView name_txt;
    private TextView address_txt;
    private TextView zipcode_txt;
    private TextView city_txt;
    private ImageView homeButton;


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
    }
}