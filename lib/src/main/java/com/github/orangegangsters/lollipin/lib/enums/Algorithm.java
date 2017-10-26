package com.github.orangegangsters.lollipin.lib.enums;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by olivier.goutay on 4/15/16.
 */
public enum Algorithm {

    SHA1("1"), SHA256("2");

    private final String mValue;

    Algorithm(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    @NonNull
    public static Algorithm getFromText(@Nullable String text) {
        for (Algorithm algorithm : Algorithm.values()) {
            if (algorithm.mValue.equals(text)) {
                return algorithm;
            }
        }
        return SHA1;
    }
}
