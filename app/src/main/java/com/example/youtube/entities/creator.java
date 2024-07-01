package com.example.youtube.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class creator implements Parcelable {
    private String name;
    private String subs_count;
    private String profile_pic;

    public creator(String name, String subsCount, String profilePic) {
        this.name = name;
        subs_count = subsCount;
        profile_pic = profilePic;
    }

    protected creator(Parcel in) {
        name = in.readString();
        subs_count = in.readString();
        profile_pic = in.readString();
    }

    public static final Creator<creator> CREATOR = new Creator<creator>() {
        @Override
        public creator createFromParcel(Parcel in) {
            return new creator(in);
        }

        @Override
        public creator[] newArray(int size) {
            return new creator[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubs_count() {
        return subs_count;
    }

    public void setSubs_count(String subs_count) {
        this.subs_count = subs_count;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(subs_count);
        dest.writeString(profile_pic);
    }
}
