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
    public void editPill(Pill oldPill, Pill newPill) {
        for (Pill p : pills) {
            if (p.equals(oldPill)) {
                p.changePillData(newPill);
                break;
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
}
