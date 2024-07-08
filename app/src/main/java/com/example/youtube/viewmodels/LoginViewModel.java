package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.data.UserSession;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> loginSuccessful = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getLoginSuccessful() {
        return loginSuccessful;
    }

    public void login(String email, String password) {
        List<User> users = userRepository.getAllUsers();
        if (users != null) {
            for (User u : users) {
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    UserSession.getInstance().setUser(u);
                    loginSuccessful.setValue(true);
                    return;
                }
            }
        }
        loginSuccessful.setValue(false);
    }
}