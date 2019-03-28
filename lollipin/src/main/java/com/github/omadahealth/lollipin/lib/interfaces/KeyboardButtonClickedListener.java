package com.github.omadahealth.lollipin.lib.interfaces;

import com.github.omadahealth.lollipin.lib.enums.KeyboardButtonEnum;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 * The {@link com.github.omadahealth.lollipin.lib.managers.AppLockActivity} will implement
 * this in order to receive events from {@link com.github.omadahealth.lollipin.lib.views.KeyboardButtonView}
 * and {@link com.github.omadahealth.lollipin.lib.views.KeyboardView}
 */
public interface KeyboardButtonClickedListener {

    /**
     * Receive the click of a button, just after a {@link android.view.View.OnClickListener} has fired.
     * Called before {@link #onRippleAnimationEnd()}.
     * @param keyboardButtonEnum The organized enum of the clicked button
     */
    public void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum);

    /**
     * Receive the end of a {@link com.andexert.library.RippleView} animation using a
     * {@link com.andexert.library.RippleAnimationListener} to determine the end.
     * Called after {@link #onKeyboardClick(com.github.omadahealth.lollipin.lib.enums.KeyboardButtonEnum)}.
     */
    public void onRippleAnimationEnd();

}
