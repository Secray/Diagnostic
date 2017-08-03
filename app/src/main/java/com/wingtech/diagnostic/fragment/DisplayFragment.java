package com.wingtech.diagnostic.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.activity.BaseActivity;
import com.wingtech.diagnostic.activity.SingleTestActivity;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;
import static com.wingtech.diagnostic.util.Constants.DISPLAY_REQUEST_CODE;

/**
 * Created by gaoweili on 17-7-28.
 */

public class DisplayFragment extends TestFragment implements View.OnTouchListener{

    private LinearLayout mLayout = null;
    private int mtick = 0;
    private String mContentDialog;
    @Override
    protected int getLayoutResId() {
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_display;
    }

    @Override
    protected void initViewEvents(View view) {
        mLayout = (LinearLayout) view.findViewById(R.id.display);
        myWork();
    }

    private void myWork() {
        mLayout.setBackgroundColor(RED);
        mContentDialog = mActivity.getIntent().getStringExtra("title_dialog");
    }

    private void changeColor(int curColor)
    {
        switch(curColor)
        {
            case 0:
                mLayout.setBackgroundColor(RED);
                break;
            case 1:
                mLayout.setBackgroundColor(BLACK);
                break;
            case 2:
                mLayout.setBackgroundColor(WHITE);
                break;
            case 3:
                mLayout.setBackgroundColor(YELLOW);
                break;
            case 4:
                mLayout.setBackgroundColor(BLUE);
                break;
            case 5:
                mLayout.setBackgroundColor(GREEN);
                break;
            case 6:
                mLayout.setBackgroundResource(R.mipmap.lcd_tiaowen);
                break;
            default:
                mLayout.setBackgroundColor(RED);
                break;
        }
    }

    public void showTheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
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
                mCallback.onChange(true);

            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCallback.onChange(false);

            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(mtick <= 6) {
                    mtick++;
                    changeColor(mtick);
                }
                if(mtick >= 7) {
                    showTheDialog();
                }

                break;
            default:
                break;
        }
        return v.onTouchEvent(event);
    }
}
