package com.github.orangegangsters.lollipin.lib.interfaces;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;

public interface CredentialStorage {

    String readSalt();

    void writeSalt(String salt);

    Algorithm readCurrentAlgorithm();

    void writeCurrentAlgorithm(Algorithm algorithm);

    String readPasscode();

    void writePasscode(String passcode);

    boolean hasPasscode();

    void clearPasscode();

    void clear();
}
