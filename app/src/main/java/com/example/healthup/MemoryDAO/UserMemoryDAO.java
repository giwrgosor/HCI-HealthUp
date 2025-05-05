package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.User;

public class UserMemoryDAO implements UserDAO {
    private static User user = new User(null,null,null,null);

    @Override
    public void editUser(User newUser) {
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setBloodType(newUser.getBloodType());
        user.setBloodRhFactor(newUser.getBloodRhFactor());
    }

    @Override
    public User getUser() {
        return user;
    }
}
