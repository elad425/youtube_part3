package com.example.youtube.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.youtube.AppDatabase;
import com.example.youtube.Daos.userDao;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.UserSession;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private AppDatabase db;
    private MutableLiveData<List<video>> videos;
    private int userId;

    public MainViewModel(Application application) {
        super(application);
        db = Room.databaseBuilder(application.getApplicationContext(),
                AppDatabase.class, "userDb").allowMainThreadQueries().build();
        videos = new MutableLiveData<>();
        userId = UserSession.getInstance().getUserId();
        initializeData();
    }

    public void initializeData() {
        userDao userDao = db.userDao();
        if (userDao.getAllUsers().isEmpty()) {
            ArrayList<user> tempUser = JsonUtils.loadUsersFromJson(getApplication());
            for (user u : tempUser) {
                userDao.insert(u);
            }
        }

        videoDao videoDao = db.videoDao();
        if (videoDao.getAllVideos().isEmpty()) {
            ArrayList<video> tempVideo = JsonUtils.loadVideosFromJson(getApplication());
            for (video v : tempVideo) {
                videoDao.insert(v);
            }
        }

        loadVideos();
    }

    public void loadVideos() {
        videoDao videoDao = db.videoDao();
        List<video> videoList = videoDao.getAllVideos();
        videos.postValue(videoList);
    }

    public LiveData<List<video>> getVideos() {
        return videos;
    }

    public int getUserId() {
        return userId;
    }

    public AppDatabase getDatabase() {
        return db;
    }
}
