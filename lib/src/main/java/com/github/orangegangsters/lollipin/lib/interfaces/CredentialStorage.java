package com.github.orangegangsters.lollipin.lib.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;

public interface CredentialStorage {

    int readAttemptsCount();

    void writeAttemptsCount(int attempts);

    @Nullable
    String readSalt();

    void writeSalt(@Nullable String salt);

    @NonNull
    Algorithm readCurrentAlgorithm();

    void writeCurrentAlgorithm(@NonNull Algorithm algorithm);

    @NonNull
    String readPasscode();

    void writePasscode(@NonNull String passcode);

    boolean hasPasscode();

    void clearPasscode();

    void clear();
}
