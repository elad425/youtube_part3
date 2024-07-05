package com.example.youtube.api;

import androidx.annotation.NonNull;

import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoApi {
    private final videoDao dao;
    Retrofit retrofit;
    videoWebServiceApi videoWebServiceApi;

    public VideoApi(videoDao dao) {
        this.dao = dao;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.68.117:3000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        videoWebServiceApi = retrofit.create(videoWebServiceApi.class);
    }

    public void getVideos() {
        Call<List<video>> call = videoWebServiceApi.getVideos();
        call.enqueue(new Callback<List<video>>() {
            @Override
            public void onResponse(@NonNull Call<List<video>> call, @NonNull Response<List<video>> response) {
                dao.clear();
                dao.insertList(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<video>> call, @NonNull Throwable t) {}
        });
    }

    public void createVideo(video newvideo, final VideoApi.ApiCallback<Void> callback) {
        Call<Void> call = videoWebServiceApi.createVideo(newvideo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to create video: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateVideo(int videoId, video updatedvideo, final VideoApi.ApiCallback<Void> callback) {
        Call<Void> call = videoWebServiceApi.updateVideo(videoId,updatedvideo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to update video: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void deleteVideo(int videoId, final VideoApi.ApiCallback<Void> callback) {
        Call<Void> call = videoWebServiceApi.deleteVideo(videoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to update video: " + response.message());
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
