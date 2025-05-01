package com.example.healthup.dao;

import com.example.healthup.domain.Location;

import java.util.List;

public interface LocationDAO {

    void delete(Location location);
    void save(Location location);
    List<Location> findAll();
}
