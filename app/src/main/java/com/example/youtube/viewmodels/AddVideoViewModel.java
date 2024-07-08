package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class AddVideoViewModel extends AndroidViewModel {
    private final VideoRepository videoRepository;
    private final MutableLiveData<Boolean> videoAddedSuccessfully = new MutableLiveData<>();

    public AddVideoViewModel(@NonNull Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
    }

    public LiveData<Boolean> getVideoAddedSuccessfully() {
        return videoAddedSuccessfully;
    }

    public void addVideo(Video newVideo) {
        videoRepository.insertVideo(newVideo);
        videoAddedSuccessfully.setValue(true);
    }
    public List<Video> getVideos(){
        return videoRepository.getAllVideos();
    }
}