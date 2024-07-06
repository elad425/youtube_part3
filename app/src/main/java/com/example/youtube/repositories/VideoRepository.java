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

    private final LiveData<List<video>> videos;

    public VideoRepository(Application application) {
        db = AppDatabase.getInstance(application);
        api = new VideoApi(db.videoDao());
        api.getVideos();
        videos = db.videoDao().getAllVideosLive();
    }

    public void insertVideo(video newVideo) {
        db.videoDao().insert(newVideo);
        api.createVideo(newVideo);
    }

    public void updateVideo(video updatedVideo) {
        db.videoDao().update(updatedVideo);
        api.updateVideo(updatedVideo.getId(), updatedVideo);
    }

    public void deleteVideo(video videoToDelete) {
        db.videoDao().delete(videoToDelete);
        api.deleteVideo(videoToDelete.getId());
    }

    public LiveData<List<video>> getAllVideosLive() {
        return videos;
    }

    public List<video> getAllVideos() {
        return db.videoDao().getAllVideos();
    }

    public video getVideoById(int id) {
        return db.videoDao().getVideoById(id);
    }
}