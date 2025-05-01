package com.example.healthup.dao;

import com.example.healthup.domain.Location;

public abstract class Initializer {
    protected abstract void eraseData();

//    public abstract ExampleDAO getExampleDAO();
    public abstract LocationDAO getLocationDAO();


    public void prepareData(){
        eraseData();

//        ExampleDAO exampleDAO = getExampleDAO();
//        exampleDAO.save(new Example());

        LocationDAO locationDAO = getLocationDAO();
        locationDAO.save(new Location("Φαρμακείο",37.8759136,23.7533105,"Αναξαγόρα 8", "Γλυφάδα", "16675"));
        locationDAO.save(new Location("Σούπερ Μάρκετ",37.8718571,23.7560822,"Λεωφόρος Βουλιαγμένης 85", "Γλυφάδα", "16674"));
    }
}
