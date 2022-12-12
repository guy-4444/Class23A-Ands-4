package com.guy.class23a_ands_4;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MCT5.initHelper();
    }
}
