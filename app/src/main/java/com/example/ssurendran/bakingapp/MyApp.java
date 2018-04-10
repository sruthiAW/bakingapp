package com.example.ssurendran.bakingapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ssurendran on 4/10/18.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
