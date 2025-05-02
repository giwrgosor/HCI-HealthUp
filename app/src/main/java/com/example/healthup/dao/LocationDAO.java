package com.example.healthup.dao;

import com.example.healthup.domain.Location;

import java.util.ArrayList;

public interface LocationDAO {

    void delete(Location location);
    void save(Location location);
    void editLocation(Location oldLocation, Location newLocation);
    Location findById(int id);
    ArrayList<Location> findAll();
}
