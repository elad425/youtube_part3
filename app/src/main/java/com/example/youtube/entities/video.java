
package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.youtube.utils.videoConverters;

import java.util.ArrayList;
@Entity(tableName = "videos")
public class video {
    @PrimaryKey(autoGenerate = true)
    private int videoId;
    private String video_name;
    private int creatorId;
    private String date_of_release;
    private String views;
    @TypeConverters(videoConverters.class)
    private ArrayList<comment> comments;
    private String video_path;
    private String thumbnail;
    private String video_length;

    public video(String video_name, int creatorId, String date_of_release, String video_path, String thumbnail, String video_length, String views) {
        this.video_name = video_name;
        this.creatorId = creatorId;
        this.date_of_release = date_of_release;
        this.video_path = video_path;
        this.thumbnail = thumbnail;
        this.views = views;
        this.video_length = video_length;
        this.comments = new ArrayList<>();
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
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

}
