package com.example.healthup.MemoryDAO;

import com.example.healthup.dao.UserDAO;
import com.example.healthup.domain.User;

public class UserMemoryDAO implements UserDAO {
    private static User user = new User(null,null,null,null,null,null,null);
    private static String url = "";

    @Override
    public void editUser(User newUser) {
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setBloodType(newUser.getBloodType());
        user.setBloodRhFactor(newUser.getBloodRhFactor());
        user.setAddress(newUser.getAddress());
        user.setCity(newUser.getCity());
        user.setZipcode(newUser.getZipcode());
        user.setBloodRhFactor(newUser.getBloodRhFactor());
        user.setBloodType(newUser.getBloodType());
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String newUrl) {
        url = newUrl;
    }
}
