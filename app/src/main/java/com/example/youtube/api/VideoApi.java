package com.example.youtube.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.youtube.R;
import com.example.youtube.data.UserSession;
import com.example.youtube.entities.Comment;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.Comment2;
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

    public VideoApi(videoDao dao, Context context) {
        this.dao = dao;
        retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.baseUrl))
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

    public void getVideoById(String id, final ApiCallback<Video> callback){
        Call<Video> call = videoWebServiceApi.getVideoById(id);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get image: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
            }
        });
    }

    public void createVideo(Video newvideo) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.createVideo(newvideo,token);
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
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.updateVideo(updatedvideo.getUserDetails().get_id(),videoId,updatedvideo,token);
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
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.deleteVideo(videoId,token);
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
                    callback.onError(response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
            }
        });
    }

    public void createComment(Comment comment, final ApiCallback<Comment2> callback) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Comment2> call = videoWebServiceApi.createComment(comment,token);
        call.enqueue(new Callback<Comment2>() {
            @Override
            public void onResponse(@NonNull Call<Comment2> call, @NonNull Response<Comment2> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Comment2> call, @NonNull Throwable t) {
            }
        });
    }

    public void deleteComment(String commentId) {
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.deleteComment(commentId,token);
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
        String token = "Bearer " + UserSession.getInstance().getToken();
        Call<Void> call = videoWebServiceApi.updateVideo(comment.get_id(),comment,token);
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
