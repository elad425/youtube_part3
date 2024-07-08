package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.youtube.entities.Image;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.MediaRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        mediaRepository.initImages();
    }

    public LiveData<List<Image>> getAllImagesLive(){
        return mediaRepository.getAllImagesLive();
    }

}