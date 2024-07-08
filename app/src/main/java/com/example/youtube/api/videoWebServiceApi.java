package com.example.youtube.api;

import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface videoWebServiceApi {
    @GET("api/videos/")
    Call<List<Video>> getVideos();
    @POST("api/videos")
    Call<Void> createVideo(@Body Video video);
    @DELETE("api/videos/{id}")
    Call<Void> deleteVideo(@Path("id") String id);
    @PUT("api/users/{id}/videos/{pid}")
    Call<Void> updateVideo(@Path("id") String id, @Path("pid") String pid  ,@Body Video video);
    @GET("api/videos/comment/{id}")
    Call<List<Comment>> getCommentById(@Path("id") String id);
    @POST("api/videos/comment")
    Call<Void> createComment(@Body Comment comment);
    @DELETE("api/videos/comment/{id}")
    Call<Void> deleteComment(@Path("id") String id);
    @PATCH("api/videos/comment/{id}")
    Call<Void> updateVideo(@Path("id") String id,@Body Comment comment);

}