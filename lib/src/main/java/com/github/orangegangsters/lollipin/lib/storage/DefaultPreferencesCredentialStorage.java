package com.github.orangegangsters.lollipin.lib.storage;

import android.content.Context;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;
import com.github.orangegangsters.lollipin.lib.interfaces.CredentialStorage;

public class DefaultPreferencesCredentialStorage implements CredentialStorage {

    public DefaultPreferencesCredentialStorage(Context context) {

    }

    @Override
    public String readSalt() {
        return null;
    }

    @Override
    public void writeSalt(String salt) {

    }

    @Override
    public Algorithm readCurrentAlgorithm() {
        return null;
    }

    @Override
    public void writeCurrentAlgorithm(Algorithm algorithm) {

    }

    @Override
    public String readPasscode() {
        return null;
    }

    @Override
    public void writePasscode(String passcode) {

    }

    @Override
    public boolean hasPasscode() {
        return false;
    }

    @Override
    public void clearPasscode() {

    }

    @Override
    public void clear() {

    }
}
