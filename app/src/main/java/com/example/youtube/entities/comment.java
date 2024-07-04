package com.example.youtube.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class comment implements Parcelable {
    private String comment;
    private int userId;
    private String date;
    public comment(String comment, int userId, String date) {
        this.comment = comment;
        this.userId = userId;
        this.date = date;
    }

    protected comment(Parcel in) {
        comment = in.readString();
        userId = in.readInt();
        date = in.readString();
    }

    public static final Creator<comment> CREATOR = new Creator<comment>() {
        @Override
        public comment createFromParcel(Parcel in) {
            return new comment(in);
        }

        @Override
        public comment[] newArray(int size) {
            return new comment[size];
        }
    };

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeInt(userId);
        dest.writeString(date);
    }
}
