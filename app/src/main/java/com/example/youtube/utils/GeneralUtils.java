package com.example.youtube.utils;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtube.adapters.VideoListAdapter;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        if (users != null) {
            int position = 0;
            while (position != users.size()) {
                if (users.get(position).getEmail().equals(user.getEmail())) {
                    users.set(position, user);
                }
                position += 1;
            }
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

    public static String timeAgo(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date givenDate;

        try {
            givenDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date format";
        }

        Date currentDate = new Date();
        long diffInMillis = currentDate.getTime() - givenDate.getTime();

        long years = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 365;
        if (years > 0) {
            return years + " years ago";
        }

        long months = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 30;
        if (months > 0) {
            return months + " months ago";
        }

        long weeks = TimeUnit.MILLISECONDS.toDays(diffInMillis) / 7;
        if (weeks > 0) {
            return weeks + " weeks ago";
        }

        long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        if (days > 0) {
            return days + " days ago";
        }

        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        if (hours > 0) {
            return hours + " hours ago";
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        if (minutes > 0) {
            return minutes + " minutes ago";
        }

        return "Just now";
    }

    public static String getTheDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public static String getViews(String numberStr) {
        try {
            long number = Long.parseLong(numberStr);
            if (number >= 1_000_000_000) {
                return number / 1_000_000_000 + "B";
            } else if (number >= 1_000_000) {
                return number / 1_000_000 + "M";
            } else if (number >= 1_000) {
                return number / 1_000 + "K";
            } else {
                return String.valueOf(number);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid number";
        }
    }
}
