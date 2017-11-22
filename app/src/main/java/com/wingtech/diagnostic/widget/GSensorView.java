package com.wingtech.diagnostic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wingtech.diagnostic.util.Utils;

/**
 * @author xiekui
 * @date 2017-7-21
 */

public class GSensorView extends View {
    private static final int X_AXIS_COLOR = Color.parseColor("#ed6f5a");
    private static final int Y_AXIS_COLOR = Color.parseColor("#3f8cc2");
    private static final int Z_AXIS_COLOR = Color.parseColor("#e5af48");
    private static final int CIRCLE_RADIUS = 25;
    private static final int DEFAULT_TEXT_SIZE = 18;
    private Paint mPaint;
    private boolean mXPlusPass, mXMinusPass, mYPlusPass, mYMinusPass, mZPlusPass, mZMinusPass;

    public GSensorView(Context context) {
        this(context, null);
    }

    public GSensorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GSensorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4);
        mPaint.setTextSize(Utils.sp2px(context, DEFAULT_TEXT_SIZE));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(X_AXIS_COLOR);
        canvas.drawLine(0, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, mPaint);
        canvas.drawText("X-", CIRCLE_RADIUS * 2 + 18, getMeasuredHeight() / 2 - 18, mPaint);
        canvas.drawText("X+", getMeasuredWidth() - CIRCLE_RADIUS * 2 - 54, getMeasuredHeight() / 2 - 18, mPaint);
        mPaint.setColor(mXMinusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(CIRCLE_RADIUS, getMeasuredHeight() / 2, CIRCLE_RADIUS, mPaint);
        mPaint.setColor(mXPlusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(getMeasuredWidth() - CIRCLE_RADIUS, getMeasuredHeight() / 2, CIRCLE_RADIUS, mPaint);

        mPaint.setColor(Y_AXIS_COLOR);
        canvas.drawLine(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight(), mPaint);
        canvas.drawText("Y-", getMeasuredWidth() / 2 + 18, getMeasuredHeight() - CIRCLE_RADIUS * 2 - 18, mPaint);
        canvas.drawText("Y+", getMeasuredWidth() / 2 + 18, CIRCLE_RADIUS * 2 + 36, mPaint);
        mPaint.setColor(mYMinusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(getMeasuredWidth() / 2, CIRCLE_RADIUS, CIRCLE_RADIUS, mPaint);
        mPaint.setColor(mYPlusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() - CIRCLE_RADIUS, CIRCLE_RADIUS, mPaint);

        float startY = (float) Math.sin(Math.PI / 3) * getMeasuredHeight() / 2 + getMeasuredHeight() / 2;
        float startX = (float) getMeasuredWidth() / 2 - getMeasuredHeight() / 4;
        float endY = (float) (getMeasuredHeight() / 2 - Math.sin(Math.PI / 3) * getMeasuredHeight() / 2);
        float endX = (float) getMeasuredWidth() / 2 + getMeasuredHeight() / 4;
        mPaint.setColor(Z_AXIS_COLOR);
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        canvas.drawText("Z-", startX + CIRCLE_RADIUS + 18, startY - 18, mPaint);
        canvas.drawText("Z+", endX - 10, endY + 48 + CIRCLE_RADIUS, mPaint);
        mPaint.setColor(mZMinusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(startX, startY, CIRCLE_RADIUS, mPaint);
        mPaint.setColor(mZPlusPass ? Color.GREEN : Color.BLACK);
        canvas.drawCircle(endX, endY, CIRCLE_RADIUS, mPaint);
    }

    public void setXPlusPass(boolean xPlusPass) {
        this.mXPlusPass = xPlusPass;
        invalidate();
    }

    public void setXMinusPass(boolean xMinusPass) {
        this.mXMinusPass = xMinusPass;
        invalidate();
    }

    public void setYPlusPass(boolean yPlusPass) {
        this.mYPlusPass = yPlusPass;
        invalidate();
    }

    public void setYMinusPass(boolean yMinusPass) {
        this.mYMinusPass = yMinusPass;
        invalidate();
    }

    public void setZPlusPass(boolean zPlusPass) {
        this.mZPlusPass = zPlusPass;
        invalidate();
    }

    public void setZMinusPass(boolean zMinusPass) {
        this.mZMinusPass = zMinusPass;
        invalidate();
    }
}
