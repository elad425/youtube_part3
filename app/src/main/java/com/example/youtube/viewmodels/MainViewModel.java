package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.youtube.entities.video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final LiveData<List<video>> allVideos;

    public MainViewModel(Application application) {
        super(application);
        VideoRepository videoRepository = new VideoRepository(application);
        UserRepository userRepository = new UserRepository(application);
        allVideos = videoRepository.getAllVideosLive();
    }

    public LiveData<List<video>> getAllVideos() {
        return allVideos;
    }
}