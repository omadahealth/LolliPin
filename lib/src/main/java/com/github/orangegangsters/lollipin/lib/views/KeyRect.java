package com.github.orangegangsters.lollipin.lib.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;

/**
 * Created by arjun on 8/3/16.
 */
public class KeyRect {
    private final View view;
    public Rect rect;
    public String value;
    public int rippleRadius = 0;
    public int requiredRadius;
    public int circleAlpha;
    private final Rect tempRect;
    public boolean hasRippleEffect = false;
    public ValueAnimator animator;
    private final int MAX_RIPPLE_ALPHA = 180;
    private InterpolatedValueListener interpolatedValueListener;
    private int animationLeftRepeatCount = 2;
    private int animationRightRepeatCount = 2;
    private int cycleCount = 4;

    public KeyRect(View view, Rect rect, String value) {
        this.view = view;
        this.rect = rect;
        this.tempRect = new Rect(rect);
        this.value = value;
        requiredRadius = (this.rect.right - this.rect.left) / 4;
        setUpAnimator();
    }

    private void setUpAnimator() {
        animator = ValueAnimator.ofFloat(0, requiredRadius);
        final int circleAlphaOffset = MAX_RIPPLE_ALPHA / requiredRadius;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (hasRippleEffect) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    rippleRadius = (int) animatedValue;
                    circleAlpha =
                            (int)(MAX_RIPPLE_ALPHA
                                    - (animatedValue
                                    * circleAlphaOffset));
                    interpolatedValueListener.onValueUpdated();
                }
            }
        });

        animator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                hasRippleEffect = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hasRippleEffect = false;
                rippleRadius = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setError() {
        ValueAnimator goLeftAnimator = ValueAnimator.ofInt(0, 5);
        goLeftAnimator.setInterpolator(new CycleInterpolator(2));
        goLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rect.left += (int) animation.getAnimatedValue();
                rect.right += (int) animation.getAnimatedValue();
                Log.i("Animation Error", "Go left " + rect.left + " " + rect.right + " " + (int) animation.getAnimatedValue());
                view.invalidate();
            }
        });
        goLeftAnimator.start();
    }

    public interface InterpolatedValueListener {
        void onValueUpdated();
    }

    public void setOnValueUpdateListener(InterpolatedValueListener listener) {
        this.interpolatedValueListener = listener;
    }

    public void playRippleAnim() {
        animator.end();
        setOnValueUpdateListener(new KeyRect.InterpolatedValueListener() {
            @Override
            public void onValueUpdated() {
                view.invalidate(rect);
            }
        });
        animator.start();
    }
}
