package com.wingtech.diagnostic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wingtech.diagnostic.listener.OnPointsChangedListener;
import com.wingtech.diagnostic.util.Log;

/**
 * @author xiekui
 * @date 2017-7-31
 */

public class MultiTouchView extends View {
    public static final int[] COLORS = {Color.YELLOW, Color.GREEN, Color.RED, Color.WHITE, Color.CYAN};
    private static final int DEFAULT_CIRCLE_SIZE = 60;
    private Paint mPaint;
    private ArrayMap<Integer, Point> mPoints = new ArrayMap<>();
    private OnPointsChangedListener mListener;

    public MultiTouchView(Context context) {
        this(context, null);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mPoints.size(); i++) {
            if (i < COLORS.length) {
                mPaint.setColor(COLORS[i]);
            }
            canvas.drawCircle(mPoints.get(mPoints.keyAt(i)).x, mPoints.get(mPoints.keyAt(i)).y, DEFAULT_CIRCLE_SIZE, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_DOWN:
                int count = event.getPointerCount();
                for (int i = 0; i < count; i++) {
                    Point p = new Point();
                    p.x = event.getX(i);
                    p.y = event.getY(i);
                    mPoints.put(event.getPointerId(i), p);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int index = (event.getAction() &
                        MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                mPoints.remove(event.getPointerId(index));
                break;
        }
        invalidate();
        mListener.onChange(mPoints);
        return true;
    }

    public void setOnPointsChangedListener(OnPointsChangedListener listener) {
        this.mListener = listener;
    }

    public class Point {
        public float x;
        public float y;
    }
}
