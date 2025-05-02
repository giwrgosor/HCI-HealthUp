package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.ContactsDAO;
import com.example.healthup.dao.Initializer;
import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Contact;
import com.example.healthup.domain.Location;

public class MemoryInitializer extends Initializer {

    @Override
    protected void eraseData() {
//        for(Example example : getExampleDAO().findAll()){
//              getExampleDAO().delete(example); }

        for(Location location: getLocationDAO().findAll()){
            getLocationDAO().delete(location);
        }

        for(Contact contact: getContactsDAO().findAll()){
            getContactsDAO().delete(contact);
        }

    }

//    public ExampleDAO getExampleDAO(){
//          return new ExampleMemoryDAO();}

    public LocationDAO getLocationDAO(){
        return new LocationMemoryDAO();
    }

    public ContactsDAO getContactsDAO(){
        return new ContactsMemoryDAO();
    }

}
