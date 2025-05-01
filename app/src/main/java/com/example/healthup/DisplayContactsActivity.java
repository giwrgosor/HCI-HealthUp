package com.example.healthup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DisplayContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        TextView nameTextView = findViewById(R.id.nameDisplayContact);
        TextView phoneTextView = findViewById(R.id.phoneDisplayContact);

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");

        if (name != null) {
            nameTextView.setText("Όνομα: " + name);
        }

        if (phone != null) {
            phoneTextView.setText("Τηλέφωνο: " + phone);
        }

    }
}