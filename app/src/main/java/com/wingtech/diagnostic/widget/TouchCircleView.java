package com.wingtech.diagnostic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.asusodm.atd.smmitest.R;
import com.wingtech.diagnostic.util.Utils;

/**
 * @author xiekui
 * @date 2017-7-24
 */

public class TouchCircleView extends View {
    private Paint mPaint;
    private int mColor;
    private float mRadius;
    public TouchCircleView(Context context) {
        this(context, null);
    }

    public TouchCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TouchCircleView);
        mColor = a.getColor(R.styleable.TouchCircleView_color, 0);
        mRadius = a.getFloat(R.styleable.TouchCircleView_radius, Utils.dp2px(context, 70));
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(Utils.dp2px(context, 8));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureSize(widthMeasureSpec);
        int h = measureSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);
    }

    private int measureSize(int measureSpec) {
        int measureSize = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                measureSize = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                measureSize = (int) (mRadius + mPaint.getStrokeWidth()) * 2;
                break;
        }
        return measureSize;
    }
}
