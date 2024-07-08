package com.example.youtube.api;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.youtube.Daos.imgDao;
import com.example.youtube.entities.Image;
import com.example.youtube.entities.User;
import com.example.youtube.utils.GeneralUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaApi {
    Retrofit retrofit;
    static mediaWebServiceApi mediaWebServiceApi;
    private final imgDao dao;
    private final Context context;

    public MediaApi(imgDao imgDao, Context context) {
        dao = imgDao;
        this.context = context;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.68.113:5000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        mediaWebServiceApi = retrofit.create(mediaWebServiceApi.class);
    }

    public void getProfileImage(String path) {
        Call<ResponseBody> call = mediaWebServiceApi.getProfileImage(GeneralUtils.getStringAfterBackslash(path));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    Image i = new Image(response.body().bytes(),path);
                    dao.insert(i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getThumbnail(String path) {
        String temp = GeneralUtils.extractTextAfterLastSlash(path);
        Call<ResponseBody> call = mediaWebServiceApi.getThumbnail(temp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    Image i = new Image(response.body().bytes(),path);
                    dao.insert(i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void downloadVideo(String videoId, final MediaApi.ApiCallback<byte[]> callback) {

        Call<ResponseBody> call = mediaWebServiceApi.downloadVideo(GeneralUtils.extractTextAfterLastSlash(videoId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        byte[] videoBytes = null;
                        try {
                            videoBytes = body.bytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (response.isSuccessful()) {
                            callback.onSuccess(videoBytes);
                        } else {
                            callback.onError("Failed to get image: " + response.message());
                        }
                    }
                } else {
                    // Handle error
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network error
                Log.e("API", "Network error: " + t.getMessage());
            }
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

}
