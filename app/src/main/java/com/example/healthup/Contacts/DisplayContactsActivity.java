package com.example.healthup.Contacts;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.LocationMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Contact;

import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DisplayContactsActivity extends AppCompatActivity {
    private ImageButton btn_homeDisplayContact;
    private FloatingActionButton btn_callDisplayContact, btn_editDisplayContact, btn_deleteDisplayContact;
    private Contact contact;
    private  TextView nameTextView, phoneTextView;
    private CheckBox emergencyCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameTextView = findViewById(R.id.nameDisplayContact);
        phoneTextView = findViewById(R.id.phoneDisplayContact);

        btn_homeDisplayContact = findViewById(R.id.homeViewContact);
        btn_callDisplayContact = findViewById(R.id.callDisplayContactIcon);
        btn_editDisplayContact = findViewById(R.id.editDisplayContactIcon);
        btn_deleteDisplayContact = findViewById(R.id.deleteDisplayContactIcon);
        emergencyCheckBox = findViewById(R.id.emergencyDisplayCheckBox);

        ContactsDAO contactsDAO = new ContactsMemoryDAO();

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        Boolean emergency = getIntent().getBooleanExtra("emergency", false);

        this.contact = new Contact(name, phone, emergency);

        if (name != null) {
            nameTextView.setText(name);
        }

        if (phone != null) {
            String formattedPhone = Contact.formatPhoneNumber(phone);
            phoneTextView.setText(formattedPhone);
        }

        if (emergency != null && emergency) {
            emergencyCheckBox.setChecked(true);
//            emergencyCheckBox.setEnabled(false);
            emergencyCheckBox.setClickable(false);
        } else {
            emergencyCheckBox.setChecked(false);
//            emergencyCheckBox.setEnabled(false);
            emergencyCheckBox.setClickable(false);
        }

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

            int checkboxId = R.id.emergencyDisplayCheckBox;
            ((android.widget.CheckBox) findViewById(checkboxId)).setTextColor(whiteColor);

            ImageView icon = findViewById(R.id.displayIconPerson);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
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

//        btn_editDisplayContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DisplayContactsActivity.this, EditContactsActivity.class);
//                intent.putExtra("id", contact.getId());
//                intent.putExtra("name", name);
//                intent.putExtra("phone", phone);
//                intent.putExtra("emergency", emergency);
//                startActivity(intent);
//            }
//        });

        btn_editDisplayContact.setOnClickListener(view -> {
            Intent intent = new Intent(DisplayContactsActivity.this, EditContactsActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
            intent.putExtra("emergency", contact.isEmergency());
            startActivityForResult(intent, 1); // Code 1 για edit
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
                            Intent intent = new Intent(this, ContactsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Όχι", null)
                    .show();
        });

    }

    protected void onResume() {
        super.onResume();

        ContactsDAO contactsDAO = new ContactsMemoryDAO();
        Contact updatedContact = contactsDAO.findById(contact.getId());

        if (updatedContact != null) {
            nameTextView.setText(updatedContact.getName());
            phoneTextView.setText(Contact.formatPhoneNumber(updatedContact.getPhone()));
            emergencyCheckBox.setChecked(updatedContact.isEmergency());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("name");
            String updatedPhone = data.getStringExtra("phone");
            boolean updatedEmergency = data.getBooleanExtra("emergency", false);
            int updatedId = data.getIntExtra("id", contact.getId());

            contact = new Contact(updatedId, updatedName, updatedPhone, updatedEmergency);

            nameTextView.setText(updatedName);
            phoneTextView.setText(Contact.formatPhoneNumber(updatedPhone));
            emergencyCheckBox.setChecked(updatedEmergency);
        }
    }

}