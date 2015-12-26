package com.github.orangegangsters.lollipin.lib.managers;

import android.app.Activity;

import java.util.HashSet;

public abstract class AppLock {

    /**
     * intent key for passing hashed password when password is set.
     */
    public static final String KEY_HASHED_PASSWORD = "lollipop_key_hashed_password";

    /**
     * intent key for passing salt when password is set.
     */
    public static final String KEY_SALT = "lollipop_key_salt";

    /**
     * ENABLE_PINLOCK type, uses at firt to define the password
     */
    public static final int ENABLE_PINLOCK = 0;
    /**
     * DISABLE_PINLOCK type, uses to disable the system by asking the current password
     */
    public static final int DISABLE_PINLOCK = 1;
    /**
     * CHANGE_PIN type, uses to change the current password
     */
    public static final int CHANGE_PIN = 2;
    /**
     * CONFIRM_PIN type, used to confirm the new password
     */
    public static final int CONFIRM_PIN = 3;
    /**
     * UNLOCK_PIN type, uses to ask the password to the user, in order to unlock the app
     */
    public static final int UNLOCK_PIN = 4;

    /**
     * UNLOCK_PIN_CANCELLABLE type, uses to ask the password to the user proactively, in order to verify the app before conducting sensitive action.
     */
    public static final int UNLOCK_PIN_CANCELLABLE = 5;

    /**
     * LOGO_ID_NONE used to denote when a user has not set a logoId using {@link #setLogoId(int)}
     */
    public static final int LOGO_ID_NONE = -1;

    /**
     * EXTRA_TYPE, uses to pass to the {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     * to determine in which type it musts be started.
     */
    public static final String EXTRA_TYPE = "type";

    /**
     * DEFAULT_TIMEOUT, define the default timeout returned by {@link #getTimeout()}.
     * If you want to modify it, you can call {@link #setTimeout(long)}. Will be stored using
     * {@link android.content.SharedPreferences}
     */
    public static final long DEFAULT_TIMEOUT = 1000 * 10; // 10sec

    /**
     * A {@link java.util.HashSet} of {@link java.lang.String} which are the classes we don't want to
     * take into account for the {@link com.github.orangegangsters.lollipin.lib.PinActivity}. These activities
     * will not log the last opened time, will not launch the
     * {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity} etc...
     */
    protected HashSet<String> mIgnoredActivities;

    public AppLock() {
        mIgnoredActivities = new HashSet<String>();
    }

    /**
     * Add an ignored activity to the {@link java.util.HashSet}
     */
    public void addIgnoredActivity(Class<?> clazz) {
        String clazzName = clazz.getName();
        this.mIgnoredActivities.add(clazzName);
    }

    /**
     * Remove an ignored activity to the {@link java.util.HashSet}
     */
    public void removeIgnoredActivity(Class<?> clazz) {
        String clazzName = clazz.getName();
        this.mIgnoredActivities.remove(clazzName);
    }

    /**
     * Get the timeout used in {@link #shouldLockSceen(android.app.Activity)}
     */
    public abstract long getTimeout();

    /**
     * Set the timeout used in {@link #shouldLockSceen(android.app.Activity)}
     */
    public abstract void setTimeout(long timeout);

    /**
     * Get the timeout used in {@link #shouldLockSceen(android.app.Activity)}
     */
    public abstract int getAttempts();

    /**
     * Set the timeout used in {@link #shouldLockSceen(android.app.Activity)}
     */
    public abstract void setAttempts(int attempts);

    /**
     * reset password
      */
    public abstract void resetPassword();


    /**
     * Get logo resource id used by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    public abstract int getLogoId();

    /**
     * Set logo resource id used by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    public abstract void setLogoId(int logoId);

    /**
     * Get the forgot option used by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    public abstract boolean shouldShowForgot();

    /**
     * Set the forgot option used by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    public abstract void setShouldShowForgot(boolean showForgot);

    /**
     * Get whether the user backed out of the {@link AppLockActivity} previously
     */
    public abstract boolean pinChallengeCancelled();

    /**
     * Set whether the user backed out of the {@link AppLockActivity}
     */
    public abstract void setPinChallengeCancelled(boolean cancelled);

    /**
     * Enable the {@link com.github.orangegangsters.lollipin.lib.managers.AppLock} by setting
     * {@link com.github.orangegangsters.lollipin.lib.managers.AppLockImpl} as the
     * {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface}
     */
    public abstract void enable();

    /**
     * Disable the {@link com.github.orangegangsters.lollipin.lib.managers.AppLock} by removing any
     * {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface}
     */
    public abstract void disable();

    /**
     * Disable the {@link com.github.orangegangsters.lollipin.lib.managers.AppLock} by removing any
     * {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface} and also delete
     * all the previous saved configurations into {@link android.content.SharedPreferences}
     */
    public abstract void disableAndRemoveConfiguration();

    /**
     * Get the last active time of the app used by {@link #shouldLockSceen(android.app.Activity)}
     */
    public abstract long getLastActiveMillis();

    /**
     * Set the last active time of the app used by {@link #shouldLockSceen(android.app.Activity)}.
     * Set in {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface#onActivityStopped(android.app.Activity)}
     * and {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface#onActivityResumed(android.app.Activity)}
     */
    public abstract void setLastActiveMillis(long lastActiveMillis);

    /**
     * Set the passcode (store his SHA1 into {@link android.content.SharedPreferences}) using the
     * {@link com.github.orangegangsters.lollipin.lib.encryption.Encryptor} class.
     */
    public abstract boolean setPasscode(String passcode);

    /**
     * Check the passcode by comparing his SHA1 into {@link android.content.SharedPreferences} using the
     * {@link com.github.orangegangsters.lollipin.lib.encryption.Encryptor} class.
     */
    public abstract boolean checkPasscode(String passcode);

    /**
     * Check the {@link android.content.SharedPreferences} to see if a password already exists
     */
    public abstract boolean isPasscodeSet();

    /**
     * Check if an activity must be ignored and then don't call the
     * {@link com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface}
     */
    public abstract boolean isIgnoredActivity(Activity activity);

    /**
     * Evaluates if:
     * - we are already into the {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     * - the passcode is not set
     * - the timeout didn't reached
     * If any of this is true, then we don't need to start the
     * {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity} (it returns false)
     * Otherwise returns true
     */
    public abstract boolean shouldLockSceen(Activity activity);


    public abstract String getForgotPinMsg();

    public abstract void setForgotPinMsg(String msg);

    public abstract String getDisablePinMsg();

    public abstract void setDisablePinMsg(String msg);

    public abstract String getChangePinMsg();

    public abstract void setChangePinMsg(String msg);

    public abstract String getUnlockPinMsg();

    public abstract void setUnlockPinMsg(String msg);

    public abstract String getConfirmPinMsg();

    public abstract void setConfirmPinMsg(String msg);

    public abstract String getCreatePinMsg();

    public abstract void setCreatePinMsg(String msg);

    public abstract void setMsg(String sharedPreferencesKey, String msg);


}
