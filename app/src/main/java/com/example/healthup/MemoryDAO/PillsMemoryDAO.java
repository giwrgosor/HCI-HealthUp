package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;

import java.util.ArrayList;

public class PillsMemoryDAO implements PillsDAO {

    protected static ArrayList<Pill> pills = new ArrayList<>();

    @Override
    public void save(Pill pill) {
        pills.add(pill);
    }

    @Override
    public void delete(Pill pill) {
        pills.remove(pill);
    }

    @Override
    public void update(Pill pill) {
        for (int i = 0; i < pills.size(); i++) {
            if (pills.get(i).getId() == pill.getId()) {
                pills.set(i, pill);
                return;
            }
        }
    }

    @Override
    public ArrayList<Pill> findAll() {
        return new ArrayList<>(pills);
    }

    @Override
    public Pill findById(int id) {
        for (Pill pill : pills) {
            if (pill.getId() == id) {
                return pill;
            }
        }
        return null;
    }

    public boolean existsByName(String name) {
        for (Pill pill : pills) {
            if (pill.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
