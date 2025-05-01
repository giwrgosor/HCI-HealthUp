package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.LocationDAO;
import com.example.healthup.domain.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationMemoryDAO implements LocationDAO {
    protected static ArrayList<Location> locations = new ArrayList<>();

    @Override
    public void delete(Location location) {
        locations.remove(location);
    }

    @Override
    public void save(Location location) {
        locations.add(location);
    }

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations);
    }
}
