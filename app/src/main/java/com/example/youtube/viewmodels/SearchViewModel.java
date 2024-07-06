package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final MutableLiveData<List<video>> filteredVideos = new MutableLiveData<>();
    private final VideoRepository videoRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
    }

    public LiveData<List<video>> getFilteredVideos() {
        return filteredVideos;
    }

    public void filterVideos(String query) {
        List<video> allVideos = videoRepository.getAllVideos();
        List<video> filtered = new ArrayList<>();
        if (!query.isEmpty()) {
            for (video video : allVideos) {
                if (video.getVideo_name().toLowerCase().startsWith(query.toLowerCase())) {
                    filtered.add(video);
                }
            }
        }
        filteredVideos.setValue(filtered);
    }
}