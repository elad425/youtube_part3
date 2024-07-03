package com.example.youtube.utils;

import android.content.Context;
import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static ArrayList<video> loadVideosFromJson(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.videoinfo);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Type videoListType = new TypeToken<ArrayList<video>>() {}.getType(); // Changed to ArrayList
        return gson.fromJson(reader, videoListType); // Changed return type
    }

    public static List<user> loadUsersFromJson(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.userinfo);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Type userListType = new TypeToken<List<user>>() {}.getType();
        return gson.fromJson(reader, userListType);
    }

}
