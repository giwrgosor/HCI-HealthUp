package com.example.healthup.Contacts;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.Locations.EditLocationActivity;
import com.example.healthup.MainActivity;
import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Contact;

public class EditContactsActivity extends AppCompatActivity {

    private EditText editName, editPhone;
    private ContactsDAO contactsDAO;
    private Button btn_saveContact;
    private CheckBox emergencyEdit;
    private ImageButton editcontact_homeBtn;
    private ImageButton voiceEditContacts_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName = findViewById(R.id.editNameContact);
        editPhone = findViewById(R.id.editPhoneContact);
        btn_saveContact = findViewById(R.id.saveContactButton);
        emergencyEdit = findViewById(R.id.emergencyEditCheckBox);
        editcontact_homeBtn = findViewById(R.id.homeEditContact);
        voiceEditContacts_btn = findViewById(R.id.voiceRecEditContacts);

        int contactId = getIntent().getIntExtra("id", -1);
        String oldName = getIntent().getStringExtra("name");
        String oldPhone = getIntent().getStringExtra("phone");
        Boolean emergency = getIntent().getBooleanExtra("emergency", false);
        Contact oldContact = new Contact(contactId, oldName, oldPhone);


        editName.setText(oldName);
        editPhone.setText(Contact.formatPhoneNumber(oldPhone));

        if (emergency) {
            emergencyEdit.setChecked(true);
        } else {
            emergencyEdit.setChecked(false);
        }

        contactsDAO = new ContactsMemoryDAO();

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.imageView2);
            if (background != null) {
                background.setImageResource(R.drawable.contacts_dark_screen);
            }

            int[] textViewIds = {
                    R.id.textView2, R.id.textView3
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }

            int checkboxId = R.id.emergencyEditCheckBox;
            ((android.widget.CheckBox) findViewById(checkboxId)).setTextColor(whiteColor);

            ImageView icon = findViewById(R.id.editIconPerson);
            icon.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN);
        }

        editPhone.addTextChangedListener(new TextWatcher() {
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
                    editPhone.removeTextChangedListener(this);
                    editPhone.setText(formatted);
                    editPhone.setSelection(formatted.length());
                    editPhone.addTextChangedListener(this);
                }

                isFormatting = false;
            }
        });

        voiceEditContacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        btn_saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = editName.getText().toString().trim();
                String newPhone = editPhone.getText().toString().trim();
                String cleanedPhone = newPhone.replaceAll("\\D", "");

                if (newName.isEmpty() && newPhone.isEmpty()) {
                    Toast.makeText(EditContactsActivity.this, "Συμπληρώστε τα πεδία!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newName.isEmpty()) {
                    Toast.makeText(EditContactsActivity.this, "Συμπληρώστε το όνομα!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPhone.isEmpty()) {
                    Toast.makeText(EditContactsActivity.this, "Συμπληρώστε το τηλέφωνο!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cleanedPhone.length() == 10) {
                    boolean isEmergency = emergencyEdit.isChecked();
                    Contact newContact = new Contact(contactId, newName, cleanedPhone, isEmergency);
                    contactsDAO.editContact(oldContact, newContact);

                    Toast.makeText(EditContactsActivity.this, "Η επαφή ενημερώθηκε επιτυχώς!", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(EditContactsActivity.this, DisplayContactsActivity.class);
//                    intent.putExtra("id", newContact.getId());
//                    intent.putExtra("name", newContact.getName());
//                    intent.putExtra("phone", newContact.getPhone());
//                    intent.putExtra("emergency", newContact.isEmergency());
//                    startActivity(intent);
//
//                    finish();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", newContact.getId());
                    resultIntent.putExtra("name", newContact.getName());
                    resultIntent.putExtra("phone", newContact.getPhone());
                    resultIntent.putExtra("emergency", newContact.isEmergency());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                } else {
                    Toast.makeText(EditContactsActivity.this, "Λάθος τηλέφωνο: Πρέπει να έχει 10 ψηφία.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        editcontact_homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditContactsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

    }
}