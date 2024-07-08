package com.example.youtube.api;

import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface videoWebServiceApi {
    @GET("api/videos/")
    Call<List<Video>> getVideos();
    @POST("api/videos")
    Call<Void> createVideo(@Body Video video);
    @DELETE("api/videos//{id}")
    Call<Void> deleteVideo(@Path("id") String id);
    @PUT("api/videos//{id}")
    Call<Void> updateVideo(@Path("id") String id ,@Body Video video);
    @GET("api/videos/comment/{id}")
    Call<List<Comment>> getCommentById(@Path("id") String id);

}