package com.example.healthup.dao;

import com.example.healthup.MemoryDAO.UserMemoryDAO;
import com.example.healthup.domain.Contact;
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
        depon.setScheduleForDay("ΠΕΜ", new boolean[]{false, true, true, false, false, false});
        pillDAO.save(depon);

        Pill panadol = new Pill("Panadol Extra");
        panadol.setScheduleForDay("ΤΡΙ", new boolean[]{false, false, true, true, true, false});
        pillDAO.save(panadol);

        Pill algo = new Pill("Algofren");
        algo.setScheduleForDay("ΠΕΜ", new boolean[]{true, true, false, false, true, false});
        pillDAO.save(algo);

        Pill ponstan = new Pill("Ponstan");
        ponstan.setScheduleForDay("ΚΥΡ", new boolean[]{false, false, false, true, false, true});
        pillDAO.save(ponstan);

        Pill nurofen = new Pill("Nurofen");
        nurofen.setScheduleForDay("ΤΕΤ", new boolean[]{false, false, true, false, true, true});
        pillDAO.save(nurofen);

        Pill legofer = new Pill("Legofer");
        legofer.setScheduleForDay("ΠΑΡ", new boolean[]{false, false, true, false, true, false});
        legofer.setScheduleForDay("ΣΑΒ", new boolean[]{false, false, true, false, true, false});
        pillDAO.save(legofer);

        UserDAO userDAO = new UserMemoryDAO();
        userDAO.setUrl("https://5919-34-118-251-207.ngrok-free.app");

    }
}
