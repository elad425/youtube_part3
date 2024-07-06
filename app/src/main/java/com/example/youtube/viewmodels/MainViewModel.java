package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public MainViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<List<video>> getAllVideosLive() {
        return videoRepository.getAllVideosLive();
    }

    public LiveData<List<user>> getAllUsersLive(){
        return userRepository.getAllUsersLive();}

}