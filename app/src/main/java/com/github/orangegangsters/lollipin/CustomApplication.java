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

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setTimeout(2 * 1000);
    }
}
