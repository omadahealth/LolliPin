package com.github.orangegangsters.lollipin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.R;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 */
public class KeyboardButtonView extends RelativeLayout {

    private Context mContext;

    public KeyboardButtonView(Context context) {
        this(context, null);
    }

    public KeyboardButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initializeView(attrs, defStyleAttr);
    }

    private void initializeView(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null && !isInEditMode()) {
            final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.KeyboardButtonView,
                    defStyleAttr, 0);
            String largeText = attributes.getString(R.styleable.KeyboardButtonView_large_text);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            KeyboardButtonView view = (KeyboardButtonView) inflater.inflate(R.layout.view_keyboard_button, this);

            TextView largeTextView = (TextView) view.findViewById(R.id.pin_code_textview_large);

            if (largeTextView != null && largeText != null) {
                largeTextView.setText(largeText);
            }
        }
    }
}
