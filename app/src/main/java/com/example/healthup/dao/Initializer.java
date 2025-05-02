package com.example.healthup.dao;

import com.example.healthup.domain.Contact;
import com.example.healthup.domain.Location;

public abstract class Initializer {
    protected abstract void eraseData();

//    public abstract ExampleDAO getExampleDAO();
    public abstract LocationDAO getLocationDAO();
    public abstract ContactsDAO getContactsDAO();


    public void prepareData(){
        eraseData();

//        ExampleDAO exampleDAO = getExampleDAO();
//        exampleDAO.save(new Example());

        LocationDAO locationDAO = getLocationDAO();
        locationDAO.save(new Location("Φαρμακείο",37.8759136,23.7533105,"Αναξαγόρα 8", "Γλυφάδα", "16675"));
        locationDAO.save(new Location("Σούπερ Μάρκετ",37.8718571,23.7560822,"Λεωφόρος Βουλιαγμένης 85", "Γλυφάδα", "16674"));


        ContactsDAO contactsDAO = getContactsDAO();
        contactsDAO.save(new Contact("Γιάννης", "6907777777"));
        contactsDAO.save(new Contact("Μαρία", "6907777771"));
        contactsDAO.save(new Contact("Νίκος", "6907777772"));
        contactsDAO.save(new Contact("Ελένη", "6907777774"));
        contactsDAO.save(new Contact("Γιώργος", "6907777776"));


    }
}
