package com.example.youtube.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.video;

import java.util.List;

@Dao
public interface videoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(video video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<video> video);

    @Update
    void update(video video);

    @Delete
    void delete(video video);

    @Query("SELECT * FROM videos WHERE id = :id")
    video getVideoById(int id);

    @Query("SELECT * FROM videos")
    List<video> getAllVideos();

    @Query("SELECT * FROM videos")
    LiveData<List<video>> getAllVideosLive();

    @Query("DELETE FROM videos")
    void clear();
}