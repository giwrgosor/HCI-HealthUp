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
    public ArrayList<Location> findAll() {
        return new ArrayList<>(locations);
    }

    @Override
    public void editLocation(Location oldLocation,String name, double latitude, double longitude, String address, String city, String zip) {
        for(Location location: locations){
            if(location.equals(oldLocation)){
                location.changeData(name,latitude,longitude,address,city,zip);
                break;
            }
        }
    }

    @Override
    public Location findById(int id) {
        for(Location location: locations){
            if(location.getId() == id){
                return location;
            }
        }

        return null;
    }
}
