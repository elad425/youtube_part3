package com.example.youtube.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.User;
import com.example.youtube.repositories.MediaRepository;
import com.example.youtube.repositories.UserRepository;

import java.util.List;

public class SignUpViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final MutableLiveData<Boolean> signUpSuccessful = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        mediaRepository = new MediaRepository(application);
        users.setValue(userRepository.getAllUsers());
    }

    public LiveData<Boolean> getSignUpSuccessful() {
        return signUpSuccessful;
    }

    public LiveData<List<User>> getUsers(){return users;}

    public void signUp(User newUser) {
        mediaRepository.uploadProfileImg(newUser.getIcon());
        userRepository.insertUser(newUser);
        signUpSuccessful.setValue(true);
    }
}