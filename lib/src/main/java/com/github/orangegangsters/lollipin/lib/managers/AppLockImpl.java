package com.github.orangegangsters.lollipin.lib.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.PinFragmentActivity;
import com.github.orangegangsters.lollipin.lib.encryption.Encryptor;
import com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface;

public class AppLockImpl<T extends AppLockActivity> extends AppLock implements LifeCycleInterface {

    public static final String TAG = "AppLockImpl";

    /**
     * The {@link android.content.SharedPreferences} key used to store the password
     */
    private static final String PASSWORD_PREFERENCE_KEY = "PASSCODE";
    /**
     * The {@link android.content.SharedPreferences} key used to store the last active time
     */
    private static final String LAST_ACTIVE_MILLIS_PREFERENCE_KEY = "LAST_ACTIVE_MILLIS";
    /**
     * The {@link android.content.SharedPreferences} key used to store the timeout
     */
    private static final String TIMEOUT_MILLIS_PREFERENCE_KEY = "TIMEOUT_MILLIS_PREFERENCE_KEY";
    /**
     * The {@link android.content.SharedPreferences} key used to store the logo resource id
     */
    private static final String LOGO_ID_PREFERENCE_KEY = "LOGO_ID_PREFERENCE_KEY";
    /**
     * A string used to pollute the SHA1 password stored into
     * {@link android.content.SharedPreferences} for more security.
     */
    private static final String PASSWORD_SALT = "7xn7@c$";

    /**
     * The {@link android.content.SharedPreferences} used to store the password, the last active time etc...
     */
    private SharedPreferences mSharedPreferences;

    /**
     * The activity class that extends {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    private Class<T> mActivityClass;

    public AppLockImpl(Context context, Class<T> activityClass) {
        super();
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mActivityClass = activityClass;
    }

    @Override
    public void setTimeout(long timeout) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(TIMEOUT_MILLIS_PREFERENCE_KEY, timeout);
        editor.apply();
    }

    @Override
    public long getTimeout() {
        return mSharedPreferences.getLong(TIMEOUT_MILLIS_PREFERENCE_KEY, DEFAULT_TIMEOUT);
    }

    @Override
    public void setLogoId(int logoId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LOGO_ID_PREFERENCE_KEY, logoId);
        editor.apply();
    }

    @Override
    public int getLogoId() {
        return mSharedPreferences.getInt(LOGO_ID_PREFERENCE_KEY, android.R.drawable.sym_def_app_icon);
    }

    @Override
    public void enable() {
        PinActivity.setListener(this);
        PinFragmentActivity.setListener(this);
    }

    @Override
    public void disable() {
        PinActivity.clearListeners();
        PinFragmentActivity.clearListeners();
    }

    @Override
    public void disableAndRemoveConfiguration() {
        PinActivity.clearListeners();
        PinFragmentActivity.clearListeners();
        mSharedPreferences.edit().remove(PASSWORD_PREFERENCE_KEY)
                .remove(LAST_ACTIVE_MILLIS_PREFERENCE_KEY)
                .remove(TIMEOUT_MILLIS_PREFERENCE_KEY)
                .remove(LOGO_ID_PREFERENCE_KEY)
                .apply();
    }

    @Override
    public long getLastActiveMillis() {
        return mSharedPreferences.getLong(LAST_ACTIVE_MILLIS_PREFERENCE_KEY, 0);
    }

    @Override
    public void setLastActiveMillis() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_ACTIVE_MILLIS_PREFERENCE_KEY, System.currentTimeMillis());
        editor.apply();
    }

    @Override
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

    @Override
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

    @Override
    public boolean isPasscodeSet() {
        if (mSharedPreferences.contains(PASSWORD_PREFERENCE_KEY)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isIgnoredActivity(Activity activity) {
        String clazzName = activity.getClass().getName();

        // ignored activities
        if (mIgnoredActivities.contains(clazzName)) {
            Log.d(TAG, "ignore activity " + clazzName);
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldLockSceen(Activity activity) {
        Log.d(TAG, "Lollipin shouldLockSceen() called");

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
        long lastActiveMillis = getLastActiveMillis();
        long passedTime = System.currentTimeMillis() - lastActiveMillis;
        long timeout = getTimeout();
        if (lastActiveMillis > 0 && passedTime <= timeout) {
            Log.d(TAG, "no enough timeout " + passedTime + " for "
                    + timeout);
            return false;
        }

        return true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (isIgnoredActivity(activity)) {
            return;
        }

        String clazzName = activity.getClass().getName();
        Log.d(TAG, "onActivityPaused " + clazzName);

        setLastActiveMillis();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isIgnoredActivity(activity)) {
            return;
        }

        String clazzName = activity.getClass().getName();
        Log.d(TAG, "onActivityResumed " + clazzName);

        if (shouldLockSceen(activity)) {
            Log.d(TAG, "mActivityClass.getClass() " + mActivityClass);
            Intent intent = new Intent(activity.getApplicationContext(),
                    mActivityClass);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplication().startActivity(intent);
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return;
        }

        setLastActiveMillis();
    }
}
