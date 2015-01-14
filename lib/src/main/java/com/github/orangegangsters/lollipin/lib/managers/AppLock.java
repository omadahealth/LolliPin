package com.github.orangegangsters.lollipin.lib.managers;

import java.util.HashSet;

public abstract class AppLock {
    public static final int ENABLE_PINLOCK = 0;
    public static final int DISABLE_PINLOCK = 1;
    public static final int CHANGE_PIN = 2;
    public static final int UNLOCK_PIN = 3;

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_LOGO = "logo";

    public static final int DEFAULT_TIMEOUT = 1000 * 10; // 10sec

    protected HashSet<String> mIgnoredActivities;

    public AppLock() {
        mIgnoredActivities = new HashSet<String>();
    }

    public void addIgnoredActivity(Class<?> clazz) {
        String clazzName = clazz.getName();
        this.mIgnoredActivities.add(clazzName);
    }

    public void removeIgnoredActivity(Class<?> clazz) {
        String clazzName = clazz.getName();
        this.mIgnoredActivities.remove(clazzName);
    }

    public abstract void setTimeout(int timeout);

    public abstract long getTimeout();

    public abstract void setLogoId(int logoId);

    public abstract Integer getLogoId();

    public abstract void enable();

    public abstract void disable();

    public abstract boolean setPasscode(String passcode);

    public abstract boolean checkPasscode(String passcode);

    public abstract boolean isPasscodeSet();
}
