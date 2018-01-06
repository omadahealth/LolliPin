package com.github.omadahealth.lollipin;

import android.app.Application;

import com.github.omadahealth.lollipin.lib.managers.LockManager;

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
        lockManager.getAppLock().setDisablePinlockMessage("Disable pinlock step message");
        lockManager.getAppLock().setEnablePinlockMessage("Enable pinlock step message");
        lockManager.getAppLock().setChangePinMessage("Change pin step message");
        lockManager.getAppLock().setUnlockPinMessage("Unlock pin step message");
        lockManager.getAppLock().setConfirmPinMessage("Confirm pinlock step message");
    }
}
