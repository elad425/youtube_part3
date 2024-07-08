package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.data.UserSession;
import com.example.youtube.entities.user;
import com.example.youtube.repositories.UserRepository;

public class ProfilePageViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<user> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>();

    public ProfilePageViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        checkLoginStatus();
    }

    public void checkLoginStatus() {
        int userId = UserSession.getInstance().getUserId();
        isUserLoggedIn.setValue(userId != 0);
        if (userId != 0) {
            loadUserData(userId);
        }
    }

    private void loadUserData(int userId) {
        user user = userRepository.getUserById(userId);
        currentUser.setValue(user);
    }

    public LiveData<user> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void logOut() {
        UserSession.getInstance().setUserId(0);
        isUserLoggedIn.setValue(false);
        currentUser.setValue(null);
    }
}