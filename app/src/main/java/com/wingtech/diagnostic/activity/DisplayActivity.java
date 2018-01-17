package com.wingtech.diagnostic.activity;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.asusodm.atd.smmitest.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class DisplayActivity extends TestingActivity {

    private LinearLayout mLayout = null;
    private int mtick = 0;
    private String mContentDialog;
    @Override
    protected int getLayoutResId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_display;
    }

    @Override
    protected void initViews() {
        mLayout = (LinearLayout) findViewById(R.id.display);
    }

    @Override
    protected void initToolbar() {
        mRequestCode = DISPLAY_REQUEST_CODE;
    }

    @Override
    protected void onWork() {
        mLayout.setBackgroundColor(RED);
        mContentDialog = getIntent().getStringExtra("title_dialog");
    }

    private void changeColor(int curColor)
    {
        switch(curColor)
        {
            case 0:
                mLayout.setBackgroundColor(RED);
                break;
            case 1:
                mLayout.setBackgroundColor(GREEN);
                break;
            case 2:
                mLayout.setBackgroundColor(BLUE);
                break;
            case 3:
                mLayout.setBackgroundColor(BLACK);
                break;
            case 4:
                mLayout.setBackgroundColor(WHITE);
                break;
            case 5:
                mLayout.setBackgroundColor(GRAY);
                break;
            //case 6:
             //   mLayout.setBackgroundResource(R.drawable.lcd_tiaowen);
             //   break;
            default:
                mLayout.setBackgroundColor(RED);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(mtick <= 5) {
                    mtick++;
                    changeColor(mtick);
                }
                if(mtick >= 6) {
                    showTheDialog();
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void showTheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        if (mContentDialog != null){
            String sFormat = getResources().getString(R.string.dialog_context);
            String s = String.format(sFormat, mContentDialog);
            mContent.setText(s);
        }
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = true;
                sendResult();
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mResult = false;
                sendResult();
            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
    }
}
