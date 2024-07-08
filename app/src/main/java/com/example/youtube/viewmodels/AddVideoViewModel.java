package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.video;
import com.example.youtube.repositories.VideoRepository;

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

    public void addVideo(video newVideo) {
        videoRepository.insertVideo(newVideo);
        videoAddedSuccessfully.setValue(true);
    }
}