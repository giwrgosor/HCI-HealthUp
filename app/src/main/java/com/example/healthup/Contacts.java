package com.example.healthup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {

    private Button btn_addContact;

    private RecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        btn_addContact = findViewById(R.id.addAContact);

        recyclerView = findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(adapter);

        contactList.add(new Contact("Γιάννης", "6907777777"));
        contactList.add(new Contact("Μαρία", "6907777771"));
        contactList.add(new Contact("Νίκος", "6907777772"));
        contactList.add(new Contact("Ελένη", "6907777774"));
        contactList.add(new Contact("Γιώργος", "6907777776"));

        adapter.sortContacts(contactList);

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");

        if (name != null && phone != null) {
            contactList.add(new Contact(name, phone));
            adapter.notifyItemInserted(contactList.size() - 1);
            adapter.sortContacts(contactList);
        }

        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Contacts.this, AddContacts.class);
                startActivity(intent);
            }
        });
    }
}
