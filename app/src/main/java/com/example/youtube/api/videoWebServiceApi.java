package com.example.youtube.api;

import com.example.youtube.entities.Comment;

import com.example.youtube.entities.UserDetails;

import com.example.youtube.entities.Video;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface videoWebServiceApi {

    @GET("api/videos/")
    Call<List<Video>> getVideos();

    @GET("api/videos/{id}")
    Call<Video> getVideoById(@Path("id") String id);

    @POST("api/users/{id}/videos")
    Call<Void> createVideo(@Path("id") String userId, @Body Video video, @Header("Authorization") String token);
  
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadVideo(@Part MultipartBody.Part file, @Header("Authorization") String token);
  
    @DELETE("api/users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Path("id") String id, @Path("pid") String pid,@Header("Authorization") String token);

    @PUT("api/users/{id}/videos/{pid}")
    Call<Void> updateVideo(@Path("id") String id, @Path("pid") String pid  ,@Body Video video,@Header("Authorization") String token);



}