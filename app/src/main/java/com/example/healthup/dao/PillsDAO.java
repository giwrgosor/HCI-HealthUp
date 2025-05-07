package com.example.healthup.dao;

import com.example.healthup.domain.Pill;
import java.util.ArrayList;

public interface PillsDAO {
    void save(Pill pill);
    void delete(Pill pill);
    ArrayList<Pill> findAll();
    Pill findById(int id);
    boolean existsByName(String name);

    void update(Pill pill);
}
