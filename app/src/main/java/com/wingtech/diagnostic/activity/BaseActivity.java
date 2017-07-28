package com.wingtech.diagnostic.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wingtech.diagnostic.R;


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
                finish();
                break;
            case R.id.clear:
            case R.id.reset:
                new AlertDialog.Builder(this).setTitle(getString(R.string.reset_dialog_title))
                        .setMessage(getString(R.string.reset_dialog_content))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
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
}
