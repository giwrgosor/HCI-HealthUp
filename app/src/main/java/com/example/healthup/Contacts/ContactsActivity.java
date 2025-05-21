package com.example.healthup.Contacts;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.ContactsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private FloatingActionButton btn_addContact;
    private ImageButton btn_homeContact;
    private ImageView btn_numpadContact;

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsRecyclerViewAdapter adapter;

    private ContactsDAO contactsDAO;
    private ImageButton voiceContacts_btn;

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
                background.setImageResource(R.drawable.blackbackground);
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
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
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
    protected void onResume() {
        super.onResume();
        contactList.clear();
        contactList.addAll(contactsDAO.findAll());
        adapter.sortContacts(contactList);
        adapter.notifyDataSetChanged();
    }

}
