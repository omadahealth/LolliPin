package com.github.orangegangsters.lollipin.lib;

import android.app.Activity;
import android.os.Bundle;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityCreated(this, savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityStarted(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityResumed(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityPaused(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityStopped(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivitySaveInstanceState(this, outState);
        }
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
