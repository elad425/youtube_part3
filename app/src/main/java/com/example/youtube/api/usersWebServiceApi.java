package com.example.youtube.api;

import com.example.youtube.entities.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface usersWebServiceApi {
    @GET("api/users/")
    Call<List<User>> getUsers();
    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") String id);
    @POST("api//users")
    Call<Void> createUser(@Body User user);
    @DELETE("api/users//{id}")
    Call<Void> deleteUser(@Path("id") String id);
    @PUT("api/users//{id}")
    Call<Void> updateUser(@Path("id") String id ,@Body User user);

}

