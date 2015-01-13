package com.github.orangegangsters.lollipin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.orangegangsters.lollipin.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan and oliviergoutay on 1/13/15.
 */
public class PinCodeRoundView extends RelativeLayout {

    private Context mContext;
    private List<ImageView> mRoundViews;

    public PinCodeRoundView(Context context) {
        this(context, null);
    }

    public PinCodeRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCodeRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initializeView(attrs, defStyleAttr);
    }

    private void initializeView(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null && !isInEditMode()) {
            final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.PinCodeView,
                    defStyleAttr, 0);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PinCodeRoundView view = (PinCodeRoundView) inflater.inflate(R.layout.view_round_pin_code, this);

            mRoundViews = new ArrayList<>();
            mRoundViews.add((ImageView) view.findViewById(R.id.pin_code_round1));
            mRoundViews.add((ImageView) view.findViewById(R.id.pin_code_round2));
            mRoundViews.add((ImageView) view.findViewById(R.id.pin_code_round3));
            mRoundViews.add((ImageView) view.findViewById(R.id.pin_code_round4));
        }
    }
}
