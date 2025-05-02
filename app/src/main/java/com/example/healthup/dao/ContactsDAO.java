package com.example.healthup.dao;

import com.example.healthup.domain.Contact;

import java.util.ArrayList;

public interface ContactsDAO {
    void delete(Contact contact);
    void save(Contact contact);
    void editLocation(Contact oldContact, Contact newContact);
    Contact findById(int id);
    ArrayList<Contact> findAll();
}
