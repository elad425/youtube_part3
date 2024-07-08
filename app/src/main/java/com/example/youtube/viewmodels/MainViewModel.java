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
    private final LiveData<List<video>> allVideos;
    private final List<user> allUsers;

    public MainViewModel(Application application) {
        super(application);
        VideoRepository videoRepository = new VideoRepository(application);
        UserRepository userRepository = new UserRepository(application);
        allVideos = videoRepository.getAllVideosLive();
        allUsers = userRepository.getAllUsers();
    }

    public LiveData<List<video>> getAllVideos() {
        return allVideos;
    }

    public List<user> getAllUsers(){ return allUsers;}
}