package com.example.youtube.data;

import com.example.youtube.entities.User;

public class UserSession {
    private static UserSession instance;
    private User user;
    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

