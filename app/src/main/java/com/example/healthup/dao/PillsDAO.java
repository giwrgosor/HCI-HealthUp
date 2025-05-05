package com.example.healthup.dao;

import com.example.healthup.domain.Pill;
import java.util.ArrayList;

public interface PillsDAO {
    void save(Pill pill);
    void delete(Pill pill);
    void editPill(Pill oldPill, Pill newPill);
    ArrayList<Pill> findAll();
    Pill findById(int id);
}
