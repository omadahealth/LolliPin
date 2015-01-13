package com.github.orangegangsters.lollipin.lib.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.encryption.Encryptor;
import com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface;

public class AppLockImpl extends AppLock implements LifeCycleInterface {
	public static final String TAG = "DefaultAppLock";

	private static final String PASSWORD_PREFERENCE_KEY = "passcode";
	private static final String PASSWORD_SALT = "7xn7@c$";

	private SharedPreferences settings;

	private int liveCount;
	private int visibleCount;

	private long lastActive;

	public AppLockImpl(Context context) {
		super();
		this.settings = PreferenceManager.getDefaultSharedPreferences(context);
		this.liveCount = 0;
		this.visibleCount = 0;
	}

	public void enable() {
		PinActivity.addListener(this);
	}

	public void disable() {
        PinActivity.clearListeners();
	}

	public boolean checkPasscode(String passcode) {
		passcode = PASSWORD_SALT + passcode + PASSWORD_SALT;
		passcode = Encryptor.getSHA1(passcode);
		String storedPasscode = "";

		if (settings.contains(PASSWORD_PREFERENCE_KEY)) {
			storedPasscode = settings.getString(PASSWORD_PREFERENCE_KEY, "");
		}

		if (passcode.equalsIgnoreCase(storedPasscode)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean setPasscode(String passcode) {
		SharedPreferences.Editor editor = settings.edit();

		if (passcode == null) {
			editor.remove(PASSWORD_PREFERENCE_KEY);
			editor.apply();
			this.disable();
		} else {
			passcode = PASSWORD_SALT + passcode + PASSWORD_SALT;
			passcode = Encryptor.getSHA1(passcode);
			editor.putString(PASSWORD_PREFERENCE_KEY, passcode);
			editor.apply();
			this.enable();
		}

		return true;
	}

	// Check if we need to show the lock screen at startup
	public boolean isPasscodeSet() {
		if (settings.contains(PASSWORD_PREFERENCE_KEY)) {
			return true;
		}

		return false;
	}

	private boolean isIgnoredActivity(Activity activity) {
		String clazzName = activity.getClass().getName();

		// ignored activities
		if (ignoredActivities.contains(clazzName)) {
			Log.d(TAG, "ignore activity " + clazzName);
			return true;
		}

		return false;
	}

	private boolean shouldLockSceen(Activity activity) {

		// already unlock
		if (activity instanceof AppLockActivity) {
			AppLockActivity ala = (AppLockActivity) activity;
			if (ala.getType() == AppLock.UNLOCK_PIN) {
				Log.d(TAG, "already unlock activity");
				return false;
			}
		}

		// no pass code set
		if (!isPasscodeSet()) {
			Log.d(TAG, "lock passcode not set.");
			return false;
		}

		// no enough timeout
		long passedTime = System.currentTimeMillis() - lastActive;
		if (lastActive > 0 && passedTime <= lockTimeOut) {
			Log.d(TAG, "no enough timeout " + passedTime + " for "
                    + lockTimeOut);
			return false;
		}

		// start more than one page
		if (visibleCount > 1) {
			return false;
		}

		return true;
	}

	@Override
	public void onActivityPaused(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityPaused " + clazzName);
	}

	@Override
	public void onActivityResumed(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityResumed " + clazzName);

		if (isIgnoredActivity(activity)) {
			return;
		}

		if (shouldLockSceen(activity)) {
			Intent intent = new Intent(activity.getApplicationContext(),
					AppLockActivity.class);
			intent.putExtra(AppLock.TYPE, AppLock.UNLOCK_PIN);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.getApplication().startActivity(intent);
		}

		lastActive = 0;
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		liveCount++;
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		if (isIgnoredActivity(activity)) {
			return;
		}

		liveCount--;
		if (liveCount == 0) {
			lastActive = System.currentTimeMillis();
			Log.d(TAG, "set last active " + lastActive);
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		if (isIgnoredActivity(activity)) {
			return;
		}
	}

	@Override
	public void onActivityStarted(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityStarted " + clazzName);

		if (isIgnoredActivity(activity)) {
			return;
		}

		visibleCount++;
	}

	@Override
	public void onActivityStopped(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityStopped " + clazzName);

		if (isIgnoredActivity(activity)) {
			return;
		}

		visibleCount--;
		if (visibleCount == 0) {
			lastActive = System.currentTimeMillis();
			Log.d(TAG, "set last active " + lastActive);
		}
	}
}
