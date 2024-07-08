package com.example.youtube.utils;

import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GeneralUtils {

    public static boolean isUserExist(List<User> users, String email){
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
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date givenDate;

        if (dateString == null){
            return "just now";
        }

        try {
            givenDate = dateFormat1.parse(dateString);
        } catch (ParseException e) {
            try {
                givenDate = dateFormat2.parse(dateString);
            } catch (ParseException t){
                return "invalid date";
            }
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

        return "today";
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
            return "Invalid number";
        }
    }

    public static List<Video> removeVideo(List<Video> videos, Video video){
        List<Video> temp = new ArrayList<>();
        if (video == null){
            return videos;
        }
        for (Video v : videos){
            if (!Objects.equals(v.get_id(), video.get_id())){
                temp.add(v);
            }
        }
        return temp;
    }

    public static String getStringAfterBackslash(String input) {
        int index = input.lastIndexOf('\\');
        if (index != -1 && index < input.length() - 1) {
            return input.substring(index + 1);
        } else {
            return "";
        }
    }

    public static boolean isUserExist(ArrayList<User> users, String email){
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

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            sb.append(random.nextInt(6));
        }
        sb.append(":");
        for (int i = 0; i < 2; i++) {
            sb.append(random.nextInt(6));
        }
        return sb.toString();
    }

    public static String extractTextAfterLastSlash(String input) {
        if (input == null || !input.contains("/")) {
            return input;
        }

        int lastSlashIndex = input.lastIndexOf('/');
        return input.substring(lastSlashIndex + 1);
    }

    public static String removeTrailingNewline(String str) {
        if (str != null && str.endsWith("\n")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }
}
