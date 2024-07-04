package com.example.youtube;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.Daos.userDao;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.userConverters;
import com.example.youtube.utils.videoConverters;


@Database(entities = {user.class, video.class}, version = 1)
@TypeConverters({videoConverters.class, userConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    static AppDatabase instance;
    public abstract userDao userDao();
    public abstract videoDao videoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "userDb")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}