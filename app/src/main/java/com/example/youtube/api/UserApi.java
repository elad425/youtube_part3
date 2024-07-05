package com.example.youtube.api;

import androidx.annotation.NonNull;

import com.example.youtube.Daos.userDao;
import com.example.youtube.entities.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private final userDao dao;
    Retrofit retrofit;
    usersWebServiceApi usersWebServiceApi;

    public UserApi(userDao dao) {
        this.dao = dao;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.68.117:3000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        usersWebServiceApi = retrofit.create(usersWebServiceApi.class);
    }

    public void get() {
        Call<List<user>> call = usersWebServiceApi.getUsers();
        call.enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(@NonNull Call<List<user>> call, @NonNull Response<List<user>> response) {
                dao.clear();
                dao.insertList(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<user>> call, @NonNull Throwable t) {}
        });
    }

    public void createUser(user newUser, final ApiCallback<Void> callback) {
        Call<Void> call = usersWebServiceApi.createUser(newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to create user: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateUser(int userId, user updatedUser, final ApiCallback<Void> callback) {
        Call<Void> call = usersWebServiceApi.updateUser(userId,updatedUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to update user: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
}