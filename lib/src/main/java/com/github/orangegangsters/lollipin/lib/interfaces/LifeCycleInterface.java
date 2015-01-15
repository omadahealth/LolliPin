package com.github.orangegangsters.lollipin.lib.interfaces;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by stoyan on 1/12/15.
 * Allows to follow the LifeCycle of the {@link com.github.orangegangsters.lollipin.lib.PinActivity}
 * Implemented by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockImpl} in order to
 * determine when the app was launched for the last time and when to launch the
 * {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
 */
public interface LifeCycleInterface {

    /**
     * Called in {@link android.app.Activity#onCreate(android.os.Bundle)}
     */
    public void onActivityCreated(Activity activity, Bundle savedInstanceState);

    /**
     * Called in {@link android.app.Activity#onStart()}
     */
    public void onActivityStarted(Activity activity);

    /**
     * Called in {@link android.app.Activity#onResume()}
     */
    public void onActivityResumed(Activity activity);

    /**
     * Called in {@link android.app.Activity#onPause()}
     */
    public void onActivityPaused(Activity activity);

    /**
     * Called in {@link android.app.Activity#onStop()}
     */
    public void onActivityStopped(Activity activity);

    /**
     * Called in {@link android.app.Activity#onSaveInstanceState(android.os.Bundle)}
     */
    public void onActivitySaveInstanceState(Activity activity, Bundle outState);

    /**
     * Called in {@link android.app.Activity#onDestroy()}
     */
    public void onActivityDestroyed(Activity activity);
}
