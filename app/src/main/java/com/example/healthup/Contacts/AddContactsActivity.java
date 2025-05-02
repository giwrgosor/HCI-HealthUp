package com.example.healthup.Contacts;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Contact;

public class AddContactsActivity extends AppCompatActivity {

    private EditText nameContactText;
    private EditText phoneContactText;
    private Button addContactBtn;
    private ImageView btn_homeContact;

    private ContactsDAO contactsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        nameContactText = findViewById(R.id.completedNameContact);
        phoneContactText = findViewById(R.id.completedPhoneContact);
        addContactBtn = findViewById(R.id.addContactButton);
        btn_homeContact = findViewById(R.id.homeContact);

        contactsDAO = new ContactsMemoryDAO(); // in-memory βάση

        phoneContactText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting) return;

                isFormatting = true;

                String raw = s.toString();
                String cleaned = raw.replaceAll("\\D", "");

                String formatted = Contact.formatPhoneNumber(cleaned);
                if (!formatted.equals(current)) {
                    current = formatted;
                    phoneContactText.removeTextChangedListener(this);
                    phoneContactText.setText(formatted);
                    phoneContactText.setSelection(formatted.length());
                    phoneContactText.addTextChangedListener(this);
                }

                isFormatting = false;
            }
        });

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameContactText.getText().toString().trim();
                String phone = phoneContactText.getText().toString().trim();
                String cleanedPhone = phone.replaceAll("\\D", "");

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

                if (cleanedPhone.length() == 10) {
                    Contact newContact = new Contact(name, phone);
                    contactsDAO.save(newContact);

                    Toast.makeText(AddContactsActivity.this, "Η επαφή προστέθηκε!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddContactsActivity.this, ContactsActivity.class);
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
