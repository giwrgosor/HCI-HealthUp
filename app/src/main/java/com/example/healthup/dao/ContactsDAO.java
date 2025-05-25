package com.example.healthup.dao;

import com.example.healthup.domain.Contact;

import java.util.ArrayList;

public interface ContactsDAO {
    void delete(Contact contact);
    void save(Contact contact);
    void editContact(Contact oldContact, Contact newContact);
    Contact findById(int id);
    Contact findByName(String name);
    Contact findByPhone(String phone);
    Contact findByPhoneAndName(String name, String phone);
    ArrayList<Contact> findAll();
}
