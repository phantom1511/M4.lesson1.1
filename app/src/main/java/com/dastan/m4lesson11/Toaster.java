package com.dastan.m4lesson11;

import android.widget.Toast;

public class Toaster {
    public static void show(String message){
        Toast.makeText(App.instance.getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
