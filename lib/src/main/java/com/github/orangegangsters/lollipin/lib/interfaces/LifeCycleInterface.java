package com.github.orangegangsters.lollipin.lib.interfaces;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by stoyan on 1/12/15.
 */
public interface LifeCycleInterface {

    public void onActivityCreated(Activity activity, Bundle savedInstanceState);

    public void onActivityStarted(Activity activity);

    public void onActivityResumed(Activity activity);

    public void onActivityPaused(Activity activity);

    public void onActivityStopped(Activity activity);

    public void onActivitySaveInstanceState(Activity activity, Bundle outState);

    public void onActivityDestroyed(Activity activity);
}
