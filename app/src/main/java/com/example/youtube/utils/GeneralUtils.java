package com.example.youtube.utils;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtube.adapters.VideoListAdapter;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;

import java.util.ArrayList;
import java.util.Objects;

public class GeneralUtils {

    public static void displayVideoList(Context context, RecyclerView lstVideos,
                                        ArrayList<video> videos, user user, video filter, ArrayList<user> users) {
        final VideoListAdapter adapter = new VideoListAdapter(context, user);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(context));
        adapter.setVideos(videos, users);
        adapter.filter(filter);
    }

    public static int findVideoPlace (ArrayList<video> videos, video video){
        int counter = 0;
        for (video v : videos){
            if (Objects.equals(v.getVideo_name(), video.getVideo_name())){
                return counter;
            }
            counter++;
        }
        return counter;
    }

    public static void updateUsers(ArrayList<user> users, user user){
        int position = 0;
        while(position!= users.size()){
            if (users.get(position).getEmail().equals(user.getEmail())){
                users.set(position,user);
            }
            position += 1;
        }
    }

    public static boolean isUserExist(ArrayList<user> users, String email){
        if (users == null){
            return false;
        }
        int position = 0;
        while(position!= users.size()){
            if (users.get(position).getEmail().equals(email)){
                return true;
            }
            position += 1;
        }
        return false;
    }

}
