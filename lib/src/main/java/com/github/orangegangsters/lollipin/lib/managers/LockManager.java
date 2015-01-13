package com.github.orangegangsters.lollipin.lib.managers;

import android.content.Context;

public class LockManager {

    private volatile static LockManager mInstance;
    private static AppLock mAppLocker;

    public static LockManager getInstance() {
        synchronized (LockManager.class) {
            if (mInstance == null) {
                mInstance = new LockManager();
            }
        }
        return mInstance;
    }

    public void enableAppLock(Context context) {
        if (mAppLocker == null) {
            mAppLocker = new AppLockImpl(context);
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

    public AppLock getAppLock(Context context) {
        if (mAppLocker == null) {
            mAppLocker = new AppLockImpl(context);
        }
        return mAppLocker;
    }
}
