package com.example.healthup.Contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.Contact;
import com.example.healthup.domain.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactsActivity extends AppCompatActivity {

    private FloatingActionButton btn_addContact;
    private ImageButton btn_homeContact;
    private ImageView btn_numpadContact;

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsRecyclerViewAdapter adapter;

    private ContactsDAO contactsDAO;
    private ImageButton voiceContacts_btn;
    private String voiceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            ImageView background = findViewById(R.id.contactsBackground);
            if (background != null) {
                background.setImageResource(R.drawable.contacts_dark_screen);
            }
        }

        btn_addContact = findViewById(R.id.addAContact);
        btn_numpadContact = findViewById(R.id.numpadContact);
        btn_homeContact = findViewById(R.id.homeContact);
        voiceContacts_btn = findViewById(R.id.voiceRecContacts);

        recyclerView = findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactsDAO = new ContactsMemoryDAO();
        contactList = contactsDAO.findAll();

        adapter = new ContactsRecyclerViewAdapter(contactList);
        recyclerView.setAdapter(adapter);
        adapter.sortContacts(contactList);

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        Boolean emergency = getIntent().getBooleanExtra("emergency", false);

        if (name != null && phone != null) {
            Contact newContact = new Contact(name, phone, emergency);
            contactsDAO.save(newContact);
            adapter.notifyItemInserted(contactList.size() - 1);
            adapter.sortContacts(contactList);
        }

        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
            }
        });

        voiceContacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What would you like to do?");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        btn_numpadContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this,KeypadActivity.class);
                startActivity(intent);
            }
        });

        btn_homeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data); Log.i("DEMO-REQUESTCODE",
                Integer.toString(requestCode)); Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));
        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null){
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceText = text.get(0);

            UserDAO userDAO = new UserMemoryDAO();
            String url = userDAO.getUrl() + "/contacts";

            try {
                JSONObject json = new JSONObject();
                json.put("text", voiceText);

                HttpRequest.sendPostRequest(url, json.toString(), new HttpRequest.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);

                            String action = json.getString("action");
                            String name = json.getString("name");
                            name = name.equals("null")? "" : name;
                            String phone = json.getString("phone");
                            phone = phone.equals("null")? "" : phone;
                            Log.d("ResponseContacts", response);
                            switch (action){
                                case "call":
                                    if(!name.isEmpty() && !phone.isEmpty()){
//                                      Klhsh me bash to onoma kai thl
                                        Contact foundC = contactsDAO.findByPhoneAndName(name,phone);
                                        if(foundC == null){
                                          handleError("No contact exists with the name and phone that you provided");
                                        }else{
                                            Intent intent = new Intent(Intent.ACTION_CALL);  // ACTION_CALL
                                            intent.setData(Uri.parse("tel:" + foundC.getPhone()));
                                            startActivity(intent);
                                        }
                                    }else if(!name.isEmpty() && phone.isEmpty()){
//                                      Klhsh me onoma
                                        Contact foundC = contactsDAO.findByName(name);
                                        if(foundC == null){
                                            handleError("No contact exists with the name that you provided");
                                        }else{
                                            Intent intent = new Intent(Intent.ACTION_CALL);  // ACTION_CALL
                                            intent.setData(Uri.parse("tel:" + foundC.getPhone()));
                                            startActivity(intent);
                                        }
                                    }else if(name.isEmpty() && !phone.isEmpty()){
//                                      Dial ton arithmo
                                        Intent intent = new Intent(ContactsActivity.this,KeypadActivity.class);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                    }else{
                                        handleError("We couldnt understand your request. If you want to make a call, say the name or the phone of the person that you want to call");
                                    }
                                    break;
                                case "view":
                                    if(!name.isEmpty() && !phone.isEmpty()){
//                                      View me bash to onoma kai thl
                                        Contact foundC = contactsDAO.findByPhoneAndName(name,phone);
                                        if(foundC == null){
                                            handleError("No contact exists with the name and phone that you provided");
                                        }else{
                                            Intent intent = new Intent(ContactsActivity.this, DisplayContactsActivity.class);
                                            intent.putExtra("name", foundC.getName());
                                            intent.putExtra("phone", foundC.getPhone());
                                            intent.putExtra("emergency", foundC.isEmergency());
                                            startActivity(intent);
                                        }
                                    }else if(!name.isEmpty() && phone.isEmpty()){
//                                      View me onoma
                                        Contact foundC = contactsDAO.findByName(name);
                                        if(foundC == null){
                                            handleError("No contact exists with the name that you provided");
                                        }else{
                                            Intent intent = new Intent(ContactsActivity.this, DisplayContactsActivity.class);
                                            intent.putExtra("name", foundC.getName());
                                            intent.putExtra("phone", foundC.getPhone());
                                            intent.putExtra("emergency", foundC.isEmergency());
                                            startActivity(intent);
                                        }
                                    }else if(name.isEmpty() && !phone.isEmpty()){
//                                      View me arithmo
                                        Contact foundC = contactsDAO.findByPhone(phone);
                                        if(foundC == null){
                                            handleError("No contact exists with the name and phone that you provided");
                                        }else {
                                            Intent intent = new Intent(ContactsActivity.this, DisplayContactsActivity.class);
                                            intent.putExtra("name", foundC.getName());
                                            intent.putExtra("phone", foundC.getPhone());
                                            intent.putExtra("emergency", foundC.isEmergency());
                                            startActivity(intent);
                                        }
                                    }else{
                                        handleError("We couldnt understand your request. If you want to view a contact, say the name or the phone of the contact you want");
                                    }
                                    break;
                                case "dial":
                                    if(!phone.isEmpty()){
                                        Intent intent = new Intent(ContactsActivity.this,KeypadActivity.class);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                    }else if(name.isEmpty()){
                                        Intent intent = new Intent(ContactsActivity.this,KeypadActivity.class);
                                        startActivity(intent);
                                    } else{
                                        handleError("We couldnt understand your request. If you want to dial a phone, say the phone you want to dial");
                                    }
                                    break;
                                case "add":
                                    Intent intent = new Intent(ContactsActivity.this, AddContactsActivity.class);
                                    if (!phone.isEmpty()) {
                                        intent.putExtra("phone", phone);
                                    }
                                    if (!name.isEmpty()) {
                                        intent.putExtra("name", name);
                                    }
                                    startActivity(intent);
                                    break;
                                default:
                                    handleError("We couldnt understand your request. Please try again!");
                            }
                        }catch(JSONException e){
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

        }
        else{
            System.out.println("Recognizer API error");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactList.clear();
        contactList.addAll(contactsDAO.findAll());
        adapter.sortContacts(contactList);
        adapter.notifyDataSetChanged();
    }

    public void handleError(String msg){
        Toast.makeText(ContactsActivity.this,msg, Toast.LENGTH_LONG).show();
    }

}
