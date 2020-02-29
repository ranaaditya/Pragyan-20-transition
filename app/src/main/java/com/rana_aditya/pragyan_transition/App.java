package com.rana_aditya.pragyan_transition;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Context getAppContext() {
        return app;
    }
}
