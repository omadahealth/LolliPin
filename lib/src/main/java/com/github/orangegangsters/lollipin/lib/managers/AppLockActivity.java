package com.github.orangegangsters.lollipin.lib.managers;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.R;
import com.github.orangegangsters.lollipin.lib.enums.KeyboardButtonEnum;
import com.github.orangegangsters.lollipin.lib.interfaces.KeyboardButtonClickedListener;
import com.github.orangegangsters.lollipin.lib.views.KeyboardView;
import com.github.orangegangsters.lollipin.lib.views.PinCodeRoundView;
import com.github.orangegangsters.lollipin.lib.views.TypefaceTextView;

/**
 * Created by stoyan and olivier on 1/13/15.
 */
public abstract class AppLockActivity extends PinActivity implements KeyboardButtonClickedListener, View.OnClickListener {

    public static final String TAG = "AppLockActivity";
    /**
     * The PIN length
     */
    private static final int PIN_CODE_LENGTH = 4;

    private TextView mStepTextView;
    private PinCodeRoundView mPinCodeRoundView;
    private KeyboardView mKeyboardView;
    private LockManager mLockManager;
    private TypefaceTextView mForgotTextView;

    private int mType = AppLock.UNLOCK_PIN;
    private int mLogoId;
    private String mPinCode;
    private String mOldPinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.nothing, R.anim.nothing);

        setContentView(R.layout.activity_pin_code);

        mLockManager = LockManager.getInstance();
        mPinCode = "";
        mOldPinCode = "";

        mStepTextView = (TextView) this.findViewById(R.id.pin_code_step_textview);
        mPinCodeRoundView = (PinCodeRoundView) this.findViewById(R.id.pin_code_round_view);
        mForgotTextView = (TypefaceTextView) this.findViewById(R.id.pin_code_forgot_textview);
        mForgotTextView.setOnClickListener(this);
        mKeyboardView = (KeyboardView) this.findViewById(R.id.pin_code_keyboard_view);
        mKeyboardView.setKeyboardButtonClickedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mType = extras.getInt(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        }

        findViewById(R.id.pin_code_logo_imageview).setBackgroundResource(mLockManager.getAppLock().getLogoId());

        initText();
    }

    /**
     * Init the {@link #mStepTextView} based on {@link #mType}
     */
    private void initText() {
        switch (mType) {
            case AppLock.DISABLE_PINLOCK:
                mStepTextView.setText(getString(R.string.pin_code_step_disable));
                break;
            case AppLock.ENABLE_PINLOCK:
                mStepTextView.setText(getString(R.string.pin_code_step_create));
                break;
            case AppLock.CHANGE_PIN:
                mStepTextView.setText(getString(R.string.pin_code_step_change));
                break;
            case AppLock.UNLOCK_PIN:
                mStepTextView.setText(getString(R.string.pin_code_step_unlock));
                break;
        }
    }

    /**
     * Overrides to allow a slide_down animation when finishing
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    /**
     * Add the button clicked to {@link #mPinCode} each time.
     * Refreshes also the {@link com.github.orangegangsters.lollipin.lib.views.PinCodeRoundView}
     */
    @Override
    public void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum) {
        int value = keyboardButtonEnum.getButtonValue();

        if (value == KeyboardButtonEnum.BUTTON_CLEAR.getButtonValue()) {
            setPinCode("");
        } else {
            setPinCode(mPinCode + value);
        }
    }

    /**
     * Called at the end of the animation of the {@link com.andexert.library.RippleView}
     * Calls {@link #onPinCodeInputed} when {@link #mPinCode}
     */
    @Override
    public void onRippleAnimationEnd() {
        if (mPinCode.length() == PIN_CODE_LENGTH) {
            onPinCodeInputed();
        }
    }

    /**
     * Switch over the {@link #mType} to determine if the password is ok, if we should pass to the next step etc...
     */
    protected void onPinCodeInputed() {
        switch (mType) {
            case AppLock.DISABLE_PINLOCK:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    setResult(RESULT_OK);
                    mLockManager.getAppLock().setPasscode(null);
                    finish();
                } else {
                    onPinCodeError();
                }
                break;
            case AppLock.ENABLE_PINLOCK:
                if (mOldPinCode == null || mOldPinCode.length() == 0) {
                    mStepTextView.setText(getString(R.string.pin_code_step_enable_confirm));
                    mOldPinCode = mPinCode;
                    setPinCode("");
                } else {
                    if (mPinCode.equals(mOldPinCode)) {
                        setResult(RESULT_OK);
                        mLockManager.getAppLock().setPasscode(mPinCode);
                        finish();
                    } else {
                        mOldPinCode = "";
                        setPinCode("");
                        mStepTextView.setText(getString(R.string.pin_code_step_create));
                        onPinCodeError();
                    }
                }
                break;
            case AppLock.CHANGE_PIN:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    mStepTextView.setText(getString(R.string.pin_code_step_create));
                    mType = AppLock.ENABLE_PINLOCK;
                    setPinCode("");
                    initText();
                } else {
                    onPinCodeError();
                }
                break;
            case AppLock.UNLOCK_PIN:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onPinCodeError();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Override {@link #onBackPressed()} to prevent user for finishing the activity
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * Displays the information dialog when the user clicks the
     * {@link #mForgotTextView}
     */
    public abstract void showForgotDialog();

    /**
     * Run a shake animation when the password is not valid.
     */
    protected void onPinCodeError() {
        Thread thread = new Thread() {
            public void run() {
                mPinCode = "";
                mPinCodeRoundView.refresh(mPinCode.length());
                Animation animation = AnimationUtils.loadAnimation(
                        AppLockActivity.this, R.anim.shake);
                mKeyboardView.startAnimation(animation);
            }
        };
        runOnUiThread(thread);
    }

    /**
     * Set the pincode and refreshes the {@link com.github.orangegangsters.lollipin.lib.views.PinCodeRoundView}
     */
    public void setPinCode(String pinCode) {
        mPinCode = pinCode;
        mPinCodeRoundView.refresh(mPinCode.length());
    }

    /**
     * Returns the type of this {@link com.github.orangegangsters.lollipin.lib.managers.AppLockActivity}
     */
    public int getType() {
        return mType;
    }

    /**
     * When we click on the {@link #mForgotTextView} handle the pop-up
     * dialog
     *
     * @param view {@link #mForgotTextView}
     */
    @Override
    public void onClick(View view) {
        showForgotDialog();
    }
}