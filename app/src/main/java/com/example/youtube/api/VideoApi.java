package com.example.youtube.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.youtube.R;
import com.example.youtube.data.UserSession;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.Video;
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

    public VideoApi(videoDao dao, Context context) {
        this.dao = dao;
        retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create()).build();
        videoWebServiceApi = retrofit.create(videoWebServiceApi.class);
    }

    public void getVideos() {
        Call<List<Video>> call = videoWebServiceApi.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                dao.clear();
                dao.insertList(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {}
        });
    }

    public void createVideo(Video newvideo) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.createVideo(newvideo,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateVideo(String videoId, Video updatedvideo) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.updateVideo(updatedvideo.getUserDetails().get_id(),videoId,updatedvideo,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteVideo(String videoId) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.deleteVideo(videoId,token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
}
