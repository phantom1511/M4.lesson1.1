package com.dastan.m4lesson11;

import android.app.Application;

import androidx.room.Room;

import com.dastan.m4lesson11.room.AppDatabase;

public class App extends Application {

    public static App instance;
    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database").allowMainThreadQueries().build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
