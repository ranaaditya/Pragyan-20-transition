package com.rana_aditya.pragyan_transition;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
      //  Fabric.with(this, new Crashlytics());
        app = this;

       // Timber.plant(new Timber.DebugTree());
    }

    public static Context getAppContext() {
        return app;
    }
}
