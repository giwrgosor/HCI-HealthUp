package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.domain.Contact;

import java.util.ArrayList;

public class ContactsMemoryDAO implements ContactsDAO {

    protected static ArrayList<Contact> contacts = new ArrayList<>();
    public void delete(Contact contact){
        contacts.remove(contact);
    }
    public void save(Contact contact){
        contacts.add(contact);
    }
    public void editContact(Contact oldContact, Contact newContact) {

        for(Contact contact: contacts){
            if(contact.equals(oldContact)){
                contact.changeContactData(newContact);
                break;
            }
        }

    }
    public ArrayList<Contact> findAll() {
        return new ArrayList<>(contacts);
    }

    public Contact findById(int id) {
        for(Contact contact: contacts){
            if(contact.getId() == id){
                return contact;
            }
        }

        return null;
    }

    @Override
    public Contact findByPhone(String phone) {
        for(Contact c : contacts){
            if(c.getPhone().equals(phone)){
                return c;
            }
        }
        return null;
    }

    @Override
    public Contact findByName(String name) {
        for(Contact c : contacts){
            if(c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }

    @Override
    public Contact findByPhoneAndName(String name, String phone) {
        for(Contact c : contacts){
            if(c.getName().equalsIgnoreCase(name) && c.getPhone().equals(phone)){
                return c;
            }
        }
        return null;
    }
}
