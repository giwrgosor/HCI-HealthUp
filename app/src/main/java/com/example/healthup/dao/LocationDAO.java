package com.example.healthup.dao;

import com.example.healthup.domain.Location;

import java.util.ArrayList;

public interface LocationDAO {

    void delete(Location location);
    void save(Location location);
    ArrayList<Location> findAll();
}
