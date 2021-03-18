package com.clickandgo;

import android.app.Application;

import com.clickandgo.di.AppComponent;
import com.clickandgo.di.DaggerAppComponent;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent component = DaggerAppComponent.create();
    }
}
