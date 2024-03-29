package com.dastan.m4lesson11.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dastan.m4lesson11.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
