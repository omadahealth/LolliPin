package com.github.orangegangsters.lollipin.lib.managers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.PinCompatActivity;
import com.github.orangegangsters.lollipin.lib.PinFragmentActivity;
import com.github.orangegangsters.lollipin.lib.encryption.Encryptor;
import com.github.orangegangsters.lollipin.lib.encryption.SaltGenerator;
import com.github.orangegangsters.lollipin.lib.enums.Algorithm;
import com.github.orangegangsters.lollipin.lib.interfaces.ConfigurationStorage;
import com.github.orangegangsters.lollipin.lib.interfaces.CredentialStorage;
import com.github.orangegangsters.lollipin.lib.interfaces.LifeCycleInterface;
import com.github.orangegangsters.lollipin.lib.storage.DefaultPreferencesConfigurationStorage;
import com.github.orangegangsters.lollipin.lib.storage.DefaultPreferencesCredentialStorage;

public class AppLockImpl extends AppLock implements LifeCycleInterface {

    public static final String TAG = "AppLockImpl";

    private final CredentialStorage mCredentialStorage;
    private final ConfigurationStorage mConfigurationStorage;

    /**
     * The activity class that extends {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    private final Class<? extends AppLockActivity> mActivityClass;

    /**
     * Static instance of {@link AppLockImpl}
     */
    private static AppLockImpl mInstance;

    /**
     * Static method that allows to get back the current static Instance of {@link AppLockImpl}
     *
     * @param context       The current context of the {@link Activity}
     * @param activityClass The activity extending {@link AppLockActivity}
     * @return The instance.
     */
    @Deprecated
    public static AppLockImpl getInstance(Context context, Class<? extends AppLockActivity> activityClass) {
        synchronized (LockManager.class) {
            if (mInstance == null) {
                mInstance = new AppLockImpl(context, activityClass);
            }
        }
        return mInstance;
    }

    AppLockImpl(Context context, Class<? extends AppLockActivity> activityClass) {
        mActivityClass = activityClass;
        mCredentialStorage = new DefaultPreferencesCredentialStorage(context);
        mConfigurationStorage = new DefaultPreferencesConfigurationStorage(context);
    }

    AppLockImpl(Builder builder) {
        mActivityClass = builder.getActivityClass();
        mConfigurationStorage = getConfigurationStorageOrDefault(builder);
        mCredentialStorage = getCredentialStorageOrDefault(builder);
    }

    private static ConfigurationStorage getConfigurationStorageOrDefault(Builder builder) {
        ConfigurationStorage storage = builder.getConfigurationStorage();
        if (storage == null) {
            return new DefaultPreferencesConfigurationStorage(builder.getContext());
        } else {
            return storage;
        }
    }

    private static CredentialStorage getCredentialStorageOrDefault(Builder builder) {
        CredentialStorage storage = builder.getCredentialStorage();
        if (storage == null) {
            return new DefaultPreferencesCredentialStorage(builder.getContext());
        } else {
            return storage;
        }
    }

    @Override
    public long getTimeout() {
        return mConfigurationStorage.readTimeout();
    }

    @Override
    public void setTimeout(long timeout) {
        mConfigurationStorage.writeTimeout(timeout);
    }

    public String getSalt() {
        String salt = mCredentialStorage.readSalt();
        if (salt == null) {
            salt = SaltGenerator.generate();
            setSalt(salt);
        }
        return salt;
    }

    private void setSalt(String salt) {
        mCredentialStorage.writeSalt(salt);
    }

    @Override
    public int getLogoId() {
        return mConfigurationStorage.readLogoId();
    }

    @Override
    public void setLogoId(int logoId) {
        mConfigurationStorage.writeLogoId(logoId);
    }

    @Override
    public boolean shouldShowForgot(int appLockType) {
        return mConfigurationStorage.readShouldShowForgot()
                && appLockType != AppLock.ENABLE_PINLOCK
                && appLockType != AppLock.CONFIRM_PIN;
    }

    @Override
    public void setShouldShowForgot(boolean showForgot) {
        mConfigurationStorage.writeShouldShowForgot(showForgot);
    }

    @Override
    public boolean pinChallengeCancelled() {
        return mConfigurationStorage.readPinChallengeCanceled();
    }

    @Override
    public void setPinChallengeCancelled(boolean backedOut) {
        mConfigurationStorage.writePinChallengeCanceled(backedOut);
    }

    @Override
    public boolean onlyBackgroundTimeout() {
        return mConfigurationStorage.readOnlyBackgroundTimeout();
    }

    @Override
    public void setOnlyBackgroundTimeout(boolean onlyBackgroundTimeout) {
        mConfigurationStorage.writeOnlyBackgroundTimeout(onlyBackgroundTimeout);
    }

    @Override
    public void enable() {
        PinActivity.setListener(this);
        PinCompatActivity.setListener(this);
        PinFragmentActivity.setListener(this);
    }

    @Override
    public void disable() {
        PinActivity.clearListeners();
        PinCompatActivity.clearListeners();
        PinFragmentActivity.clearListeners();
    }

    @Override
    public void disableAndRemoveConfiguration() {
        PinActivity.clearListeners();
        PinCompatActivity.clearListeners();
        PinFragmentActivity.clearListeners();
        mConfigurationStorage.clear();
        mCredentialStorage.clear();
    }

    @Override
    public boolean isFingerprintAuthEnabled() {
        return mConfigurationStorage.readFingerprintAuthEnabled();
    }

    @Override
    public void setFingerprintAuthEnabled(boolean enabled) {
        mConfigurationStorage.writeFingerprintAuthEnabled(enabled);
    }

    @Override
    public long getLastActiveMillis() {
        return mConfigurationStorage.readLastActiveMillis();
    }

    @Override
    public void setLastActiveMillis() {
        mConfigurationStorage.writeLastActiveMillis(System.currentTimeMillis());
    }

    @Override
    public boolean checkPasscode(String passcode) {
        Algorithm algorithm = mCredentialStorage.readCurrentAlgorithm();

        String salt = getSalt();
        passcode = salt + passcode + salt;
        passcode = Encryptor.getSHA(passcode, algorithm);
        String storedPasscode = "";

        if (isPasscodeSet()) {
            storedPasscode = mCredentialStorage.readPasscode();
        }

        return storedPasscode.equalsIgnoreCase(passcode);
    }

    @Override
    public boolean setPasscode(String passcode) {
        String salt = getSalt();
        if (passcode == null) {
            mCredentialStorage.clearPasscode();
            this.disable();
        } else {
            setAlgorithm(Algorithm.SHA256);
            passcode = Encryptor.getSHA(salt + passcode + salt, Algorithm.SHA256);
            mCredentialStorage.writePasscode(passcode);
            this.enable();
        }
        return true;
    }

    /**
     * Set the algorithm used in {@link #setPasscode(String)}
     */
    private void setAlgorithm(Algorithm algorithm) {
        mCredentialStorage.writeCurrentAlgorithm(algorithm);
    }

    @Override
    public boolean isPasscodeSet() {
        return mCredentialStorage.hasPasscode();
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

        // previously backed out of pin screen
        if (pinChallengeCancelled()) {
            return true;
        }

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

        if ((onlyBackgroundTimeout() || !shouldLockSceen(activity)) && !(activity instanceof AppLockActivity)) {
            setLastActiveMillis();
        }
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

        if (!shouldLockSceen(activity) && !(activity instanceof AppLockActivity)) {
            setLastActiveMillis();
        }
    }
}
