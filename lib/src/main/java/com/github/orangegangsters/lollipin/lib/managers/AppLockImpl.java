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

	private SharedPreferences mSharedPreferences;

	private int mLiveActivitiesCount;
	private int mVisibleActivitiesCount;

	private long mLastActiveMillis;

	public AppLockImpl(Context context) {
		super();
		this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.mLiveActivitiesCount = 0;
		this.mVisibleActivitiesCount = 0;
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

		if (mSharedPreferences.contains(PASSWORD_PREFERENCE_KEY)) {
			storedPasscode = mSharedPreferences.getString(PASSWORD_PREFERENCE_KEY, "");
		}

		if (passcode.equalsIgnoreCase(storedPasscode)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean setPasscode(String passcode) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();

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
		if (mSharedPreferences.contains(PASSWORD_PREFERENCE_KEY)) {
			return true;
		}

		return false;
	}

	private boolean isIgnoredActivity(Activity activity) {
		String clazzName = activity.getClass().getName();

		// ignored activities
		if (mIgnoredActivities.contains(clazzName)) {
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
		long passedTime = System.currentTimeMillis() - mLastActiveMillis;
		if (mLastActiveMillis > 0 && passedTime <= mLockTimeoutMillis) {
			Log.d(TAG, "no enough timeout " + passedTime + " for "
                    + mLockTimeoutMillis);
			return false;
		}

		// start more than one page
		if (mVisibleActivitiesCount > 1) {
			return false;
		}

		return true;
	}

	@Override
	public void onActivityPaused(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityPaused " + clazzName);

        mLastActiveMillis = System.currentTimeMillis();
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
			intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.getApplication().startActivity(intent);
		}

		mLastActiveMillis = System.currentTimeMillis();
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

		if (isIgnoredActivity(activity)) {
			return;
		}

		mLiveActivitiesCount++;
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		if (isIgnoredActivity(activity)) {
			return;
		}

		mLiveActivitiesCount--;
		if (mLiveActivitiesCount == 0) {
			mLastActiveMillis = System.currentTimeMillis();
			Log.d(TAG, "set last active " + mLastActiveMillis);
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

		mVisibleActivitiesCount++;
	}

	@Override
	public void onActivityStopped(Activity activity) {
		String clazzName = activity.getClass().getName();
		Log.d(TAG, "onActivityStopped " + clazzName);

		if (isIgnoredActivity(activity)) {
			return;
		}

		mVisibleActivitiesCount--;
		if (mVisibleActivitiesCount == 0) {
			mLastActiveMillis = System.currentTimeMillis();
			Log.d(TAG, "set last active " + mLastActiveMillis);
		}
	}
}
