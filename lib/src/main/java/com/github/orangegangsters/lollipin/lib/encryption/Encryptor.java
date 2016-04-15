package com.github.orangegangsters.lollipin.lib.encryption;

import android.text.TextUtils;

import com.github.orangegangsters.lollipin.lib.enums.Algorithm;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Used by {@link com.github.orangegangsters.lollipin.lib.managers.AppLockImpl} to get the SHA1
 * of the 4-digit password.
 */
public class Encryptor {

    /**
     * Convert a chain of bytes into a {@link java.lang.String}
     *
     * @param bytes The chain of bytes
     * @return The converted String
     */
    private static String bytes2Hex(byte[] bytes) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        return hs.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Allows to get the SHA of a {@link java.lang.String} using {@link java.security.MessageDigest}
     * if device does not support sha-256, fall back to sha-1 instead
     */
    public static String getSHA(String text, Algorithm algorithm) {
        String sha = "";
        if (TextUtils.isEmpty(text)) {
            return sha;
        }

        MessageDigest shaDigest = getShaDigest(algorithm);

        if (shaDigest != null) {
            byte[] textBytes = text.getBytes();
            shaDigest.update(textBytes, 0, text.length());
            byte[] shahash = shaDigest.digest();
            return bytes2Hex(shahash);
        }

        return null;
    }

    /**
     * Gets the default {@link MessageDigest} to use.
     * Select {@link Algorithm#SHA256} in {@link com.github.orangegangsters.lollipin.lib.managers.AppLockImpl#setPasscode(String)}
     * but can be {@link Algorithm#SHA1} for older versions.
     *
     * @param algorithm The {@link Algorithm} to use
     */
    private static MessageDigest getShaDigest(Algorithm algorithm) {
        switch (algorithm) {
            case SHA256:
                try {
                    return MessageDigest.getInstance("SHA-256");
                } catch (Exception e) {
                    try {
                        return MessageDigest.getInstance("SHA-1");
                    } catch (Exception e2) {
                        return null;
                    }
                }
            case SHA1:
            default:
                try {
                    return MessageDigest.getInstance("SHA-1");
                } catch (Exception e2) {
                    return null;
                }
        }
    }
}
