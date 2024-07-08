package com.example.youtube.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.youtube.data.AppDatabase;
import com.example.youtube.entities.video;
import com.example.youtube.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private final AppDatabase db;

    public VideoRepository(Application application) {
        db = AppDatabase.getInstance(application);
        loadVideos(application.getApplicationContext());
    }

    public void insertVideo(video newVideo) {
        db.videoDao().insert(newVideo);
    }

    public void updateVideo(video updatedVideo) {
        db.videoDao().update(updatedVideo);
    }

    public void deleteVideo(video videoToDelete) {
        db.videoDao().delete(videoToDelete);
    }

    public LiveData<List<video>> getAllVideosLive() {
        return db.videoDao().getAllVideosLive();
    }

    public List<video> getAllVideos() {
        return db.videoDao().getAllVideos();
    }

    private void loadVideos(Context context) {
        if (db.videoDao().getAllVideos().isEmpty()) {
            ArrayList<video> tempVideo = JsonUtils.loadVideosFromJson(context);
            for (video v : tempVideo) {
                db.videoDao().insert(v);
            }
        }
    }

    public video getVideoById(int id) {
        return db.videoDao().getVideoById(id);
    }
}