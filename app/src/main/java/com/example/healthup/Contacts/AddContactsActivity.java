package com.example.healthup.Contacts;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Contact;

import java.util.ArrayList;

public class AddContactsActivity extends AppCompatActivity {

    private EditText nameContactText;
    private EditText phoneContactText;
    private Button addContactBtn;
    private ImageButton btn_homeContact;

    private ContactsDAO contactsDAO;
    private CheckBox emergencyCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameContactText = findViewById(R.id.completedNameContact);
        phoneContactText = findViewById(R.id.completedPhoneContact);
        addContactBtn = findViewById(R.id.addContactButton);
        btn_homeContact = findViewById(R.id.homeContact);

        contactsDAO = new ContactsMemoryDAO();

        emergencyCheckBox = findViewById(R.id.emergencyCheckBox);

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

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.imageView2);
            if (background != null) {
                background.setImageResource(R.drawable.blackbackground);
            }

            int[] textViewIds = {
                    R.id.textView2, R.id.textView3
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            int checkboxId = R.id.emergencyCheckBox;
            ((android.widget.CheckBox) findViewById(checkboxId)).setTextColor(whiteColor);

            ImageView icon = findViewById(R.id.addContactBackground);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameContactText.getText().toString().trim();
                String phone = phoneContactText.getText().toString().trim();
                String cleanedPhone = phone.replaceAll("\\D", "");

                boolean isEmergency = emergencyCheckBox.isChecked();


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

                ArrayList<Contact> currentContacts = contactsDAO.findAll();
                for (Contact existingContact : currentContacts) {
                    String existingCleanedPhone = existingContact.getPhone().replaceAll("\\D", "");
                    if (existingCleanedPhone.equals(cleanedPhone)) {
                        Toast.makeText(AddContactsActivity.this, "Αυτός ο αριθμός υπάρχει ήδη!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (cleanedPhone.length() == 10) {
                    Contact newContact = new Contact(name, phone, isEmergency);
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
