package com.example.youtube.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.youtube.entities.video;

import java.util.List;

@Dao
public interface videoDao {
    @Insert
    void insert(video video);

    @Update
    void update(video video);

    @Delete
    void delete(video video);

    @Query("SELECT * FROM videos WHERE videoId = :id")
    video getVideoById(int id);

    @Query("SELECT * FROM videos")
    List<video> getAllVideos();

    @Query("SELECT * FROM videos WHERE creator = :creatorId")
    List<video> getVideosByCreator(int creatorId);
}