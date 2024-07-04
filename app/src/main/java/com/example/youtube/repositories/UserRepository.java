package com.example.youtube.repositories;

import android.app.Application;
import android.content.Context;

import com.example.youtube.AppDatabase;
import com.example.youtube.entities.user;
import com.example.youtube.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final AppDatabase db;

    public UserRepository(Application application) {
        db = AppDatabase.getInstance(application);
        loadUsers(application.getApplicationContext());
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

    public void updateUser(user newUser) {
        db.userDao().update(newUser);
    }

    private void loadUsers(Context context){
        if (db.userDao().getAllUsers().isEmpty()) {
            ArrayList<user> tempUser = JsonUtils.loadUsersFromJson(context);
            for (user u : tempUser) {
                db.userDao().insert(u);
            }
        }
    }
}