package com.github.orangegangsters.lollipin.lib.interfaces;

import com.github.orangegangsters.lollipin.lib.enums.KeyboardButtonEnum;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 * The {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity} will implement
 * this in order to receive events from {@link com.github.orangegangsters.lollipin.lib.views.KeyboardButtonView}
 * and {@link com.github.orangegangsters.lollipin.lib.views.KeyboardView}
 */
public interface KeyboardButtonClickedListener {

    /**
     * Receive the click of a button, just after a {@link android.view.View.OnClickListener} has fired.
     * Called before {@link #onRippleAnimationEnd()}.
     * @param keyboardButtonEnum The organized enum of the clicked button
     */
    void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum);

    /**
     * Receive the end of a {@link com.andexert.library.RippleView} animation using a
     * {@link com.andexert.library.RippleAnimationListener} to determine the end.
     * Called after {@link #onKeyboardClick(com.github.orangegangsters.lollipin.lib.enums.KeyboardButtonEnum)}.
     */
    void onRippleAnimationEnd();

}
