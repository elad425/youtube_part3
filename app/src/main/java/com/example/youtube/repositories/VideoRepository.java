package com.example.youtube.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Comment;
import com.example.youtube.entities.Comment2;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import com.example.youtube.api.VideoApi;
import com.example.youtube.data.AppDatabase;
import java.util.List;

public class VideoRepository {
    private final AppDatabase db;
    private final VideoApi videoApi;

    private final LiveData<List<Video>> videos;
    private final MutableLiveData<List<Comment>> comments;
    private final MutableLiveData<Comment> addedComment;

    public VideoRepository(Application application) {
        db = AppDatabase.getInstance(application);
        videoApi = new VideoApi(db.videoDao(),application.getApplicationContext());
        videoApi.getVideos();
        videos = db.videoDao().getAllVideosLive();
        comments = new MutableLiveData<>();
        addedComment = new MutableLiveData<>();
    }

    public void insertVideo(Video newVideo) {
        db.videoDao().insert(newVideo);
        videoApi.createVideo(newVideo);
    }

    public void updateVideo(Video updatedVideo) {
        db.videoDao().update(updatedVideo);
        videoApi.updateVideo(updatedVideo.get_id(), updatedVideo);
    }

    public void deleteVideo(Video videoToDelete) {
        db.videoDao().delete(videoToDelete);
        videoApi.deleteVideo(videoToDelete.get_id());
    }

    public LiveData<List<Video>> getAllVideosLive() {
        return videos;
    }

    public List<Video> getAllVideos() {
        return db.videoDao().getAllVideos();
    }

    public Video getVideoById(String id) {
        return db.videoDao().getVideoById(id);
    }

    public MutableLiveData<List<Comment>> getCommentByVideoId(String videoId) {
        videoApi.getCommentsById(videoId, new VideoApi.ApiCallback<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> result) {
                comments.setValue(result);
            }

            @Override
            public void onError(String error) {
            }
        });
        return comments;
    }

    public void addComment(Comment comment, User user){
        videoApi.createComment(comment, new VideoApi.ApiCallback<Comment2>() {
            @Override
            public void onSuccess(Comment2 result) {
                Comment comment1 = result.commentFormatConvert(result,user);
                addedComment.setValue(comment1);
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    public MutableLiveData<Comment> getAddedComment() {return addedComment;}

    public void deleteComment(Comment comment){
        videoApi.deleteComment(comment.get_id());
    }

    public void updateComment(Comment comment){
        videoApi.updateComment(comment);
    }

}