package com.example.youtube.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.youtube.api.UserApi;
import com.example.youtube.data.AppDatabase;
import com.example.youtube.entities.user;

import java.util.List;

public class UserRepository {
    private final AppDatabase db;
    private final UserApi api;

    private final LiveData<List<user>> users;

    public UserRepository(Application application) {
        db = AppDatabase.getInstance(application);
        api = new UserApi(db.userDao());
        api.get();
        users = db.userDao().getAllUsersLive();
    }

    public user getUserById(int userId) {
        return db.userDao().getUserById(userId);
    }

    public List<user> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public LiveData<List<user>> getAllUsersLive() {
        return users;
    }

    public void insertUser(user newUser) {
        db.userDao().insert(newUser);
        api.createUser(newUser);
    }

    public void updateUser(user newUser) {
        db.userDao().update(newUser);
        api.updateUser(newUser.getId(), newUser);
    }

}