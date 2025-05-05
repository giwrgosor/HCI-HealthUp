package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.dao.Initializer;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Contact;
import com.example.healthup.domain.Location;
import com.example.healthup.domain.Pill;

public class MemoryInitializer extends Initializer {

    @Override
    protected void eraseData() {
        for(Location location: getLocationDAO().findAll()){
            getLocationDAO().delete(location);
        }

        for(Contact contact: getContactsDAO().findAll()){
            getContactsDAO().delete(contact);
        }

        for(Pill pill: getPillsDAO().findAll()){
            getPillsDAO().delete(pill);
        }
    }

    @Override
    public LocationDAO getLocationDAO(){
        return new LocationMemoryDAO();
    }

    @Override
    public ContactsDAO getContactsDAO(){
        return new ContactsMemoryDAO();
    }

    public PillsDAO getPillsDAO(){
        return new PillsMemoryDAO();
    }
}
