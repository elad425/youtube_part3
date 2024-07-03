package com.example.youtube;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.Daos.userDao;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.Converters.userConverters;
import com.example.youtube.Converters.videoConverters;

@Database(entities = {user.class, video.class}, version = 1)
@TypeConverters({videoConverters.class, userConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract userDao userDao();
    public abstract videoDao videoDao();
}