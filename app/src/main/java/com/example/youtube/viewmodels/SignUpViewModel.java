package com.example.youtube.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.user;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.utils.GeneralUtils;

import java.util.List;

public class SignUpViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Boolean> signUpSuccessful = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getSignUpSuccessful() {
        return signUpSuccessful;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signUp(String username, String email, String password, String confirmPassword, Uri imageUri) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage.setValue("All fields are required");
            return;
        }

        if (username.length() >= 20) {
            errorMessage.setValue("Username too long, must be under 20 letters");
            return;
        }

        List<user> users = userRepository.getAllUsers();
        if (GeneralUtils.isUserExist(users, email)) {
            errorMessage.setValue("This email already exists");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Please enter a valid email address");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("Passwords do not match");
            return;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            errorMessage.setValue("Password must be at least 8 characters long and contain at least one number");
            return;
        }

        if (imageUri == null) {
            errorMessage.setValue("Please upload an image");
            return;
        }

        user newUser = new user(username, email, password, imageUri.toString(), "0");
        userRepository.insertUser(newUser);
        signUpSuccessful.setValue(true);
    }
}