package com.github.orangegangsters.lollipin.lib.enums;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 */
public enum KeyboardButtonEnum {

    BUTTON_0(0),
    BUTTON_1(1),
    BUTTON_2(2),
    BUTTON_3(3),
    BUTTON_4(4),
    BUTTON_5(5),
    BUTTON_6(6),
    BUTTON_7(7),
    BUTTON_8(8),
    BUTTON_9(9),
    BUTTON_CLEAR(-1);

    private int mButtonValue;

    KeyboardButtonEnum(int value) {
        this.mButtonValue = value;
    }

    /**
     * Get the button value (numeric)
     * @return 0-9 values for digits, -1 for clear button
     */
    public int getButtonValue() {
        return mButtonValue;
    }
}
