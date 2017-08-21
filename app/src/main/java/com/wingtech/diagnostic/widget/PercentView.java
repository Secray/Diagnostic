package com.wingtech.diagnostic.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.Log;


/**
 * @author secray
 * @date 2017-07-03
 */

public class PercentView extends View {
    private static final int DEFAULT_TEXT_SIZE = 100;
    private static final String CHARACTER_PERCENT = "%";
    // Default first color
    private static final int FIRST_COLOR = Color.parseColor("#0092cf");
    // Default second color
    private static final int SECOND_COLOR = Color.parseColor("#00f6ff");
    private static final int BACKGROUND_COLOR = Color.parseColor("#16485f");
    // Direction top as default
    public static final int TOP = 1, BOTTOM = 0;
    // Typeface Normal as default
    public static final int NORMAL = 0, SANS = 1, SERIF = 2, MONOSPACE = 3;

    public static final int GRAVITY_TOP = 0, GRAVITY_BOTTOM = 1, GRAVITY_CENTER = 2;

    private Paint mPaint;
    private Paint mPercentPaint;
    private Rect mBound;
    private Context mContext;

    private String mText;
    private int mFirstColor;
    private int mSecondColor;
    private int mBackgroundColor;
    private int mDirection;
    private int mGravity;
    private int mTextSize;
    private float mTextWidth;
    private float mTextHeight;
    private Paint.FontMetricsInt mFm;
    private float mStartX;
    private float mStartY;
    private float mClipStartY;
    private float mClipEndY;
    private Typeface mTypeface;
    private LinearGradient mGradient;

    private float mPercent = 0f;

    enum M {
        WIDTH,
        HEIGHT
    }

    enum VALUE {
        DP,
        SP
    }

    public PercentView(Context context) {
        this(context, null);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PercentView);

        mFirstColor = a.getColor(R.styleable.PercentView_firstColor, FIRST_COLOR);
        mSecondColor = a.getColor(R.styleable.PercentView_secondColor, SECOND_COLOR);
        mBackgroundColor = a.getColor(R.styleable.PercentView_backgroundColor, BACKGROUND_COLOR);
        mDirection = a.getInt(R.styleable.PercentView_direction, TOP);
        mTextSize = a.getDimensionPixelSize(R.styleable.PercentView_textSize, DEFAULT_TEXT_SIZE);
        mText = a.getString(R.styleable.PercentView_text);
        mGravity = a.getInt(R.styleable.PercentView_gravity, GRAVITY_CENTER);
        int typeface = a.getInt(R.styleable.PercentView_typeface, NORMAL);

        a.recycle();

        mContext = context;
        mTypeface = obtainTypeface(typeface);
        mBound = new Rect();
        mGradient = new LinearGradient(0f, 0f, 0f, 0f, mFirstColor, mSecondColor,
                Shader.TileMode.CLAMP);

        initPaint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(M.WIDTH, widthMeasureSpec);
        int height = measureSize(M.HEIGHT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDirection == TOP) {
            drawText(canvas, mFirstColor, mStartX, mStartY, mStartX,
                    mClipStartY, getWidth(), mClipEndY, false);

            drawText(canvas, mBackgroundColor, mStartX, mStartY, mStartX,
                    mClipEndY, getWidth(), mStartY + value2px(1, VALUE.DP), true);
        } else {
            drawText(canvas, mFirstColor, mStartX, mStartY, mStartX,
                    mClipEndY, getWidth(), mStartY + value2px(1, VALUE.DP), false);

            drawText(canvas, mBackgroundColor, mStartX, mStartY, mStartX,
                    mClipStartY, getWidth(), mClipEndY, true);
        }
        canvas.drawText(CHARACTER_PERCENT, mStartX + mTextWidth, mStartY, mPercentPaint);
    }

    private void initPaint() {
        // init main paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFakeBoldText(true);
        mPaint.setTypeface(mTypeface);
        mPaint.setTextSize(value2px(mTextSize, VALUE.DP));
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);

        // init percent character paint
        mPercentPaint = new Paint();
        mPercentPaint.setAntiAlias(true);
        mPercentPaint.setStyle(Paint.Style.FILL);
        mPercentPaint.setTypeface(mTypeface);
        mPercentPaint.setTextSize(value2px(mTextSize, VALUE.SP) * 4 / 25);
        mPercentPaint.setColor(mSecondColor);

        if (mText == null) {
            mText = "0";
        }
    }

    private void init(int w, int h) {
        if (mPercent == 0f) {
            mPercent = (float)(Integer.parseInt(mText)) / 100;
        }

        mFm = mPaint.getFontMetricsInt();
        mTextWidth = mPaint.measureText(mText);
        mTextHeight = mFm.descent - mFm.ascent;
        mStartX = w / 2 - mTextWidth / 2 - mPercentPaint.measureText(CHARACTER_PERCENT) / 2;
        mStartY = mTextHeight / 2 + h / 2 - mFm.descent;
        switch (mGravity) {
            case GRAVITY_BOTTOM:
                mStartY = h - value2px(16f, VALUE.DP);
                break;
            case GRAVITY_CENTER:
                break;
            case GRAVITY_TOP:
                mStartY = Math.abs(mFm.ascent);
                break;
        }

        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
        mClipStartY = mStartY - mBound.height();
        float height = mBound.height();
        if (mDirection == TOP) {
            mClipEndY = mClipStartY + height * mPercent;
            mGradient = new LinearGradient(mStartX, mClipStartY, mStartX, mClipEndY,
                    mFirstColor, mSecondColor, Shader.TileMode.CLAMP);
        } else {
            mClipEndY = mStartY - height * mPercent
                    + (mPercent == 0.01f ? 0 : value2px(1, VALUE.DP));
            mGradient = new LinearGradient(mStartX, mClipEndY, mStartX,
                    mStartY + value2px(1, VALUE.DP),
                    mFirstColor, mSecondColor, Shader.TileMode.CLAMP);
        }
    }

    private void drawText(Canvas canvas, int color,  float x, float y,
                          float left, float top, float right, float bottom, boolean isBg) {
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        if (isBg) {
            mPaint.setShader(null);
            mPaint.setColor(color);
        } else {
            mPaint.setShader(mGradient);
        }
        canvas.clipRect(left, top, right, bottom);
        canvas.drawText(mText, x, y, mPaint);
        canvas.restore();
    }

    private int measureSize(M m, int measureSpec) {
        int measureSize = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                measureSize = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                float value;
                if (m == M.HEIGHT) {
                    Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                    value = Math.abs((fontMetrics.bottom - fontMetrics.top));
                    measureSize = (int) value + getPaddingTop() + getPaddingBottom();
                } else {
                    value = mPaint.measureText(mText);
                    measureSize = (int) (value + getPaddingLeft() + getPaddingRight()
                            + mPercentPaint.measureText(CHARACTER_PERCENT));
                }
                break;
        }
        return measureSize;
    }

    private float value2px(float value, VALUE v) {
        return TypedValue.applyDimension(v == VALUE.DP ? TypedValue.COMPLEX_UNIT_DIP : TypedValue.COMPLEX_UNIT_SP,
                value, mContext.getResources().getDisplayMetrics());
    }

    private Typeface obtainTypeface(int typeface) {
        Typeface t;
        switch (typeface) {
            case NORMAL:
                t = Typeface.DEFAULT;
                break;
            case SANS:
                t = Typeface.SANS_SERIF;
                break;
            case SERIF:
                t = Typeface.SERIF;
                break;
            case MONOSPACE:
                t = Typeface.MONOSPACE;
                break;
            default:
                t = Typeface.DEFAULT;
                break;
        }
        return t;
    }

    @MainThread
    public void setPercent(float percent) {
        mPercent = percent;
        mText = (int) (percent * 100) + "";
        if (mText.length() >= 3) {
            mTextSize = 86;
            mPaint.setTextSize(value2px(mTextSize, VALUE.DP));
        }
        init(getMeasuredWidth(), getMeasuredHeight());
        invalidate();
    }

    @MainThread
    public void setFirstColor(@ColorInt int firstColor) {
        this.mFirstColor = firstColor;
        invalidate();
    }

    @MainThread
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        invalidate();
    }

    @MainThread
    public void setSecondColor(@ColorInt int secondColor) {
        this.mSecondColor = secondColor;
        mPercentPaint.setColor(secondColor);
        invalidate();
    }

    /****
     * Sets the direction of the progress
     * @param direction 0 as bottom, 1 as top
     */
    @MainThread
    public void setDirection(int direction) {
        this.mDirection = direction;
        requestLayout();
        invalidate();
    }

    /***
     * Sets the text size, sp as unit
     * @param textSize
     */
    @MainThread
    public void setTextSize(int textSize) {
        mPaint.setTextSize(textSize);
        requestLayout();
        invalidate();
    }

    /**
     * Sets the typeface and style in which the text should be displayed.
     * Note that not all Typeface families actually have bold and italic
     * variants
     */
    @MainThread
    public void setTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    /****
     * Sets the typeface and style in which the text should be displayed.
     * Note that not all Typeface families actually have bold and italic
     * variants
     * @param typeface NORMAL 0,SAN 1,SERIF 2,MONOSPACE 3
     */
    public void setTypeface(int typeface) {
        mPaint.setTypeface(obtainTypeface(typeface));
        mPercentPaint.setTypeface(obtainTypeface(typeface));
        requestLayout();
        invalidate();
    }
}