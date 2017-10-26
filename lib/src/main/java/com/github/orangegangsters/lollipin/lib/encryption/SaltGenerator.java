package com.github.orangegangsters.lollipin.lib.encryption;

import android.util.Base64;

import java.security.SecureRandom;
import java.util.Arrays;

public final class SaltGenerator {

    private static final int KEY_LENGTH = 256;
    private static final String DEFAULT_PASSWORD_SALT = "7xn7@c$";

    public static String generate() {
        byte[] salt = new byte[KEY_LENGTH];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(System.currentTimeMillis());
            sr.nextBytes(salt);
            return Arrays.toString(salt);
        } catch (Exception e) {
            salt = DEFAULT_PASSWORD_SALT.getBytes();
        }
        return Base64.encodeToString(salt, Base64.DEFAULT);
    }
}
