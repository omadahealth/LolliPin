package com.github.orangegangsters.lollipin.lib.managers;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.R;
import com.github.orangegangsters.lollipin.lib.enums.KeyboardButtonEnum;
import com.github.orangegangsters.lollipin.lib.interfaces.KeyboardButtonClickedListener;
import com.github.orangegangsters.lollipin.lib.views.KeyboardView;
import com.github.orangegangsters.lollipin.lib.views.PinCodeRoundView;

/**
 * Created by stoyan and olivier on 1/13/15.
 */
public class AppLockActivity extends PinActivity implements KeyboardButtonClickedListener {

    public static final String TAG = "AppLockActivity";
    private static final int PIN_CODE_LENGTH = 4;

    private TextView mStepTextView;
    private PinCodeRoundView mPinCodeRoundView;
    private KeyboardView mKeyboardView;
    private LockManager mLockManager;

    private int mType = AppLock.UNLOCK_PIN;
    private String mPinCode;
    private String mOldPinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.nothing, R.anim.nothing);

        setContentView(R.layout.activity_pin_code);

        mLockManager = LockManager.getInstance(this);
        mPinCode = "";
        mOldPinCode = "";

        mStepTextView = (TextView) this.findViewById(R.id.pin_code_step_textview);
        mPinCodeRoundView = (PinCodeRoundView) this.findViewById(R.id.pin_code_round_view);
        mKeyboardView = (KeyboardView) this.findViewById(R.id.pin_code_keyboard_view);
        mKeyboardView.setKeyboardButtonClickedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mType = extras.getInt(AppLock.TYPE, AppLock.UNLOCK_PIN);
        }

        switch (mType) {
            case AppLock.DISABLE_PINLOCK:
                mStepTextView.setText("Disable Passcode");
                break;
            case AppLock.ENABLE_PINLOCK:
                mStepTextView.setText("Enable Passcode");
                break;
            case AppLock.CHANGE_PIN:
                mStepTextView.setText("Change Passcode");
                break;
            case AppLock.UNLOCK_PIN:
                mStepTextView.setText("Unlock Passcode");
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    @Override
    public void onKeyboardClick(KeyboardButtonEnum keyboardButtonEnum) {
        int value = keyboardButtonEnum.getButtonValue();
        Toast.makeText(this, "" + value, Toast.LENGTH_SHORT).show();

        if (value == KeyboardButtonEnum.BUTTON_CLEAR.getButtonValue()) {
            mPinCode = "";
            mPinCodeRoundView.refresh(mPinCode.length());
        } else {
            mPinCode += keyboardButtonEnum.getButtonValue();
            mPinCodeRoundView.refresh(mPinCode.length());
            if (mPinCode.length() == PIN_CODE_LENGTH) {
                onPasscodeInputed();
            }
        }

    }

    protected void onPasscodeInputed() {
        switch (mType) {
            case AppLock.DISABLE_PINLOCK:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    setResult(RESULT_OK);
                    mLockManager.getAppLock().setPasscode(null);
                    finish();
                } else {
                    onPasscodeError();
                }
                break;
            case AppLock.ENABLE_PINLOCK:
                if (mOldPinCode == null) {
                    mStepTextView.setText("Enter again");
                    mOldPinCode = mPinCode;
                } else {
                    if (mPinCode.equals(mOldPinCode)) {
                        setResult(RESULT_OK);
                        mLockManager.getAppLock().setPasscode(mPinCode);
                        finish();
                    } else {
                        mOldPinCode = null;
                        mStepTextView.setText("Enter passcode");
                        onPasscodeError();
                    }
                }
                break;
            case AppLock.CHANGE_PIN:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    mStepTextView.setText("Enter passcode");
                    mType = AppLock.ENABLE_PINLOCK;
                } else {
                    onPasscodeError();
                }
                break;
            case AppLock.UNLOCK_PIN:
                if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    onPasscodeError();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//		if (type == AppLock.UNLOCK_PIN) {
//			// back to home screen
//			Intent intent = new Intent();
//			intent.setAction(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			this.startActivity(intent);
//			finish();
//		} else {
//			finish();
//		}
    }

    protected void onPasscodeError() {
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

    public int getType() {
        return mType;
    }
}