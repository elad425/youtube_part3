package com.example.youtube.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.user;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class SignUpViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> signUpSuccessful = new MutableLiveData<>();
    private final MutableLiveData<List<user>> users = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        users.setValue(userRepository.getAllUsers());
    }

    public LiveData<Boolean> getSignUpSuccessful() {
        return signUpSuccessful;
    }

    public LiveData<List<user>> getUsers(){return users;}

    public void signUp(user newUser) {
        userRepository.insertUser(newUser);
        signUpSuccessful.setValue(true);
    }
}