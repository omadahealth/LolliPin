package com.github.orangegangsters.lollipin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.orangegangsters.lollipin.lib.R;

/**
 * Created by stoyan and olivier on 1/13/15.
 */
public class KeyboardView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

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
        if (attrs != null && !isInEditMode()) {
            final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.PinCodeView,
                    defStyleAttr, 0);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            KeyboardView view = (KeyboardView) inflater.inflate(R.layout.view_keyboard, this);

            initKeyboardButtons(view);
        }
    }

    private void initKeyboardButtons(KeyboardView view) {
        view.findViewById(R.id.pin_code_button_1).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_2).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_3).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_4).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_5).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_6).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_7).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_8).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_9).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_10).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_11).setOnClickListener(this);
        view.findViewById(R.id.pin_code_button_12).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO handle clicks
    }
}
