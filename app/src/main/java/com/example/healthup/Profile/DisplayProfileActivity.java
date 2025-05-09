package com.example.healthup.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayProfileActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private User user;
    private TextView name_txt;
    private TextView surname_txt;
    private TextView address_txt;
    private TextView city_txt;
    private TextView zip_txt;
    private TextView bloodType_txt;
    private TextView bloodFactor_txt;
    private FloatingActionButton edit_btn;
    private ImageButton homeButtonDisplayProfile;

    @Override
    protected void onResume(){
        super.onResume();
        user = userDAO.getUser();
        name_txt.setText(user.getName());
        surname_txt.setText(user.getSurname());
        address_txt.setText(user.getAddress());
        city_txt.setText(user.getCity());
        zip_txt.setText(user.getZipcode());
        bloodType_txt.setText(user.getBloodType());
        bloodFactor_txt.setText(user.getBloodRhFactor());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userDAO = new UserMemoryDAO();
        user = userDAO.getUser();
        Log.d("DisplayProfileActivity","Vrethike o user" + user.getName());
        name_txt = findViewById(R.id.displayProfileName_txt);
        surname_txt = findViewById(R.id.displayProfileSurname_txt);
        address_txt = findViewById(R.id.displayProfileAddress_txt);
        city_txt = findViewById(R.id.displayProfileCity_txt);
        zip_txt = findViewById(R.id.displayProfileZip_txt);
        bloodType_txt = findViewById(R.id.displayProfileBloodType);
        bloodFactor_txt = findViewById(R.id.displayProfileRhFactor);
        homeButtonDisplayProfile = findViewById(R.id.homeButtonDisplayProfile);

        name_txt.setText(user.getName());
        surname_txt.setText(user.getSurname());
        address_txt.setText(user.getAddress());
        city_txt.setText(user.getCity());
        zip_txt.setText(user.getZipcode());
        bloodType_txt.setText(user.getBloodType());
        bloodFactor_txt.setText(user.getBloodRhFactor());

        edit_btn = findViewById(R.id.editUserProfile_btn);

        homeButtonDisplayProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProfileActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}