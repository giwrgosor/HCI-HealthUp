package com.example.healthup.dao;

import com.example.healthup.domain.Contact;
import com.example.healthup.domain.Location;
import com.example.healthup.domain.Pill;

public abstract class Initializer {
    protected abstract void eraseData();

//    public abstract ExampleDAO getExampleDAO();
    public abstract LocationDAO getLocationDAO();
    public abstract ContactsDAO getContactsDAO();
    public abstract PillsDAO getPillsDAO();
    public abstract UserDAO getUserDAO();

    public void prepareData(){
        eraseData();

//        ExampleDAO exampleDAO = getExampleDAO();
//        exampleDAO.save(new Example());

        ContactsDAO contactsDAO = getContactsDAO();
        contactsDAO.save(new Contact("Γιάννης", "6907777777"));
        contactsDAO.save(new Contact("Μαρία", "6907777771"));
        contactsDAO.save(new Contact("Νίκος", "6907777772"));
        contactsDAO.save(new Contact("Ελένη", "6907777774"));
        contactsDAO.save(new Contact("Γιώργος", "6907777776"));

        PillsDAO pillDAO = getPillsDAO();
        pillDAO.save(new Pill("Depon"));
        pillDAO.save(new Pill("Panadol Extra"));
        pillDAO.save(new Pill("Algofren"));
        pillDAO.save(new Pill("Ponstan"));

    }
}
