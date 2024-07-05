package com.example.youtube.repositories;

import android.app.Application;

import com.example.youtube.api.UserApi;
import com.example.youtube.data.AppDatabase;
import com.example.youtube.entities.user;

import java.util.List;

public class UserRepository {
    private final AppDatabase db;
    private final UserApi api;

    public UserRepository(Application application) {
        db = AppDatabase.getInstance(application);
        api = new UserApi(db.userDao());
        api.get();
    }

    public user getUserById(int userId) {
        return db.userDao().getUserById(userId);
    }

    public List<user> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public void insertUser(user newUser) {
        db.userDao().insert(newUser);
        api.createUser(newUser, new UserApi.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle successful user creation
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    public void updateUser(user newUser) {
        db.userDao().update(newUser);
        api.updateUser(newUser.getId(), newUser, new UserApi.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle successful user update
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

}