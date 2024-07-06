package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.youtube.utils.userConverters;

import java.util.ArrayList;
@Entity(tableName = "users")
public class user {
    @PrimaryKey
    private int id;
    private String name;
    private String email;
    private String password;
    private String profile_pic;
    @TypeConverters({userConverters.class})
    private ArrayList<Integer> likedVideos;
    @TypeConverters({userConverters.class})
    private ArrayList<Integer> dislikedVideos;
    @TypeConverters({userConverters.class})
    private ArrayList<Integer> subs;
    private String subs_count;

    public user(int id, String name, String email, String password, String profile_pic, String subs_count) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_pic = profile_pic;
        likedVideos = new ArrayList<>();
        dislikedVideos = new ArrayList<>();
        subs = new ArrayList<>();
        this.subs_count = subs_count;
    }

    public user() {
        likedVideos = new ArrayList<>();
        dislikedVideos = new ArrayList<>();
        subs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public ArrayList<Integer> getLikedVideos() {
        return likedVideos;
    }

    public void setLikedVideos(ArrayList<Integer> likedVideos) {
        this.likedVideos = likedVideos;
    }

    public ArrayList<Integer> getDislikedVideos() {
        return dislikedVideos;
    }

    public void setDislikedVideos(ArrayList<Integer> dislikedVideos) {
        this.dislikedVideos = dislikedVideos;
    }

    public ArrayList<Integer> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<Integer> subs) {
        this.subs = subs;
    }

    public String getSubs_count() {
        return subs_count;
    }

    public void setSubs_count(String subs_count) {
        this.subs_count = subs_count;
    }

    public void addToLiked(video video) {
        for (Integer s : likedVideos) {
            if (s == video.getId()) {
                return;
            }
        }
        this.likedVideos.add(video.getId());
    }

    public void removeFromLiked(video video) {
        likedVideos.removeIf(s -> s == video.getId());
    }

    public void addToDisLiked(video video) {
        for (Integer s : dislikedVideos) {
            if (s == video.getId()) {
                return;
            }
        }
        this.dislikedVideos.add(video.getId());
    }

    public void removeFromDisLiked(video video) {
        dislikedVideos.removeIf(s -> s == video.getId());
    }

    public void addToSubs(user creator) {
        this.subs.add(creator.getId());
    }

    public void removeFromSubs(user creator) {
        subs.removeIf(c -> c == creator.getId());
    }

    public boolean isLiked(video video) {
        if(likedVideos != null) {
            for (Integer s : likedVideos) {
                if (s == video.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDisLiked(video video) {
        if(dislikedVideos != null) {
            for (Integer s : dislikedVideos) {
                if (s == video.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSubs(user creator) {
        if(subs != null) {
            for (Integer c : subs) {
                if (c == creator.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
