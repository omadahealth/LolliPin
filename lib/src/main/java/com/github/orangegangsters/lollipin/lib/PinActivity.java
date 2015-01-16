package com.github.orangegangsters.lollipin.lib;

import android.app.Activity;

import com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface;

/**
 * Created by stoyan and olivier on 1/12/15.
 * You must extend this Activity in order to support this library.
 * Then to enable PinCode blocking, you must call
 * {@link com.github.orangegangsters.lollipin.lib.managers.LockManager#enableAppLock(android.content.Context, Class)}
 */
public class PinActivity extends Activity {
    private static LifeCycleInterface mLifeCycleListener;

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mLifeCycleListener != null) {
                    mLifeCycleListener.onActivityResumed(PinActivity.this);
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mLifeCycleListener != null) {
                    mLifeCycleListener.onActivityPaused(PinActivity.this);
                }
            }
        }).start();
    }

    public static void setListener(LifeCycleInterface listener) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener = null;
        }
        mLifeCycleListener = listener;
    }

    public static void clearListeners() {
        mLifeCycleListener = null;
    }

    public static boolean hasListeners() {
        return (mLifeCycleListener != null);
    }
}
