package com.example.healthup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactsActivity extends AppCompatActivity {

    private EditText nameContactText;
    private EditText phoneContactText;
    private Button addContactBtn;
    private ImageView btn_homeContact;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        nameContactText = findViewById(R.id.completedNameContact);
        phoneContactText = findViewById(R.id.completedPhoneContact);
        addContactBtn = findViewById(R.id.addContactButton);
        btn_homeContact = findViewById(R.id.homeContact);

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameContactText.getText().toString().trim();
                String phone = phoneContactText.getText().toString().trim();

                if (name.isEmpty() && phone.isEmpty()) {
                    Toast.makeText(AddContactsActivity.this, "Συμπληρώστε τα πεδία!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.isEmpty()) {
                    Toast.makeText(AddContactsActivity.this, "Συμπληρώστε το όνομα!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.isEmpty()) {
                    Toast.makeText(AddContactsActivity.this, "Συμπληρώστε το τηλέφωνο!", Toast.LENGTH_SHORT).show();
                    return;
                }

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

        btn_homeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

    }


}
