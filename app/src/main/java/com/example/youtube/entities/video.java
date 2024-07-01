package com.example.youtube.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class video implements Parcelable {
    private String video_name;
    private creator creator;
    private String date_of_release;
    private String views;
    private String likes;
    private ArrayList<comment> comments;
    private String video_path;
    private String thumbnail;
    private String video_length;

    public video(String video_name, creator creator, String date_of_release, String video_path, String thumbnail, String video_length, String views, String likes) {
        this.video_name = video_name;
        this.creator = creator;
        this.date_of_release = date_of_release;
        this.video_path = video_path;
        this.thumbnail = thumbnail;
        this.views = views;
        this.video_length = video_length;
        this.comments = new ArrayList<>();
        this.likes = likes;
    }

    protected video(Parcel in) {
        video_name = in.readString();
        creator = in.readParcelable(creator.class.getClassLoader());
        date_of_release = in.readString();
        views = in.readString();
        likes = in.readString();
        comments = in.createTypedArrayList(comment.CREATOR);
        video_path = in.readString();
        thumbnail = in.readString();
        video_length = in.readString();
    }

    public static final Creator<video> CREATOR = new Creator<video>() {
        @Override
        public video createFromParcel(Parcel in) {
            return new video(in);
        }

        @Override
        public video[] newArray(int size) {
            return new video[size];
        }
    };

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public creator getCreator() {
        return creator;
    }

    public void setCreator(creator creator) {
        this.creator = creator;
    }

    public String getDate_of_release() {
        return date_of_release;
    }

    public void setDate_of_release(String date_of_release) { this.date_of_release = date_of_release;}

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public ArrayList<comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<comment> comments) {
        this.comments = comments;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo_length() {
        return video_length;
    }

    public void setVideo_length(String video_length) {
        this.video_length = video_length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(video_name);
        dest.writeParcelable(creator,flags);
        dest.writeString(date_of_release);
        dest.writeString(views);
        dest.writeString(likes);
        dest.writeTypedList(comments);
        dest.writeString(video_path);
        dest.writeString(thumbnail);
        dest.writeString(video_length);

    }
}
