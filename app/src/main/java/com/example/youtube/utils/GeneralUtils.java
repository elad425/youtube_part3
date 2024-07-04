package com.example.youtube.utils;

import com.example.youtube.entities.user;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
