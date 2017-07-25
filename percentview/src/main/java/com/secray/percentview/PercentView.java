package com.secray.percentview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.IOException;

/**
 * @author secray
 * @date 2017-07-03
 */

public class PercentView extends View {
    private static final int DEFAULT_TEXT_SIZE = 100;
    private static final int DEFAULT_TITLE_SIZE = 14;
    // Default first color
    private static final int FIRST_COLOR = Color.parseColor("#0092cf");
    // Default second color
    private static final int SECOND_COLOR = Color.parseColor("#00f6ff");
    private static final int TITLE_COLOR = Color.parseColor("#b3b6b9");
    // Direction top as default
    public static final int TOP = 1, BOTTOM = 0;
    // Typeface Normal as default
    public static final int NORMAL = 0, SANS = 1, SERIF = 2, MONOSPACE = 3;

    private Paint mPaint;
    private Paint mPercentPaint;
    private Paint mTitlePaint;
    private Rect mBound;
    private Context mContext;

    private String mText;
    private String mTitle;
    private int mFirstColor;
    private int mSecondColor;
    private int mTitleColor;
    private int mDirection;
    private int mTextSize;
    private int mTitleSize;
    private Typeface mTypeface;

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
        mTitleColor = a.getColor(R.styleable.PercentView_titleColor, TITLE_COLOR);
        mDirection = a.getInt(R.styleable.PercentView_direction, TOP);
        mTextSize = a.getDimensionPixelSize(R.styleable.PercentView_textSize, DEFAULT_TEXT_SIZE);
        mTitleSize = a.getDimensionPixelSize(R.styleable.PercentView_titleSize, DEFAULT_TITLE_SIZE);
        mText = a.getString(R.styleable.PercentView_text);
        mTitle = a.getString(R.styleable.PercentView_title);
        int typeface = a.getInt(R.styleable.PercentView_typeface, NORMAL);

        a.recycle();

        mContext = context;
        mTypeface = obtainTypeface(typeface);
        mBound = new Rect();

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mText.length() >= 3) {
            mTextSize = 86;
        }
        if (mPercent == 0f) {
            mPercent = (float)(Integer.parseInt(mText)) / 100;
        }


        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        float textWidth = mPaint.measureText(mText);
        float textHeight = fm.descent - fm.ascent;

        float startX = getWidth() / 2 - textWidth / 2;
        float startY = textHeight / 2 + getHeight() / 2 - fm.descent;


        float clipStartY = getHeight() / 2 - mBound.height() / 2 - value2px(2.5f, VALUE.DP);
        float clipEndY;
        float height = startY - clipStartY + value2px(1, VALUE.DP);

        if (mDirection == TOP) {
            clipEndY = clipStartY + height * mPercent;
            drawText(canvas, mFirstColor, startX, startY, startX,
                    clipStartY, getWidth(), clipEndY);

            drawText(canvas, mSecondColor, startX, startY, startX,
                    clipEndY, getWidth(), startY + value2px(1, VALUE.DP));
        } else if (mDirection == BOTTOM) {
            clipEndY = startY - height * mPercent;
            drawText(canvas, mFirstColor, startX, startY, startX,
                    clipEndY, getWidth(), startY + value2px(1, VALUE.DP));

            drawText(canvas, mSecondColor, startX, startY, startX,
                    clipStartY, getWidth(), clipEndY);
        }
        canvas.drawText("%", startX + textWidth, startY, mPercentPaint);

        if (mTitle != null && mTitle.length() != 0) {
            drawTitle(canvas, startY);
        }
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

        // init title paint
        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf"));
        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextSize(value2px(mTitleSize, VALUE.SP));

        if (mText == null) {
            mText = "0";
        }
    }

    private void drawText(Canvas canvas, int color,  float x, float y,
                          float left, float top, float right, float bottom) {
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        mPaint.setColor(color);
        canvas.clipRect(left, top, right, bottom);
        canvas.drawText(mText, x, y, mPaint);
        canvas.restore();
    }

    private void drawTitle(Canvas canvas, float y) {
        Paint.FontMetricsInt fm = mTitlePaint.getFontMetricsInt();
        float titleWidth = mTitlePaint.measureText(mTitle);
        float titleHeight = fm.descent - fm.ascent;

        float x = getWidth() / 2 - titleWidth / 2;
        canvas.drawText(mTitle, x, titleHeight + y + value2px(8, VALUE.DP), mTitlePaint);
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
                    measureSize = (int) value + getPaddingLeft() + getPaddingRight();
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
        invalidate();
    }

    @MainThread
    public void setFirstColor(@ColorInt int firstColor) {
        this.mFirstColor = firstColor;
        invalidate();
    }

    @MainThread
    public void setSecondColor(@ColorInt int secondColor) {
        this.mSecondColor = secondColor;
        mPercentPaint.setColor(secondColor);
        invalidate();
    }

    @MainThread
    public void setTitleColor(@ColorInt int titleColor) {
        mTitlePaint.setColor(titleColor);
        invalidate();
    }


    /****
     * Sets the direction of the progress
     * @param direction 0 as bottom, 1 as top
     */
    @MainThread
    public void setDirection(int direction) {
        this.mDirection = direction;
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
        requestLayout();
        invalidate();
    }
}
