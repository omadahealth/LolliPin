package com.github.orangegangsters.lollipin.lib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.orangegangsters.lollipin.lib.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arjun on 8/2/16.
 */
public class PassCodeView extends View {
    private boolean DEBUG = false;
    private final int KEYS_COUNT = 12;
    private final String eraseChar = "\u232B";
    private final int KEY_PAD_COLS = 3;
    private final int KEY_PAD_ROWS = 4;
    private int digits;
    private int filledCount = 0;
    private Bitmap filledDrawable;
    private Bitmap emptyDrawable;
    private Paint paint;
    private int DEFAULT_DRAWABLE_DIM;
    private int DEFAULT_VIEW_HEIGHT = 200;
    private int DRAWABLE_PADDING = 100;
    private int drawableWidth;
    private int drawableHeight;
    private int drawableStartX;
    private int drawableStartY;
    private static final int DIGIT_PADDING = 40;
    private int kpStartX;
    private int kpStartY;
    private ArrayList<KeyRect> keyRects = new ArrayList<>();
    private int keyWidth;
    private int keyHeight;
    private String passCodeText = "";
    private TextChangeListener textChangeListener;
    private boolean skipKeyDraw = false;
    private int touchSlope = 5;

    private Map<Integer, Integer> touchXMap = new HashMap<>();
    private Map<Integer, Integer> touchYMap = new HashMap<>();
    private Typeface typeFace;
    private TextPaint textPaint;
    private float keyTextSize;
    private long animDuration = 200;
    private Paint circlePaint;

    public PassCodeView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public PassCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public PassCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public PassCodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PassCodeView, defStyleAttr, defStyleRes);
        try {
            digits = values.getInteger(R.styleable.PassCodeView_digits, 4);
            float digitSize = values.getDimension(R.styleable.PassCodeView_digit_size,
                    getResources().getDimension(R.dimen.drawableDimen));
            keyTextSize = values.getDimension(R.styleable.PassCodeView_key_text_size,
                    getResources().getDimension(R.dimen.key_text_size));
            drawableWidth = (int) digitSize; //DEFAULT_DRAWABLE_DIM;
            drawableHeight = (int) digitSize; //DEFAULT_DRAWABLE_DIM;
//            filledCount = values.getInteger(R.styleable.PassCodeView_filled_count, 0);
            filledDrawable = getBitmap(values.getResourceId(R.styleable.PassCodeView_filled_drawable, -1));
            emptyDrawable = getBitmap(values.getResourceId(R.styleable.PassCodeView_empty_drawable, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        values.recycle();
        preparePaint();
    }

    private void preparePaint() {
        paint = new Paint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.argb(255, 0, 0, 0));
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setTextSize(keyTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setTypeFace(Typeface typeFace) {
        if (this.typeFace != typeFace) {
            this.typeFace = typeFace;
            textPaint.setTypeface(typeFace);
            requestLayout();
            invalidate();
        }
    }

    public void setKeyTextColor(int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        textPaint.setColor(colorStateList.getColorForState(getDrawableState(), 0));
        invalidate();
    }

    public void setKeyTextSize (float size) {
        textPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    private void computeDrawableStartXY() {
        int totalDrawableWidth = digits * drawableWidth;
        int totalPaddingWidth = DIGIT_PADDING * (digits - 1);
        int totalReqWidth = totalDrawableWidth + totalPaddingWidth;
        drawableStartX = getMeasuredWidth() / 2 - totalReqWidth / 2;
        drawableStartY = (drawableHeight + DRAWABLE_PADDING) / 2 - drawableHeight / 2;
        computeKeyboardStartXY();
    }

    private void computeKeyboardStartXY() {
        kpStartX = 0;
        kpStartY = drawableHeight + DRAWABLE_PADDING;
        keyWidth = getMeasuredWidth() / KEY_PAD_COLS;
        keyHeight = (getMeasuredHeight()
                - (drawableHeight + 2 * DRAWABLE_PADDING)) / KEY_PAD_ROWS;
        initialiseKeyRects();
    }

    private void initialiseKeyRects() {
        keyRects.clear();
        int x = kpStartX, y = kpStartY;
        for (int i = 1 ; i <= KEYS_COUNT ; i ++) {
            keyRects.add(
                    new KeyRect(this,
                        new Rect(x, y, x + keyWidth, y + keyHeight),
                        String.valueOf(i)));
            x = x + keyWidth;
            if (i % 3 == 0) {
                y = y + keyHeight;
                x = kpStartX;
            }
        }
        // TODO: 8/3/16 looks bad
        keyRects.get(9).setValue("");
        keyRects.get(10).setValue("0");
        keyRects.get(11).setValue(eraseChar);
    }

    private Bitmap getBitmap(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCodeText(canvas);
        drawKeyPad(canvas);
    }

    private void drawKeyPad(Canvas canvas) {
        float centerHalf = (textPaint.descent() + textPaint.ascent()) / 2;
        for (KeyRect rect : keyRects) {
            canvas.drawText(rect.value,
                    rect.rect.exactCenterX(),
                    rect.rect.exactCenterY() - centerHalf, textPaint);
            if (rect.hasRippleEffect) {
                circlePaint.setAlpha(rect.circleAlpha);
                canvas.drawCircle(rect.rect.exactCenterX(), rect.rect.exactCenterY(),
                        rect.rippleRadius, circlePaint);
            }
            if (DEBUG) {
                canvas.drawLine(rect.rect.left,
                        rect.rect.centerY(),
                        rect.rect.right, rect.rect.centerY(), textPaint);
                canvas.drawLine(rect.rect.centerX(),
                        rect.rect.top,
                        rect.rect.centerX(), rect.rect.bottom, textPaint);
            }
        }
    }

    private void drawCodeText(Canvas canvas) {
        int x = drawableStartX, y = drawableStartY;
        int totalContentWidth = drawableWidth + DIGIT_PADDING;
        for (int i = 1 ; i <= filledCount ; i ++) {
            canvas.drawBitmap(filledDrawable, x, y, paint);
            x += totalContentWidth;
        }
        for (int i = 1 ; i <= (digits - filledCount) ; i ++) {
            canvas.drawBitmap(emptyDrawable, x, y, paint);
            x += totalContentWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: 8/3/16 mind the padding
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int measuredWidth = 0, measuredHeight = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            double height = MeasureSpec.getSize(heightMeasureSpec) * 0.7;
            measuredHeight = (int)height;// + paddingTop + paddingBottom;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
        computeDrawableStartXY();
    }

    private void setFilledCount(int count) {
        filledCount = count > digits ? digits : count;
        invalidate(drawableStartX,
                drawableStartX,
                drawableStartX + getMeasuredWidth(),
                drawableStartY + getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            return processTouch(event);
    }

    private boolean processTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int pointerDownId = event.getPointerId(event.getActionIndex());
                touchXMap.put(pointerDownId, (int) event.getX());
                touchYMap.put(pointerDownId, (int) event.getY());
                break;

            case MotionEvent.ACTION_UP:
                int pointerUpId = event.getPointerId(event.getActionIndex());
                int pointerUpIndex = event.findPointerIndex(pointerUpId);
                int eventX = (int) event.getX(pointerUpIndex);
                int eventY = (int) event.getY(pointerUpIndex);
                findKeyPressed(touchXMap.get(pointerUpId),
                        touchYMap.get(pointerUpId),eventX, eventY);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("Pointer", "down");
                int pointerActionDownId = event.getPointerId(event.getActionIndex());
                touchXMap.put(pointerActionDownId, (int) event.getX(event.getActionIndex()));
                touchYMap.put(pointerActionDownId, (int) event.getY(event.getActionIndex()));
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerActionUpIndex = event.getActionIndex();
                int pointerActionUpId = event.getPointerId(pointerActionUpIndex);
                int eventPointerX = (int) event.getX(pointerActionUpIndex);
                int eventPointerY = (int) event.getY(pointerActionUpIndex);
                findKeyPressed(touchXMap.get(pointerActionUpId),
                        touchYMap.get(pointerActionUpId), eventPointerX, eventPointerY);
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    private void findKeyPressed(int downEventX, int downEventY, int upEventX, int upEventY) {
        for (KeyRect keyRect : keyRects) {
            if (keyRect.rect.contains(downEventX, downEventY)
                    && keyRect.rect.contains(upEventX, upEventY)) {
                int length = passCodeText.length();
                if (keyRect.value.equals(eraseChar)) {
                    if (length > 0) {
                        passCodeText = passCodeText.substring(0, passCodeText.length() - 1);
                        invalidateAndNotifyListener();
                    }
                } else if (!keyRect.value.isEmpty() && length < digits) {
                    passCodeText = passCodeText + keyRect.value;
                    invalidateAndNotifyListener();
                }
                keyRect.playRippleAnim();
            }
        }
    }

    public void reset() {
        this.passCodeText = "";
        filledCount = 0;
        invalidate();
    }

    public interface TextChangeListener {
        void onTextChanged(String text);
    }

    private void invalidateAndNotifyListener() {
        setFilledCount(passCodeText.length());
        Log.i("New text", passCodeText);
        if (textChangeListener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    textChangeListener.onTextChanged(passCodeText);
                }
            }, 500);
        }
    }

    public void setOnTextChangeListener(TextChangeListener listener) {
        this.textChangeListener = listener;
    }

    public void removeOnTextChangeListener() {
        this.textChangeListener = null;
    }

    public void setError(boolean reset) {
        if (reset) {
            reset();
        }
        for (KeyRect keyRect : keyRects) {
            keyRect.setError();
        }
    }
}
