package com.github.orangegangsters.lollipin.lib.storage;

import android.content.Context;

import com.github.orangegangsters.lollipin.lib.interfaces.ConfigurationStorage;
import com.github.orangegangsters.lollipin.lib.interfaces.CredentialStorage;

public class DefaultPreferencesConfigurationStorage implements ConfigurationStorage {

    public DefaultPreferencesConfigurationStorage(Context context) {

    }

    @Override
    public long readTimeout() {
        return 0;
    }

    @Override
    public void writeTimeout(long timeout) {

    }

    @Override
    public int readLogoId() {
        return 0;
    }

    @Override
    public void writeLogoId(int logoId) {

    }

    @Override
    public boolean readShouldShowForgot() {
        return false;
    }

    @Override
    public void writeShouldShowForgot(boolean showForgot) {

    }

    @Override
    public boolean readPinChallengeCanceled() {
        return false;
    }

    @Override
    public void writePinChallengeCanceled(boolean pinChallengeCanceled) {

    }

    @Override
    public boolean readOnlyBackgroundTimeout() {
        return false;
    }

    @Override
    public void writeOnlyBackgroundTimeout(boolean onlyBackgroundTimeout) {

    }

    @Override
    public boolean readFingerprintAuthEnabled() {
        return false;
    }

    @Override
    public void writeFingerprintAuthEnabled(boolean enabled) {

    }

    @Override
    public long readLastActiveMillis() {
        return 0;
    }

    @Override
    public void writeLastActiveMillis(long millis) {

    }

    @Override
    public void clear() {

    }
}
