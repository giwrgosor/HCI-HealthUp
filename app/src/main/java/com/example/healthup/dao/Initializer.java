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
        contactsDAO.save(new Contact("Γιάννης", "6907777777", false));
        contactsDAO.save(new Contact("Μαρία", "6907777771", false));
        contactsDAO.save(new Contact("Νίκος", "6907777772", true));
        contactsDAO.save(new Contact("Ελένη", "6907777774", false));
        contactsDAO.save(new Contact("Γιώργος", "6907777776", false));

        PillsDAO pillDAO = getPillsDAO();
        Pill depon = new Pill("Depon");
        depon.setScheduleForDay("ΔΕΥ", new boolean[]{true, false, false, true, false, true});
        depon.setScheduleForDay("ΠΑΡ", new boolean[]{false, true, true, false, false, false});
        pillDAO.save(depon);

        Pill panadol = new Pill("Panadol Extra");
        panadol.setScheduleForDay("ΤΡΙ", new boolean[]{false, false, true, true, true, false});
        pillDAO.save(panadol);

        Pill algo = new Pill("Algofren");
        algo.setScheduleForDay("ΤΕΤ", new boolean[]{true, true, false, false, true, false});
        pillDAO.save(algo);

        Pill ponstan = new Pill("Ponstan");
        ponstan.setScheduleForDay("ΚΥΡ", new boolean[]{false, false, false, true, false, true});
        pillDAO.save(ponstan);

    }
}
