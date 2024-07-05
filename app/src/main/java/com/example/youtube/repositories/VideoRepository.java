package com.example.youtube.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.youtube.api.VideoApi;
import com.example.youtube.data.AppDatabase;
import com.example.youtube.entities.video;

import java.util.List;

public class VideoRepository {
    private final AppDatabase db;
    private final VideoApi api;

    public VideoRepository(Application application) {
        db = AppDatabase.getInstance(application);
        api = new VideoApi(db.videoDao());
        api.getVideos();
    }

    public void insertVideo(video newVideo) {
        db.videoDao().insert(newVideo);
        api.createVideo(newVideo, new VideoApi.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle successful user creation
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    public void updateVideo(video updatedVideo) {
        db.videoDao().update(updatedVideo);
        api.updateVideo(updatedVideo.getVideoId(), updatedVideo, new VideoApi.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle successful user update
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    public void deleteVideo(video videoToDelete) {
        db.videoDao().delete(videoToDelete);
        api.deleteVideo(videoToDelete.getVideoId(), new VideoApi.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // Handle successful user update
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    public LiveData<List<video>> getAllVideosLive() {
        return db.videoDao().getAllVideosLive();
    }

    public List<video> getAllVideos() {
        return db.videoDao().getAllVideos();
    }

    public video getVideoById(int id) {
        return db.videoDao().getVideoById(id);
    }
}