package com.example.healthup.dao;

import com.example.healthup.domain.User;

public interface UserDAO {
    void editUser(User newUser);
    void setUrl(String url);
    User getUser();
    String getUrl();
}
