package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.youtube.utils.userConverters;

import java.util.ArrayList;
import java.util.Objects;
@Entity(tableName = "users")
public class user {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String password;
    private String profile_pic;
    @TypeConverters({userConverters.class})
    private ArrayList<String> likedVideos;
    @TypeConverters({userConverters.class})
    private ArrayList<String> dislikedVideos;
    @TypeConverters({userConverters.class})
    private ArrayList<String> subs;
    private String subs_count;

    public user(String name, String email, String password, String profile_pic, String subs_count) {
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

    public ArrayList<String> getLikedVideos() {
        return likedVideos;
    }

    public void setLikedVideos(ArrayList<String> likedVideos) {
        this.likedVideos = likedVideos;
    }

    public ArrayList<String> getDislikedVideos() {
        return dislikedVideos;
    }

    public void setDislikedVideos(ArrayList<String> dislikedVideos) {
        this.dislikedVideos = dislikedVideos;
    }

    public ArrayList<String> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<String> subs) {
        this.subs = subs;
    }

    public String getSubs_count() {
        return subs_count;
    }

    public void setSubs_count(String subs_count) {
        this.subs_count = subs_count;
    }

    public void addToLiked(video video) {
        for (String s : likedVideos) {
            if (s.equals(video.getVideo_name())) {
                return;
            }
        }
        this.likedVideos.add(video.getVideo_name());
    }

    public void removeFromLiked(video video) {
        likedVideos.removeIf(s -> s.equals(video.getVideo_name()));
    }

    public void addToDisLiked(video video) {
        for (String s : dislikedVideos) {
            if (s.equals(video.getVideo_name())) {
                return;
            }
        }
        this.dislikedVideos.add(video.getVideo_name());
    }

    public void removeFromDisLiked(video video) {
        dislikedVideos.removeIf(s -> s.equals(video.getVideo_name()));
    }

    public void addToSubs(user creator) {
        this.subs.add(creator.name);
    }

    public void removeFromSubs(user creator) {
        subs.removeIf(c -> Objects.equals(c, creator.getName()));
    }

    public boolean isLiked(video video) {
        if(likedVideos != null) {
            for (String s : likedVideos) {
                if (s.equals(video.getVideo_name())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDisLiked(video video) {
        if(dislikedVideos != null) {
            for (String s : dislikedVideos) {
                if (s.equals(video.getVideo_name())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSubs(user creator) {
        if(subs != null) {
            for (String c : subs) {
                if (Objects.equals(c, creator.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
