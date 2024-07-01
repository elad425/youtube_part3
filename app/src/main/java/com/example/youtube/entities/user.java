package com.example.youtube.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class user implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String profile_pic;
    private ArrayList<String> likedVideos;
    private ArrayList<String> dislikedVideos;
    private ArrayList<creator> subs;

    public user(String name, String email, String password, String profile_pic) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_pic = profile_pic;
        likedVideos = new ArrayList<>();
        dislikedVideos = new ArrayList<>();
        subs = new ArrayList<>();
    }

    protected user(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        profile_pic = in.readString();
        likedVideos = in.createStringArrayList();
        dislikedVideos = in.createStringArrayList();
        subs = in.createTypedArrayList(creator.CREATOR);
    }

    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };

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

    public ArrayList<creator> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<creator> subs) {
        this.subs = subs;
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

    public void addToSubs(creator creator) {
        this.subs.add(creator);
    }

    public void removeFromSubs(creator creator) {
        subs.removeIf(c -> Objects.equals(c.getName(), creator.getName()));
    }

    public boolean isLiked(video video) {
        for (String s : likedVideos) {
            if (s.equals(video.getVideo_name())) {
                return true;
            }
        }
        return false;
    }

    public boolean isDisLiked(video video) {
        for (String s : dislikedVideos) {
            if (s.equals(video.getVideo_name())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSubs(creator creator) {
        for (creator c : subs) {
            if (Objects.equals(c.getName(), creator.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(profile_pic);
        dest.writeStringList(likedVideos);
        dest.writeStringList(dislikedVideos);
        dest.writeTypedList(subs);
    }
}
