package com.dewarder.realmstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.security.SecureRandom;

import io.realm.RealmConfiguration;

public final class RealmEncryptionKeyManager {

    public static RealmEncryptionKeyProvider getKeyProvider(Context context) {
        return new SharedPreferencesKeyProvider(context);
    }

    private static class SharedPreferencesKeyProvider implements RealmEncryptionKeyProvider {

        private static final String REALM_KEY_PREFERENCE = "REALM_KEY_PREFERENCE_KEY";

        private final SharedPreferences mPreferences;

        SharedPreferencesKeyProvider(Context context) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Override
        public byte[] provideEncryptionKey() {
            if (mPreferences.contains(REALM_KEY_PREFERENCE)) {
                String key = mPreferences.getString(REALM_KEY_PREFERENCE, null);
                if (key == null) {
                    throw new IllegalStateException("Something went wrong");
                }
                return Base64.decode(key, Base64.NO_WRAP);
            }
            byte[] newKey = generateRealmKey();
            mPreferences.edit()
                    .putString(REALM_KEY_PREFERENCE, Base64.encodeToString(newKey, Base64.NO_WRAP))
                    .apply();
            return newKey;
        }
    }

    private static byte[] generateRealmKey() {
        byte[] realmKey = new byte[RealmConfiguration.KEY_LENGTH];
        new SecureRandom().nextBytes(realmKey);
        return realmKey;
    }
}
