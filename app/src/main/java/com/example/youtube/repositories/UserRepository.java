package com.example.youtube.repositories;

import android.app.Application;

import com.example.youtube.AppDatabase;
import com.example.youtube.entities.user;

import java.util.List;

public class UserRepository {
    private final AppDatabase db;

    public UserRepository(Application application) {
        db = AppDatabase.getInstance(application);
    }

    public user getUserById(int userId) {
        return db.userDao().getUserById(userId);
    }

    public List<user> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public void insertUser(user newUser) {
        db.userDao().insert(newUser);
    }
}