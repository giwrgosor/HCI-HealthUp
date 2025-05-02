package com.example.healthup.Contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.R;
import com.example.healthup.domain.Contact;

import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.dao.ContactsDAO;


public class DisplayContactsActivity extends AppCompatActivity {
    private ImageView btn_homeDisplayContact, btn_callDisplayContact, btn_editDisplayContact, btn_deleteDisplayContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        TextView nameTextView = findViewById(R.id.nameDisplayContact);
        TextView phoneTextView = findViewById(R.id.phoneDisplayContact);

        btn_homeDisplayContact = findViewById(R.id.homeContact);
        btn_callDisplayContact = findViewById(R.id.callIcon);
        btn_editDisplayContact = findViewById(R.id.editIcon);
        btn_deleteDisplayContact = findViewById(R.id.deleteIcon);

        ContactsDAO contactsDAO = new ContactsMemoryDAO();

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");

        if (name != null) {
            nameTextView.setText(name);
        }

        if (phone != null) {
            String formattedPhone = Contact.formatPhoneNumber(phone);
            phoneTextView.setText(formattedPhone);
        }

        btn_homeDisplayContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayContactsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        btn_callDisplayContact.setOnClickListener(view -> {
            if (phone != null && !phone.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Το τηλέφωνο δεν είναι διαθέσιμο", Toast.LENGTH_SHORT).show();
            }
        });

        btn_editDisplayContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayContactsActivity.this, EditContactsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

        btn_deleteDisplayContact.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Επιβεβαίωση διαγραφής")
                    .setMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε την επαφή;")
                    .setPositiveButton("Ναι", (dialog, which) -> {
                        if (name != null && phone != null) {
                            Contact contactToDelete = new Contact(name, phone);
                            contactsDAO.delete(contactToDelete);
                            Toast.makeText(this, "Η επαφή διαγράφηκε", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Όχι", null)
                    .show();
        });


    }
}