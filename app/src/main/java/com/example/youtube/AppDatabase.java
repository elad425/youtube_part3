package com.example.youtube;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.youtube.Daos.userDao;
import com.example.youtube.entities.user;
import com.example.youtube.utils.Converters;

@Database(entities = {user.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract userDao userDao();
}
