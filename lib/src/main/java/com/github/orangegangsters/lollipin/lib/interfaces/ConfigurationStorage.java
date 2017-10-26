package com.github.orangegangsters.lollipin.lib.interfaces;

public interface ConfigurationStorage {

    long readTimeout();

    void writeTimeout(long timeout);

    int readLogoId();

    void writeLogoId(int logoId);

    boolean readShouldShowForgot();

    void writeShouldShowForgot(boolean showForgot);

    boolean readPinChallengeCanceled();

    void writePinChallengeCanceled(boolean pinChallengeCanceled);

    boolean readOnlyBackgroundTimeout();

    void writeOnlyBackgroundTimeout(boolean onlyBackgroundTimeout);

    boolean readFingerprintAuthEnabled();

    void writeFingerprintAuthEnabled(boolean enabled);

    long readLastActiveMillis();

    void writeLastActiveMillis(long millis);

    void clear();
}
