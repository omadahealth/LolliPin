package com.github.orangegangsters.lollipin;

import android.app.Application;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;

/**
 * Created by oliviergoutay on 1/14/15.
 */
public class CustomApplication extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();

        LockManager<CustomPinActivity> activity = LockManager.getInstance();
        activity.enableAppLock(this, CustomPinActivity.class);
    }
}
