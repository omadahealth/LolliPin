package com.github.omadahealth.lollipin.lib.enums;

/**
 * Created by olivier.goutay on 4/15/16.
 */
public enum Algorithm {

    SHA1("1"), SHA256("2");

    private String mValue;

    Algorithm(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public static Algorithm getFromText(String text) {
        for (Algorithm algorithm : Algorithm.values()) {
            if (algorithm.mValue.equals(text)) {
                return algorithm;
            }
        }
        return SHA1;
    }
}
