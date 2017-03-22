package com.github.orangegangsters.lollipin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.github.orangegangsters.lollipin.lib.R;
import com.github.orangegangsters.lollipin.lib.enums.KeyboardButtonEnum;
import com.github.orangegangsters.lollipin.lib.interfaces.KeyboardButtonClickedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan and olivier on 1/13/15.
 */
public class KeyboardView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private KeyboardButtonClickedListener mKeyboardButtonClickedListener;

    private List<KeyboardButtonView> mButtons;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initializeView(attrs, defStyleAttr);
    }

    private void initializeView(AttributeSet attrs, int defStyleAttr) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            KeyboardView view = (KeyboardView) inflater.inflate(R.layout.view_keyboard, this);

            initKeyboardButtons(view);
        }
    }

    /**
     * Init the keyboard buttons (onClickListener)
     */
    private void initKeyboardButtons(KeyboardView view) {
        mButtons = new ArrayList<>();
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_0));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_1));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_2));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_3));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_4));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_5));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_6));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_7));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_8));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_9));
        mButtons.add((KeyboardButtonView) view.findViewById(R.id.pin_code_button_clear));

        for(View button : mButtons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(mKeyboardButtonClickedListener == null) {
            return;
        }

        int id = v.getId();
        if(id == R.id.pin_code_button_0) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_0);
        } else if(id == R.id.pin_code_button_1) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_1);
        } else if(id == R.id.pin_code_button_2) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_2);
        } else if(id == R.id.pin_code_button_3) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_3);
        } else if(id == R.id.pin_code_button_4) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_4);
        } else if(id == R.id.pin_code_button_5) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_5);
        } else if(id == R.id.pin_code_button_6) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_6);
        } else if(id == R.id.pin_code_button_7) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_7);
        } else if(id == R.id.pin_code_button_8) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_8);
        } else if(id == R.id.pin_code_button_9) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_9);
        } else if(id == R.id.pin_code_button_clear) {
            mKeyboardButtonClickedListener.onKeyboardClick(KeyboardButtonEnum.BUTTON_CLEAR);
        }
    }

    /**
     * Set the {@link com.andexert.library.RippleAnimationListener} to the
     * {@link com.github.orangegangsters.lollipin.lib.views.KeyboardButtonView}
     */
    public void setKeyboardButtonClickedListener(KeyboardButtonClickedListener keyboardButtonClickedListener) {
        this.mKeyboardButtonClickedListener = keyboardButtonClickedListener;
        for(KeyboardButtonView button : mButtons) {
            button.setOnRippleAnimationEndListener(mKeyboardButtonClickedListener);
        }
    }
}
