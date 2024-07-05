package com.example.youtube.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtube.entities.user;

import java.util.List;

@Dao
public interface userDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(user user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<user> users);

    @Update
    void update(user user);

    @Delete
    void delete(user user);

    @Query("SELECT * FROM users WHERE id = :id")
    user getUserById(int id);

    @Query("SELECT * FROM users")
    List<user> getAllUsers();

    @Query("DELETE FROM users")
    void clear();

}
