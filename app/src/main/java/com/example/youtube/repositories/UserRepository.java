package com.example.youtube.repositories;

import android.app.Application;
import com.example.youtube.entities.User;
import com.example.youtube.api.UserApi;
import java.util.List;

public class UserRepository {
    private final UserApi api;

    private List<User> users;

    private User user;

    public UserRepository(Application application) {
        api = new UserApi(application.getApplicationContext());
        getUsers();
    }

    public User getUserById(String userId) {
        api.getUserById(userId,new UserApi.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                user = result;
            }

            @Override
            public void onError(String error) {
            }
        });
        return user;
    }

    public void getUsers() {
        api.getUsers(new UserApi.ApiCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                users = result;
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public List<User> getAllUsers(){
        return users;
    }

    public void insertUser(User newUser) {
        api.createUser(newUser);
    }

    public void updateUser(User newUser) {
        api.updateUser(newUser.get_id(), newUser);
    }

}