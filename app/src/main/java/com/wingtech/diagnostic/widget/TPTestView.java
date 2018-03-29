package com.wingtech.diagnostic.widget;

/**
 * @author gaoweili
 * @date 2017-8-5
 */

import com.wingtech.diagnostic.activity.TouchTestActivity;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.AttributeSet;

public class TPTestView extends View {
    enum RECTYPE {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        //V_LINE1,
        //--V_LINE2,
        V_LINE3,
        //--V_LINE4,
        //V_LINE5,
        //H_LINE1,
        //H_LINE2,
        H_LINE3,
        //-H_LINE4,
        H_LINE5,
        NULL,
    }

    ;

    private final String TAG = "TPTEST";
    private Path m_path;
    private Canvas m_Canvas;
    private Paint m_Paint;
    private Paint m_CentrePaint;
    private Paint m_PathPaint;
    private Context m_Context;
    private RECTYPE m_RecType;
    private int m_LineIndex = 0;
    private boolean m_bCheckLineState = true;
    private boolean m_finish = false;
    private boolean m_result = false;
    private boolean m_eventup = true;
    private boolean m_Vertical = true;
    private boolean m_MutilPointTouch = false;
    int iRecLeft = 0;
    int iRecRight = 0;
    int iRecTop = 0;
    int iRecBottom = 0;
    int addtionalY = 50;
    float startX = 0.0f;
    float startY = 0.0f;
    float stopX = 0.0f;
    float stopY = 0.0f;
    int iRetryTimes = 100;
    int window_width = 0;
    int window_height = 0;
    int state_height = 0;//状态栏高度
    int width_average = 0;
    int width_averageToAverage = 0;//垂直方形的宽度的平均值
    int height_average = 0;
    Rect m_frame;
    Toast m_toast;
    private OnCompleteListenerTP mCompleteListener;
    public Hashtable m_HT_Pointers = new Hashtable();
    private int flag1, flag2, flag3, flag4, flag5, flag6;


    public TPTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        m_Context = context;

        WindowManager manager = ((Activity) m_Context).getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        if (android.os.Build.MODEL.contains("ASUS_X017D"))
            window_height = 2160;
        Log.i(TAG, "W=" + window_width + ";H=" + window_height);
        Log.i(TAG, "W=" + window_width + ";H=" + window_height);
/*      window_width = 320;
      window_height = 480;//因有虚拟键盘，占了一部分高度，暂时定480调试
*/
        height_average = window_height / 14;
        width_average = (window_width - height_average * 2) / 5;//左右两边的宽度与垂直方向的平均值一样，然后算剩下的水平方向的5条线的平均值
        width_averageToAverage = width_average / 2;

        //Log.i("JAY", "window_width=" + window_width + " window_height=" + window_height + " state_height=" + state_height);

        m_RecType = RECTYPE.LEFT;
        m_Vertical = true;
        m_LineIndex = 0;

        m_Paint = new Paint();
        m_Paint.setAntiAlias(true);
        m_Paint.setARGB(255, 255, 255, 255);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeWidth(2);


        m_CentrePaint = new Paint();
        m_CentrePaint.setStyle(Paint.Style.STROKE);
        m_CentrePaint.setStrokeWidth(5);
        m_CentrePaint.setAntiAlias(true);
        m_CentrePaint.setColor(Color.WHITE);


        //
        m_PathPaint = new Paint();
        m_PathPaint.setAntiAlias(false);
        m_PathPaint.setARGB(255, 0, 96, 255);
        m_PathPaint.setStrokeWidth(10);

    }

    public TPTestView(Context context) {
        super(context);
        setFocusable(true);
        m_Context = context;

        WindowManager manager = ((Activity) m_Context).getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        if (android.os.Build.MODEL.contains("ASUS_X017D"))
            window_height = 2160;
        Log.i(TAG, "W=" + window_width + ";H=" + window_height);
          /*	      window_width = 320;
          window_height = 480;//因有虚拟键盘，占了一部分高度，暂时定480调试
*/
        height_average = window_height / 14;
        width_average = (window_width - height_average * 2) / 5;//左右两边的宽度与垂直方向的平均值一样，然后算剩下的水平方向的5条线的平均值
        width_averageToAverage = width_average / 2;

        m_RecType = RECTYPE.LEFT;
        m_Vertical = true;
        m_LineIndex = 0;

        m_Paint = new Paint();
        m_Paint.setAntiAlias(true);
        m_Paint.setARGB(255, 255, 255, 255);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeWidth(2);


        m_CentrePaint = new Paint();
        m_CentrePaint.setStyle(Paint.Style.STROKE);
        m_CentrePaint.setStrokeWidth(5);
        m_CentrePaint.setAntiAlias(true);
        m_CentrePaint.setColor(Color.WHITE);


        //
        m_PathPaint = new Paint();
        m_PathPaint.setAntiAlias(false);
        m_PathPaint.setARGB(255, 0, 96, 255);
        m_PathPaint.setStrokeWidth(10);

    }


    public void setOnCompleteListenerTP(OnCompleteListenerTP mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    public interface OnCompleteListenerTP {

        public void onComplete(boolean bResult);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (m_HT_Pointers) {
            //不支持多点触摸测试

            int action = event.getAction();
            int NP = m_HT_Pointers.size();
            String state = "";
            Log.i(TAG, "mPointers=" + NP);


            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    TouchTestActivity.mFail.setVisibility(View.GONE);
                    m_eventup = false;
                    //add by xieqin for probabilistic error finish test start
                    m_finish = false;
                    m_result = false;
                    //add by xieqin for probabilistic error finish test end
                    PointerState newPS = new PointerState();
                    Log.i(TAG, "ADD key=" + String.valueOf(event.getActionIndex()));
                    m_HT_Pointers.put(String.valueOf(event.getActionIndex()), newPS);

                    Enumeration enumPoniter = m_HT_Pointers.keys();
                    while (enumPoniter.hasMoreElements()) {
                        String key = (String) enumPoniter.nextElement();
                        final PointerState ps = (PointerState) m_HT_Pointers.get(key);
                        ps.mXs.clear();
                        ps.mYs.clear();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    TouchTestActivity.mFail.setVisibility(View.VISIBLE);
                    m_eventup = true;
                    m_bCheckLineState = checkLine();
                    if (m_bCheckLineState) {
                        iRetryTimes = 100;
                        if (m_RecType != RECTYPE.H_LINE5) {
                            //add by xieqin for probabilistic error finish test start
                            m_finish = false;
                            m_result = false;
                            //add by xieqin for probabilistic error finish test end
                            m_LineIndex++;
                            m_RecType = RECTYPE.values()[m_LineIndex];
                            switch (m_RecType) {
                                case LEFT:
                                case RIGHT:
                                    //case V_LINE1:
                                    //   case V_LINE2:
                                case V_LINE3:
                                    //--  case V_LINE4:
                                    //case V_LINE5:
                                    m_Vertical = true;
                                    break;
                                case TOP:
                                case BOTTOM:
                                    //case H_LINE1:
                                    //case H_LINE2:
                                case H_LINE3:
                                    //	        case H_LINE4:
                                    m_Vertical = false;
                                    break;
                            }
                        } else {
                            m_finish = true;
                            m_result = true;
                            m_Vertical = true;
                            state = "TP Test Success";
                        }
                    } else {
                        //add by xieqin for probabilistic error finish test start
                        m_finish = false;
                        m_result = false;
                        //add by xieqin for probabilistic error finish test end
                        iRetryTimes--;
                        if (iRetryTimes == 0) {
                            //任何一步测试尝试3次测试失败的话回到原点重测
                            m_finish = true;
                            m_result = false;
                            state = "TP Test Failed,Try To Do It From The Step 1";
                        }
                    }
                    m_path.reset();
                    //drawRec(m_RecType,m_bCheckLineState);
                    break;
            }

            if (m_finish) {
                m_Vertical = true;
                m_RecType = RECTYPE.LEFT;
                m_bCheckLineState = true;
                //jay lee quickly to out of tp test 20141117
			/*
	        	m_finish = false;
	        	m_toast=Toast.makeText(m_Context.getApplicationContext(), state, Toast.LENGTH_SHORT);
	        	m_toast.show();
			*/
                mCompleteListener.onComplete(m_result);
            }

            final int NI = event.getPointerCount();
            Log.i(TAG, "NI=" + NI);
            for (int i = 0; i < NI; i++) {
                final int id = event.getActionIndex();//event.getPointerId(i);//同时两个触摸点后，当释放第一个触摸点先的话，可能会导致后续画点不成功。
                Log.i(TAG, "id=" + id);
                if (!m_HT_Pointers.containsKey(String.valueOf(id))) {
                    continue;
                }
                Log.i(TAG, "111 id=" + id);
                final PointerState ps = (PointerState) m_HT_Pointers.get(String.valueOf(id));
                final int N = event.getHistorySize();
                for (int j = 0; j < N; j++) {
                    ps.mXs.add(event.getHistoricalX(i, j));
                    ps.mYs.add(event.getHistoricalY(i, j));
                }
                ps.mXs.add(event.getX(i));
                ps.mYs.add(event.getY(i));
            }

            postInvalidate();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        synchronized (m_HT_Pointers) {
            final int NP = m_HT_Pointers.size();
            m_Canvas = canvas;

            if (m_finish) {
                return;
            }
            drawRec(m_RecType, m_bCheckLineState);

            if (!m_eventup) {
                Enumeration enumPoniter = m_HT_Pointers.keys();
                while (enumPoniter.hasMoreElements()) {
                    String key = (String) enumPoniter.nextElement();
                    final PointerState ps = (PointerState) m_HT_Pointers.get(key);
                    final int N = ps.mXs.size();
                    float lastX = 0, lastY = 0;
                    boolean haveLast = false;
                    for (int i = 0; i < N; i++) {
                        float x = ps.mXs.get(i);
                        float y = ps.mYs.get(i);
                        if (Float.isNaN(x)) {
                            haveLast = false;
                            continue;
                        }
                        if (haveLast) {

                            canvas.drawLine(lastX, lastY, x, y, m_PathPaint);
                            canvas.drawPoint(lastX, lastY, m_Paint);
                        }
                        lastX = x;
                        lastY = y;
                        haveLast = true;
                    }
                }
            }
        }
    }

    private boolean checkLine() {
        int i = 0;
        int iPointCount = 0;
        float iFirstPointX = 0.0f;
        float iFirstPointY = 0.0f;
        float iLastPointX = 0.0f;
        float iLastPointY = 0.0f;
        boolean iCheckOk = true;

        //不支持多点触控的划线测试
        Log.i(TAG, "m_HT_Pointers.size=" + m_HT_Pointers.size());
        if (m_HT_Pointers.size() > 1) {
            return false;
        }

        Enumeration enumPoniter = m_HT_Pointers.keys();
        while (enumPoniter.hasMoreElements()) {
            String key = (String) enumPoniter.nextElement();
            Log.i(TAG, "key=" + key);
            final PointerState ps = (PointerState) m_HT_Pointers.get(key);

            iPointCount = ps.mXs.size();
            Log.i(TAG, "iPointCount=" + iPointCount);

            if (iPointCount == 0 || iPointCount != ps.mYs.size()) {
                Log.i(TAG, "iPointCount=" + iPointCount + "ps.mYs.size()=" + ps.mYs.size());
                return false;
            }

            iFirstPointX = ps.mXs.get(0);
            iFirstPointY = ps.mYs.get(0);
            iLastPointX = ps.mXs.get(iPointCount - 1);
            iLastPointY = ps.mYs.get(iPointCount - 1);


//       	Log.i(TAG,"m_RecType " + m_RecType.toString());

            Log.i(TAG, "FX=" + iFirstPointX + ";" + "LX=" + iLastPointX + ";" + "FY=" + iFirstPointY + ";" + "LY=" + iLastPointY);
            Log.i(TAG, "RL=" + iRecLeft + ";" + "RR=" + iRecRight + ";" + "RT=" + iRecTop + ";" + "RB=" + iRecBottom);
            //判断线是否够长 判断第一个与最后一个点分别与测试区域的边界的距离是否达到合适的位置
            if (m_Vertical) {
                Log.i(TAG, "Vertical true");
                if ((iFirstPointY <= (iRecTop + 50) && iLastPointY >= (iRecBottom - 100)) || (iFirstPointY >= (iRecBottom - 100) && iLastPointY <= (iRecTop + 50))) {
                    Log.i(TAG, "line is ok 1");
                } else {
                    Log.i(TAG, "line is too short 1");
                    return false;
                }
            } else {

                Log.i(TAG, "Vertical false");
                if ((iFirstPointX <= (iRecLeft + 50) && iLastPointX >= (iRecRight - 50)) || (iFirstPointX >= (iRecRight - 50) && iLastPointX <= (iRecLeft + 50))) {
                    Log.i(TAG, "line is ok");
                } else {
                    Log.i(TAG, "line is too short");
                    return false;
                }
            }

            //判断所有点都在区域范围内
            for (i = 0; i < iPointCount; i++) {
                Log.i(TAG, i + ":mXs=" + ps.mXs.get(i));
                Log.i(TAG, i + ":mYs=" + ps.mYs.get(i));
                Log.i(TAG, i + ":iRecLeft=" + iRecLeft + "|iRecRight=" + iRecRight);
                Log.i(TAG, i + ":iRecTop=" + iRecTop + "|iRecBottom=" + iRecBottom);
                if (ps.mXs.get(i) >= iRecLeft && ps.mXs.get(i) <= iRecRight &&
                        ps.mYs.get(i) >= iRecTop && ps.mYs.get(i) <= iRecBottom) {
                    Log.i(TAG, "line is true");
                } else if (m_Vertical && ps.mXs.get(i) >= iRecLeft && ps.mXs.get(i) <= iRecRight && ps.mYs.get(i) >= iRecTop && ps.mYs.get(i) <= iRecBottom + 100) {
                    Log.i(TAG, "line is true");
                } else {
                    Log.i(TAG, "line is false");
                    return false;
                }

            }
            break;//只有一个
        }
        return iCheckOk;
    }


    private void drawRec(RECTYPE rectype, boolean bState) {
        Paint rectPaint = new Paint();
        m_path = new Path();

        switch (rectype) {
            case LEFT:
           /*iRecLeft = 0;
           iRecTop = 0;
           iRecRight = height_average;
           iRecBottom = window_height + addtionalY;
           startX = height_average/2;
           startY = 0;
           stopX = startX;
           stopY = window_height;*/
                if (bState) {
                    flag1 = 2;
                } else {
                    flag1 = 1;
                }
                drawRecBg(flag1, flag2, flag3, flag4, flag5, flag6);
                break;
            case TOP:
           /*iRecLeft = 0;
           iRecTop = 0;
           iRecRight = window_width;
           iRecBottom = height_average;
           startX = 0;
           startY = height_average/2;
           stopX = window_width;
           stopY = startY;*/
                flag1 = 3;
                if (bState) {
                    flag2 = 2;
                } else {
                    flag2 = 1;
                }
                drawRecBg2(flag1, flag2, flag3, flag4, flag5, flag6);
                break;
            case RIGHT:
           /*iRecLeft = window_width - height_average;
           iRecTop = 0;
           iRecRight = window_width;
           iRecBottom = window_height + addtionalY;
           startX = iRecLeft + height_average/2;
           startY = 0;
           stopX = startX;
           stopY = window_height;*/
                flag2 = 3;
                if (bState) {
                    flag3 = 2;
                } else {
                    flag3 = 1;
                }
                drawRecBg3(flag1, flag2, flag3, flag4, flag5, flag6);
                break;
            case BOTTOM:
       	/*
           iRecLeft = 0;
           iRecTop = window_height - height_average;
           iRecRight = window_width;
           iRecBottom = window_height;
           startX = 0;
           startY = iRecTop  + height_average/2;
           stopX = window_width;
           stopY = startY;*/
                flag3 = 3;
                if (bState) {
                    flag4 = 2;
                } else {
                    flag4 = 1;
                }
                drawRecBg4(flag1, flag2, flag3, flag4, flag5, flag6);
                break;

            case V_LINE3:
           /*iRecLeft = height_average + width_average*2;
           iRecTop = 0;
           iRecRight = height_average + width_average*3;
           iRecBottom = window_height + addtionalY;
           startX = height_average + width_average*2 + width_averageToAverage;
           startY = 0;
           stopX = startX;
           stopY = window_height;*/
                flag4 = 3;
                if (bState) {
                    flag5 = 2;
                } else {
                    flag5 = 1;
                }
                drawRecBg5(flag1, flag2, flag3, flag4, flag5, flag6);
                break;

            case H_LINE3:
           /*iRecLeft = 0;
           iRecTop = window_height / 2 - height_average / 2;
           iRecRight = window_width;
           iRecBottom = window_height / 2 + height_average / 2;
           startX = 0;
           startY = window_height / 2;
           stopX = window_width;
           stopY = startY;*/
                flag5 = 3;
                if (bState) {
                    flag6 = 2;
                } else {
                    flag6 = 1;
                }
                drawRecBg6(flag1, flag2, flag3, flag4, flag5, flag6);
                break;

            case H_LINE5:
                flag6 = 6;
                mCompleteListener.onComplete(true);
                break;
        }
       
/*
	   	m_Canvas.drawColor(Color.GRAY);
	   	if(bState)
	   	{
		    rectPaint.setColor(Color.GREEN);
	   	}
	   	else
	   	{
		    rectPaint.setColor(Color.RED);
	   	} 
	   	rectPaint.setAntiAlias(true);
	   	rectPaint.setStyle(Paint.Style.FILL);	    	
	   	m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);	    	
	   	m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
	   	m_Canvas.drawPath(m_path, m_Paint);*/
    }


    private void drawRecBg(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 2) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
    }

    private void drawRecBg2(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();

        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 2) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState2 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag2 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
    }

    private void drawRecBg3(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();


        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 2) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState2 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag2 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState3 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag3 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
    }

    private void drawRecBg4(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();

        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 2) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 2) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState2 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag2 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState3 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag3 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);


        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState4 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag4 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
    }

    private void drawRecBg5(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();


        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 2) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState2 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag2 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState3 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag3 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);


        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState4 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag4 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState5 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag5 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

    }

    private void drawRecBg6(int bState1, int bState2, int bState3, int bState4, int bState5, int bState6) {
        Paint rectPaint = new Paint();
        m_path = new Path();

        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = height_average;
        iRecBottom = window_height + addtionalY;
        startX = height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState1 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState1 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState1 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag1 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = 0;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = height_average;
        startX = 0;
        startY = height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState2 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState2 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState2 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag2 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = window_width - height_average;
        iRecTop = 0;
        iRecRight = window_width;
        iRecBottom = window_height + addtionalY;
        startX = iRecLeft + height_average / 2;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState3 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState3 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState3 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag3 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);


        iRecLeft = 0;
        iRecTop = window_height - height_average;
        iRecRight = window_width;
        iRecBottom = window_height;
        startX = 0;
        startY = iRecTop + height_average / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState4 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState4 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState4 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag4 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);
        iRecLeft = height_average + width_average * 2;
        iRecTop = 0;
        iRecRight = height_average + width_average * 3;
        iRecBottom = window_height + addtionalY;
        startX = height_average + width_average * 2 + width_averageToAverage;
        startY = 0;
        stopX = startX;
        stopY = window_height;
        rectPaint.setColor(Color.GRAY);
        if (bState5 == 3) {
            rectPaint.setColor(Color.GREEN);
        } else if (bState5 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState5 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag5 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

        iRecLeft = 0;
        iRecTop = window_height / 2 - height_average / 2;
        iRecRight = window_width;
        iRecBottom = window_height / 2 + height_average / 2;
        startX = 0;
        startY = window_height / 2;
        stopX = window_width;
        stopY = startY;
        rectPaint.setColor(Color.GRAY);
        if (bState6 == 3) {
            rectPaint.setColor(Color.GRAY);
        } else if (bState6 == 1) {
            rectPaint.setColor(Color.RED);
        }
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(new Rect(iRecLeft, iRecTop, iRecRight, iRecBottom), rectPaint);
        if (bState6 == 2) {
            m_Canvas.drawLine(startX, startY, stopX, stopY, m_CentrePaint);
            flag6 = 3;

        }
        m_Canvas.drawPath(m_path, m_Paint);

    }

    public static class PointerState {
        private final ArrayList<Float> mXs = new ArrayList<Float>();
        private final ArrayList<Float> mYs = new ArrayList<Float>();
    }

}
