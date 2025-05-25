package com.example.healthup.Contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Contact;
import com.example.healthup.domain.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditContactsActivity extends AppCompatActivity {

    private EditText editName, editPhone;
    private ContactsDAO contactsDAO;
    private Button btn_saveContact;
    private CheckBox emergencyEdit;
    private ImageButton editcontact_homeBtn;
    private ImageButton voiceEditContacts_btn;
    private String voiceText;

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

        editName.setText(oldName);
        editPhone.setText(Contact.formatPhoneNumber(oldPhone));

        if (emergency) {
            emergencyEdit.setChecked(true);
        } else {
            emergencyEdit.setChecked(false);
        }

        contactsDAO = new ContactsMemoryDAO();
        Contact oldContact = contactsDAO.findByPhoneAndName(oldName,oldPhone);

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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say the values you want to enter");
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
                    Toast.makeText(EditContactsActivity.this, "Please fill the fields of the contact!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newName.isEmpty()) {
                    Toast.makeText(EditContactsActivity.this, "Please enter the name of the contact!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPhone.isEmpty()) {
                    Toast.makeText(EditContactsActivity.this, "Please enter the phone of the contact!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cleanedPhone.length() == 10) {
                    boolean isEmergency = emergencyEdit.isChecked();
                    Contact newContact = new Contact(newName, cleanedPhone, isEmergency);
                    contactsDAO.editContact(oldContact, newContact);

                    Toast.makeText(EditContactsActivity.this, "The contact has been successfully updated!", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(EditContactsActivity.this, "Invalid phone number: The phone number must have 10 digits.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DEMO-REQUESTCODE",
                Integer.toString(requestCode));
        Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/add-edit-contact";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String name = json.getString("name");
                            name = name.equals("null") ? "" : name;
                            String phone = json.getString("phone");
                            phone = phone.equals("null") ? "" : phone;
                            String emergency = json.getString("emergency");
                            emergency = emergency.equals("null") ? "" : emergency;
                            Log.d("ResponseContacts", response);

                            if(!name.isEmpty()){
                                editName.setText(name);
                            }
                            if(!phone.isEmpty()){
                                editPhone.setText(phone);
                            }
                            if(!emergency.isEmpty()){
                                emergencyEdit.setChecked(emergency.equalsIgnoreCase("true"));
                            }
                            if(phone.isEmpty() && name.isEmpty() && emergency.isEmpty()){
                                handleError("We could not understand the values that you said. Please try again.");
                            }

                        } catch (JSONException e) {
                            Log.e("HTTP_RESPONSE", "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error", error);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Recognizer API error");
        }
    }

    public void handleError(String msg){
        Toast.makeText(EditContactsActivity.this,msg, Toast.LENGTH_LONG).show();
    }
}