package com.github.orangegangsters.lollipin.lib.managers;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.PinActivity;


public class AppLockActivity extends PinActivity {
	public static final String TAG = "AppLockActivity";

	private int type = -1;
	private String oldPasscode = null;

	protected EditText codeField1 = null;
	protected EditText codeField2 = null;
	protected EditText codeField3 = null;
	protected EditText codeField4 = null;
	protected InputFilter[] filters = null;
	protected TextView tvMessage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.page_passcode);
//
//		tvMessage = (TextView) findViewById(R.id.tv_message);
//
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			String message = extras.getString(AppLock.MESSAGE);
//			if (message != null) {
//				tvMessage.setText(message);
//			}
//
//			type = extras.getInt(AppLock.TYPE, -1);
//		}
//
//		filters = new InputFilter[2];
//		filters[0] = new InputFilter.LengthFilter(1);
//		filters[1] = numberFilter;
//
//		codeField1 = (EditText) findViewById(R.id.passcode_1);
//		setupEditText(codeField1);
//
//		codeField2 = (EditText) findViewById(R.id.passcode_2);
//		setupEditText(codeField2);
//
//		codeField3 = (EditText) findViewById(R.id.passcode_3);
//		setupEditText(codeField3);
//
//		codeField4 = (EditText) findViewById(R.id.passcode_4);
//		setupEditText(codeField4);
//
//		// setup the keyboard
//		((Button) findViewById(R.id.button0)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button1)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button2)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button3)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button4)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button5)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button6)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button7)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button8)).setOnClickListener(btnListener);
//		((Button) findViewById(R.id.button9)).setOnClickListener(btnListener);
//
//		((Button) findViewById(R.id.button_clear))
//				.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						clearFields();
//					}
//				});
//
//		((Button) findViewById(R.id.button_erase))
//				.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						onDeleteKey();
//					}
//				});
//
//		overridePendingTransition(R.anim.slide_up, R.anim.nothing);
//
//		switch (type) {
//
//		case AppLock.DISABLE_PINLOCK:
//			this.setTitle("Disable Passcode");
//			break;
//		case AppLock.ENABLE_PINLOCK:
//			this.setTitle("Enable Passcode");
//			break;
//		case AppLock.CHANGE_PIN:
//			this.setTitle("Change Passcode");
//			break;
//		case AppLock.UNLOCK_PIN:
//			this.setTitle("Unlock Passcode");
//			break;
//		}
	}

	public int getType() {
		return type;
	}

	protected void onPasscodeInputed() {
//		String passLock = codeField1.getText().toString()
//				+ codeField2.getText().toString()
//				+ codeField3.getText().toString() + codeField4.getText();
//
//		codeField1.setText("");
//		codeField2.setText("");
//		codeField3.setText("");
//		codeField4.setText("");
//		codeField1.requestFocus();
//
//		switch (type) {
//
//		case AppLock.DISABLE_PINLOCK:
//			if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//				setResult(RESULT_OK);
//				LockManager.getInstance().getAppLock().setPasscode(null);
//				finish();
//			} else {
//				onPasscodeError();
//			}
//			break;
//
//		case AppLock.ENABLE_PINLOCK:
//			if (oldPasscode == null) {
//				tvMessage.setText(R.string.reenter_passcode);
//				oldPasscode = passLock;
//			} else {
//				if (passLock.equals(oldPasscode)) {
//					setResult(RESULT_OK);
//					LockManager.getInstance().getAppLock()
//							.setPasscode(passLock);
//					finish();
//				} else {
//					oldPasscode = null;
//					tvMessage.setText(R.string.enter_passcode);
//					onPasscodeError();
//				}
//			}
//			break;
//
//		case AppLock.CHANGE_PIN:
//			if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//				tvMessage.setText(R.string.enter_passcode);
//				type = AppLock.ENABLE_PINLOCK;
//			} else {
//				onPasscodeError();
//			}
//			break;
//
//		case AppLock.UNLOCK_PIN:
//			if (LockManager.getInstance().getAppLock().checkPasscode(passLock)) {
//				setResult(RESULT_OK);
//				finish();
//			} else {
//				onPasscodeError();
//			}
//			break;
//
//		default:
//			break;
//		}
	}

	@Override
	public void onBackPressed() {
		if (type == AppLock.UNLOCK_PIN) {
			// back to home screen
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			finish();
		} else {
			finish();
		}
	}

	protected void setupEditText(EditText editText) {
		editText.setInputType(InputType.TYPE_NULL);
		editText.setFilters(filters);
		editText.setOnTouchListener(touchListener);
		editText.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL) {
			onDeleteKey();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onDeleteKey() {
		if (codeField1.isFocused()) {

		} else if (codeField2.isFocused()) {
			codeField1.requestFocus();
			codeField1.setText("");
		} else if (codeField3.isFocused()) {
			codeField2.requestFocus();
			codeField2.setText("");
		} else if (codeField4.isFocused()) {
			codeField3.requestFocus();
			codeField3.setText("");
		}
	}

	private OnClickListener btnListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
//			int currentValue = -1;
//			int id = view.getId();
//			if (id == R.id.button0) {
//				currentValue = 0;
//			} else if (id == R.id.button1) {
//				currentValue = 1;
//			} else if (id == R.id.button2) {
//				currentValue = 2;
//			} else if (id == R.id.button3) {
//				currentValue = 3;
//			} else if (id == R.id.button4) {
//				currentValue = 4;
//			} else if (id == R.id.button5) {
//				currentValue = 5;
//			} else if (id == R.id.button6) {
//				currentValue = 6;
//			} else if (id == R.id.button7) {
//				currentValue = 7;
//			} else if (id == R.id.button8) {
//				currentValue = 8;
//			} else if (id == R.id.button9) {
//				currentValue = 9;
//			} else {
//			}
//
//			// set the value and move the focus
//			String currentValueString = String.valueOf(currentValue);
//			if (codeField1.isFocused()) {
//				codeField1.setText(currentValueString);
//				codeField2.requestFocus();
//				codeField2.setText("");
//			} else if (codeField2.isFocused()) {
//				codeField2.setText(currentValueString);
//				codeField3.requestFocus();
//				codeField3.setText("");
//			} else if (codeField3.isFocused()) {
//				codeField3.setText(currentValueString);
//				codeField4.requestFocus();
//				codeField4.setText("");
//			} else if (codeField4.isFocused()) {
//				codeField4.setText(currentValueString);
//			}
//
//			if (codeField4.getText().toString().length() > 0
//					&& codeField3.getText().toString().length() > 0
//					&& codeField2.getText().toString().length() > 0
//					&& codeField1.getText().toString().length() > 0) {
//				onPasscodeInputed();
//			}
		}
	};

	protected void onPasscodeError() {
//		Toast toast = Toast.makeText(this, getString(R.string.passcode_wrong),
//                Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
//		toast.show();
//
//		Thread thread = new Thread() {
//			public void run() {
//				Animation animation = AnimationUtils.loadAnimation(
//                        AppLockActivity.this, R.anim.shake);
//				findViewById(R.id.ll_applock).startAnimation(animation);
//				codeField1.setText("");
//				codeField2.setText("");
//				codeField3.setText("");
//				codeField4.setText("");
//				codeField1.requestFocus();
//			}
//		};
//		runOnUiThread(thread);
	}

	private InputFilter numberFilter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			if (source.length() > 1) {
				return "";
			}

			if (source.length() == 0) // erase
			{
				return null;
			}

			try {
				int number = Integer.parseInt(source.toString());
				if ((number >= 0) && (number <= 9))
					return String.valueOf(number);
				else
					return "";
			} catch (NumberFormatException e) {
				return "";
			}
		}
	};

	private OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			v.performClick();
			clearFields();
			return false;
		}
	};

	private void clearFields() {
		codeField1.setText("");
		codeField2.setText("");
		codeField3.setText("");
		codeField4.setText("");

		codeField1.postDelayed(new Runnable() {

			@Override
			public void run() {
				codeField1.requestFocus();
			}
		}, 200);
	}
}