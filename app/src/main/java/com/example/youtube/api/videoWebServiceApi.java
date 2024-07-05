package com.example.youtube.api;

import com.example.youtube.entities.video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface videoWebServiceApi {
    @GET("videos")
    Call<List<video>> getVideos();
    @POST("videos")
    Call<Void> createVideo(@Body video video);
    @DELETE("videos/{id}")
    Call<Void> deleteVideo(@Path("id") int id);
    @PUT("videos/{id}")
    Call<Void> updateVideo(@Path("id") int id ,@Body video video);
}