package com.example.healthup.dao;

import com.example.healthup.MemoryDAO.UserMemoryDAO;
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

        LocationDAO locationDAO = getLocationDAO();
        locationDAO.save(new Location("Medical Center", 37.984472, 23.752042, "Mihalakopoulou 114", "Athens", "11527"));
        locationDAO.save(new Location("Public Hospital", 38.011428, 23.680210, "Thivon 372", "Peristeri", "12135"));

        ContactsDAO contactsDAO = getContactsDAO();
        contactsDAO.save(new Contact("John", "6907777777", false));
        contactsDAO.save(new Contact("Helen", "6907777771", false));
        contactsDAO.save(new Contact("Nick", "6907777772", true));
        contactsDAO.save(new Contact("George", "6907777776", false));

        PillsDAO pillDAO = getPillsDAO();
        Pill depon = new Pill("Depon");
        depon.setScheduleForDay("MON", new boolean[]{true, false, false, true, false, true});
        depon.setScheduleForDay("THU", new boolean[]{false, true, true, false, false, false});
        pillDAO.save(depon);

        Pill panadol = new Pill("Panadol Extra");
        panadol.setScheduleForDay("TUE", new boolean[]{false, false, true, true, true, false});
        pillDAO.save(panadol);

        Pill algo = new Pill("Algofren");
        algo.setScheduleForDay("THU", new boolean[]{true, true, false, false, true, false});
        pillDAO.save(algo);

        Pill ponstan = new Pill("Ponstan");
        ponstan.setScheduleForDay("SUN", new boolean[]{false, false, false, true, false, true});
        pillDAO.save(ponstan);

        Pill nurofen = new Pill("Nurofen");
        nurofen.setScheduleForDay("WED", new boolean[]{false, false, true, false, true, true});
        pillDAO.save(nurofen);

        Pill legofer = new Pill("Legofer");
        legofer.setScheduleForDay("FRI", new boolean[]{false, false, true, false, true, false});
        legofer.setScheduleForDay("SAT", new boolean[]{false, false, true, false, true, false});
        pillDAO.save(legofer);

        UserDAO userDAO = new UserMemoryDAO();
        userDAO.setUrl("https://fd32-34-139-190-207.ngrok-free.app");

    }
}
