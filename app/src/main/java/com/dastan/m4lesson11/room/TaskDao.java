package com.dastan.m4lesson11.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dastan.m4lesson11.Task;

import java.util.List;

@Dao
public interface TaskDao {

//    @Query("SELECT * FROM task")
//    List<Task> getAll();

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM task ORDER BY title")
    LiveData<List<Task>> sortedMethod();

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAll();

    @Insert
    void insert(Task task);
}
