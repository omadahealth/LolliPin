package com.github.orangegangsters.lollipin.lib.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;
import com.github.orangegangsters.lollipin.lib.interfaces.CredentialStorage;

public class DefaultPreferencesCredentialStorage implements CredentialStorage {

    /**
     * The {@link android.content.SharedPreferences} key used to store the dynamically generated password salt
     */
    private static final String PASSWORD_SALT_PREFERENCE_KEY = "PASSWORD_SALT_PREFERENCE_KEY";
    /**
     * The {@link android.content.SharedPreferences} key used to store the password
     */
    private static final String PASSWORD_PREFERENCE_KEY = "PASSCODE";
    /**
     * The {@link android.content.SharedPreferences} key used to store the {@link Algorithm}
     */
    private static final String PASSWORD_ALGORITHM_PREFERENCE_KEY = "ALGORITHM";

    private final SharedPreferences mPreferences;

    public DefaultPreferencesCredentialStorage(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    @Nullable
    public String readSalt() {
        return mPreferences.getString(PASSWORD_SALT_PREFERENCE_KEY, null);
    }

    @Override
    public void writeSalt(@Nullable String salt) {
        mPreferences.edit()
                .putString(PASSWORD_SALT_PREFERENCE_KEY, salt)
                .apply();
    }

    @NonNull
    @Override
    public Algorithm readCurrentAlgorithm() {
        return Algorithm.getFromText(
                mPreferences.getString(PASSWORD_ALGORITHM_PREFERENCE_KEY, null));
    }

    @Override
    public void writeCurrentAlgorithm(@NonNull Algorithm algorithm) {
        mPreferences.edit()
                .putString(PASSWORD_ALGORITHM_PREFERENCE_KEY, algorithm.getValue())
                .apply();
    }

    @NonNull
    @Override
    public String readPasscode() {
        return mPreferences.getString(PASSWORD_PREFERENCE_KEY, "");
    }

    @Override
    public void writePasscode(@NonNull String passcode) {
        mPreferences.edit()
                .putString(PASSWORD_PREFERENCE_KEY, passcode)
                .apply();
    }

    @Override
    public boolean hasPasscode() {
        return mPreferences.contains(PASSWORD_PREFERENCE_KEY);
    }

    @Override
    public void clearPasscode() {
        mPreferences.edit()
                .remove(PASSWORD_PREFERENCE_KEY)
                .apply();
    }

    @Override
    public void clear() {
        mPreferences.edit()
                .remove(PASSWORD_SALT_PREFERENCE_KEY)
                .remove(PASSWORD_PREFERENCE_KEY)
                .remove(PASSWORD_ALGORITHM_PREFERENCE_KEY)
                .apply();
    }
}
