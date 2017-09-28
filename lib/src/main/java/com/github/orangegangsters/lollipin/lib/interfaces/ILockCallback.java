package com.github.orangegangsters.lollipin.lib.interfaces;

public interface ILockCallback {

    boolean onCheckPasscode(String inputPasscode);

    boolean onSetPasscode(String passcode);
}
