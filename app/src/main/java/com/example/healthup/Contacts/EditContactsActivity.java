package com.example.healthup.Contacts;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthup.R;

public class EditContactsActivity extends AppCompatActivity {

    EditText editName, editPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        editName = findViewById(R.id.editNameContact);
        editPhone = findViewById(R.id.editPhoneContact);


        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");

        editName.setText(name);
        editPhone.setText(phone);
    }
}