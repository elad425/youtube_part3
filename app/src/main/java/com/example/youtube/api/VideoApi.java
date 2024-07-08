package com.example.youtube.api;

import androidx.annotation.NonNull;

import com.example.youtube.entities.Comment;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.Video;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoApi {
    private final videoDao dao;
    Retrofit retrofit;
    videoWebServiceApi videoWebServiceApi;

    public VideoApi(videoDao dao) {
        this.dao = dao;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.30:5000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        videoWebServiceApi = retrofit.create(videoWebServiceApi.class);
    }

    public void getVideos() {
        Call<List<Video>> call = videoWebServiceApi.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                dao.clear();
                dao.insertList(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {}
        });
    }

    public void createVideo(Video newvideo) {
        Call<Void> call = videoWebServiceApi.createVideo(newvideo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateVideo(String videoId, Video updatedvideo) {
        Call<Void> call = videoWebServiceApi.updateVideo(updatedvideo.getUserDetails().get_id(),videoId,updatedvideo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteVideo(String videoId) {
        Call<Void> call = videoWebServiceApi.deleteVideo(videoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void getCommentsById(String id, final ApiCallback<List<Comment>> callback){
        Call<List<Comment>> call = videoWebServiceApi.getCommentById(id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get image: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
            }
        });
    }

    public void createComment(Comment comment) {
        Call<Void> call = videoWebServiceApi.createComment(comment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteComment(String commentId) {
        Call<Void> call = videoWebServiceApi.deleteComment(commentId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public void updateComment(Comment comment) {
        Call<Void> call = videoWebServiceApi.updateVideo(comment.get_id(),comment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }
}
