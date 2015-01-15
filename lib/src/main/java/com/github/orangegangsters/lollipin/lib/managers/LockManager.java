package com.github.orangegangsters.lollipin.lib.managers;

import android.content.Context;

public class LockManager<T extends AppLockActivity> {

    private volatile static LockManager mInstance;
    private static AppLock mAppLocker;

    public static LockManager getInstance() {
        synchronized (LockManager.class) {
            if (mInstance == null) {
                mInstance = new LockManager<>();
            }
        }
        return mInstance;
    }

    public void enableAppLock(Context context, Class<T> activityClass) {
        if (mAppLocker == null) {
            mAppLocker = new AppLockImpl<>(context, activityClass);
        }
        mAppLocker.enable();
    }

    public boolean isAppLockEnabled() {
        if (mAppLocker == null) {
            return false;
        } else {
            return true;
        }
    }

    public void disableAppLock() {
        if (mAppLocker != null) {
            mAppLocker.disable();
        }
    }

    public void setAppLock(AppLock appLocker) {
        if (mAppLocker != null) {
            mAppLocker.disable();
        }
        mAppLocker = appLocker;
    }

    public AppLock getAppLock() {
        return mAppLocker;
    }
}
