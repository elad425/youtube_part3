package com.example.youtube.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.api.UserApi;
import com.example.youtube.entities.LoginResponse;
import com.example.youtube.entities.User;
import com.example.youtube.data.UserSession;

public class LoginViewModel extends AndroidViewModel {
    private final UserApi userApi;
    private final MutableLiveData<Boolean> loginSuccessful = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userApi = new UserApi(application.getApplicationContext());
    }

    public LiveData<Boolean> getLoginSuccessful() {
        return loginSuccessful;
    }

    public void login(String email,String password) {
        userApi.login(email,password, new UserApi.ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
                UserSession.getInstance().setUser(result.getUser());
                getApplication().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                        .edit()
                        .putString("token", result.getToken())
                        .apply();
                UserSession.getInstance().setToken(result.getToken());
                loginSuccessful.setValue(true);
            }

            @Override
            public void onError(String error) {
                loginSuccessful.setValue(false);
            }
        });
    }
    public void validateToken(String token) {
        userApi.validateToken(token, new UserApi.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                UserSession.getInstance().setUser(result);
                loginSuccessful.setValue(true);
            }

            @Override
            public void onError(String error) {
                // Handle error
                loginSuccessful.setValue(false);
            }
        });
    }
}