package com.wingtech.diagnostic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wingtech.diagnostic.App;
import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.util.SharedPreferencesUtils;


/**
 * @author xiekui
 * @date 2017-7-12
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initViews();
        handleIntent(getIntent());
        initToolbar();
        onWork();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.clear:
            case R.id.reset:
                showTheDialog();
                /*
                new AlertDialog.Builder(this).setTitle(getString(R.string.reset_dialog_title))
                        .setMessage(getString(R.string.reset_dialog_content))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {

                        }).create().show();

                            }
                        }).create().show();*/

                break;
            case R.id.exit:
                App.exit();
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.result:
                startActivity(new Intent(this, TestResultActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract void initViews();
    protected abstract void initToolbar();
    protected abstract void onWork();
    protected void handleIntent(Intent intent) {

    }

    protected void handleTestResult() {

    }

    public void showTheDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.content_test_dialog, null);//获取自定义布局
        builder.setView(layout);
        AlertDialog dlg = builder.create();
        TextView mContent = (TextView) layout.findViewById(R.id.dialog_context);
        TextView mTitle = (TextView) layout.findViewById(R.id.dialog_title);
        mTitle.setText(getString(R.string.reset_dialog_title));
        mContent.setText(getString(R.string.reset_dialog_content));
        Button pass = (Button) layout.findViewById(R.id.pass);
        pass.setText(R.string.headset_context_dialog_btn);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SharedPreferencesUtils.setNull(BaseActivity.this);
                handleTestResult();
                dlg.dismiss();
            }
        });
        Button fail = (Button) layout.findViewById(R.id.fail);
        fail.setText(R.string.menu_test_cancel);
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dlg.dismiss();
            }
        });

        dlg.show();
    }
}
