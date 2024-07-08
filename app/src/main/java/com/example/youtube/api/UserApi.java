package com.example.youtube.api;

import androidx.annotation.NonNull;

import com.example.youtube.entities.User;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    Retrofit retrofit;
    usersWebServiceApi usersWebServiceApi;

    public UserApi() {
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.68.113:5000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        usersWebServiceApi = retrofit.create(usersWebServiceApi.class);
    }

    public void getUsers(final UserApi.ApiCallback<List<User>> callback){
        Call<List<User>> call = usersWebServiceApi.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get image: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
            }
        });
    }

    public void getUserById(String id, final ApiCallback<User> callback){
        Call<User> call = usersWebServiceApi.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get image: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
            }
        });
    }

    public void createUser(User newUser) {
        Call<Void> call = usersWebServiceApi.createUser(newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateUser(String userId, User updatedUser) {
        Call<Void> call = usersWebServiceApi.updateUser(userId,updatedUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

}