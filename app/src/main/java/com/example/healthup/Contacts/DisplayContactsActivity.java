package com.example.healthup.Contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Contact;

import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.HttpRequest;
import com.example.healthup.domain.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DisplayContactsActivity extends AppCompatActivity {
    private ImageButton btn_homeDisplayContact;
    private FloatingActionButton btn_callDisplayContact, btn_editDisplayContact, btn_deleteDisplayContact;
    private Contact contact;
    private  TextView nameTextView, phoneTextView;
    private CheckBox emergencyCheckBox;
    private ImageButton voiceDisplayContacts_btn;
    private String voiceText;
    private String phone;
    private ContactsDAO contactsDAO;


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
        voiceDisplayContacts_btn = findViewById(R.id.voiceRecDisplayContacts);

        contactsDAO = new ContactsMemoryDAO();

        String name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        Boolean emergency = getIntent().getBooleanExtra("emergency", false);

        contact = contactsDAO.findByPhoneAndName(name, phone);

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
                background.setImageResource(R.drawable.contacts_dark_screen);
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

        voiceDisplayContacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say the action you want to do.");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

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
                Toast.makeText(this, "The phone is not available", Toast.LENGTH_SHORT).show();
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
                    .setTitle("Delete Confrimation")
                    .setMessage("Are you sure you want to delete the contact \"" + name + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (name != null && phone != null) {
                            Contact contactToDelete = new Contact(name, phone);
                            contactsDAO.delete(contactToDelete);
                            Toast.makeText(this, "Contact deleted!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, ContactsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
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
            String url = userDAO.getUrl() + "/viewcontact";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String action = json.getString("action");
                            action = action.equals("null") ? "" : action;
                            Log.d("ResponseContacts", response);
                            if(action.equalsIgnoreCase("call")){
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                startActivity(callIntent);
                            }else if(action.equalsIgnoreCase("edit")){
                                Intent intent = new Intent(DisplayContactsActivity.this, EditContactsActivity.class);
                                intent.putExtra("id", contact.getId());
                                intent.putExtra("name", contact.getName());
                                intent.putExtra("phone", contact.getPhone());
                                intent.putExtra("emergency", contact.isEmergency());
                                startActivity(intent);
                            }else if(action.equalsIgnoreCase("delete")){
                                new AlertDialog.Builder(DisplayContactsActivity.this)
                                        .setTitle("Delete Confrimation")
                                        .setMessage("Are you sure you want to delete the contact \"" + contact.getName() + "\";")
                                        .setPositiveButton("Yes", (dialog, which) -> {
                                            if (contact.getName() != null && phone != null) {
                                                Contact contactToDelete = new Contact(contact.getName(), phone);
                                                contactsDAO.delete(contactToDelete);
                                                Toast.makeText(DisplayContactsActivity.this, "Contact deleted!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DisplayContactsActivity.this, ContactsActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }else{
                                handleError("We could not understand your request. Please try again.");
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
        Toast.makeText(DisplayContactsActivity.this,msg, Toast.LENGTH_LONG).show();
    }

}