package com.example.youtube.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.example.youtube.entities.user;
import com.example.youtube.entities.video;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GeneralUtils {

    public static boolean isUserExist(List<user> users, String email){
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
        assert givenDate != null;
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

    public static user getUserById(List<user> users, int id){
        for (user u : users){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    public static List<video> removeVideo(List<video> videos, video video){
        List<video> temp = new ArrayList<>();
        if (video == null){
            return videos;
        }
        for (video v : videos){
            if (v.getId() != video.getId()){
                temp.add(v);
            }
        }
        return temp;
    }

    public static String getVideoDuration(Uri videoUri, Context context) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, videoUri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            assert time != null;
            long timeInMilliSec = Long.parseLong(time);
            long minutes = (timeInMilliSec / 1000) / 60;
            long seconds = (timeInMilliSec / 1000) % 60;
            return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        } finally {
            retriever.release();
        }
    }

    public static int videoIdGenerator(List<video> videos){
        int i = 1;
        for (video v : videos){
            if (v.getId() != i){ break;}
            i+=1;
        }
        return i;
    }

    public static int userIdGenerator(List<user> users){
        int i = 1;
        for (user u : users){
            if (u.getId() != i){ break;}
            i+=1;
        }
        return i;
    }
}
