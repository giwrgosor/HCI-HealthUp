package com.example.healthup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactsActivity extends AppCompatActivity {

    private EditText nameContactText;
    private EditText phoneContactText;
    private Button addContactBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        nameContactText = findViewById(R.id.completedNameContact);
        phoneContactText = findViewById(R.id.completedPhoneContact);
        addContactBtn = findViewById(R.id.addContactButton);

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = phoneContactText.getText().toString().trim();

                if (phone.length() == 10 && phone.matches("\\d+")) {
                    Toast.makeText(AddContactsActivity.this, "Η επαφή προστέθηκε!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddContactsActivity.this, ContactsActivity.class);
                    intent.putExtra("name", nameContactText.getText().toString().trim());
                    intent.putExtra("phone", phoneContactText.getText().toString().trim());
                    startActivity(intent);

                } else {
                    Toast.makeText(AddContactsActivity.this, "Λάθος τηλέφωνο: Πρέπει να έχει 10 ψηφία.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
