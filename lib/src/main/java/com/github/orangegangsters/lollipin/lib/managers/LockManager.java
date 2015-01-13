package com.github.orangegangsters.lollipin.lib.managers;

import android.content.Context;

public class LockManager {

	private volatile static LockManager instance;
	private static AppLock curAppLocker;

	public static LockManager getInstance(Context context) {
		synchronized (LockManager.class) {
			if (instance == null) {
				instance = new LockManager();
                curAppLocker = new AppLockImpl(context);
			}
		}
		return instance;
	}

	public void enableAppLock(Context context) {
		if (curAppLocker == null) {
			curAppLocker = new AppLockImpl(context);
		}
		curAppLocker.enable();
	}

	public boolean isAppLockEnabled() {
		if (curAppLocker == null) {
			return false;
		} else {
			return true;
		}
	}

	public void setAppLock(AppLock appLocker) {
		if (curAppLocker != null) {
			curAppLocker.disable();
		}
		curAppLocker = appLocker;
	}

	public AppLock getAppLock() {
		return curAppLocker;
	}
}
