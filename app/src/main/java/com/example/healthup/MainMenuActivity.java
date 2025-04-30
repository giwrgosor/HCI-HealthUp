package com.example.healthup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {

    private Button sos_btn;
    private Button returnHome_btn;
    private Button contacts_btn;
    private Button locations_btn;
    private Button pills_btn;
    private Button profile_btn;


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

        sos_btn.setOnClickListener(new View.OnClickListener() {

        });

        returnHome_btn.setOnClickListener(new View.OnClickListener() {

        });

        contacts_btn.setOnClickListener(new View.OnClickListener() {

        });

        locations_btn.setOnClickListener(new View.OnClickListener() {

        });

        pills_btn.setOnClickListener(new View.OnClickListener() {

        });

        profile_btn.setOnClickListener(new View.OnClickListener() {

        });
    }
}