package com.dewarder.realmstorage;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PasscodeData extends RealmObject {

    /**
     * Ensure that only one object of PasscodeData will be created in Realm
     */
    private static final long SINGLE_PRIMARY_KEY = 0L;

    @PrimaryKey
    private long id = SINGLE_PRIMARY_KEY;
    private int attempts;
    private String salt;
    private String algorithm;
    private String passcode;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
