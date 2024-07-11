package com.example.youtube.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.youtube.data.UserSession;
import com.example.youtube.entities.Image;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.MediaRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;
import java.util.Objects;

public class MainViewModel extends AndroidViewModel {
    private final VideoRepository videoRepository;
    private final MediaRepository mediaRepository;

    public MainViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        mediaRepository = new MediaRepository(application);
    }

    public LiveData<List<Video>> getAllVideosLive() {
        return videoRepository.getAllVideosLive();
    }

    public MediaRepository getMediaRepository(){ return mediaRepository;}

    public void initImages(){
        mediaRepository.initVideoMedia();
    }

    public LiveData<List<Image>> getAllImagesLive(){
        return mediaRepository.getAllImagesLive();
    }

    public void reload(){
        videoRepository.reloadVideos();
    }

    public Bitmap getBitmap(User user) {
        return mediaRepository.getImage(user.getIcon());
    }

}