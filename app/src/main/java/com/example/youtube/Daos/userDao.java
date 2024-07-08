//package com.example.youtube.Daos;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.example.youtube.User;
//
//import java.util.List;
//
//@Dao
//public interface userDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(User user);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertList(List<User> users);
//
//    @Update
//    void update(User user);
//
//    @Delete
//    void delete(User user);
//
//    @Query("SELECT * FROM user WHERE _id = :id")
//    User getUserById(String id);
//
//    @Query("SELECT * FROM user")
//    List<User> getAllUsers();
//
//    @Query("DELETE FROM user")
//    void clear();
//
//    @Query("SELECT * FROM user")
//    LiveData<List<User>> getAllUsersLive();
//
//}
