package com.github.orangegangsters.lollipin.lib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.orangegangsters.lollipin.lib.R;

import java.util.Hashtable;

/**
 * Created by stoyan and oliviergoutay on 12/8/14.
 *
 * Used for loading custom fonts located into assets
 */
public class TypefaceTextView extends TextView {
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    public TypefaceTextView(Context context) {
        super(context);
        setCustomTypeface(context, null);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(context, attrs);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setCustomTypeface(context, attrs);
    }

    private void setCustomTypeface(Context context, AttributeSet attrs) {
        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode() || attrs == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.PinCodeView);
        String fontName = styledAttrs.getString(R.styleable.PinCodeView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = getFont(context, fontName);
            setTypeface(typeface);
        }
    }

    /**
     * Avoid reloading assets every time
     */
    public static Typeface getFont(Context context, String fontName) {
        synchronized (cache) {
            if (!cache.containsKey(fontName)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), fontName);
                    cache.put(fontName, t);
                } catch (Exception e) {
                    return null;
                }
            }
            return cache.get(fontName);
        }
    }
}