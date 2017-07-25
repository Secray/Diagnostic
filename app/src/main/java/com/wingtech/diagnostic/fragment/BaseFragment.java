package com.wingtech.diagnostic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingtech.diagnostic.activity.BaseActivity;

/**
 * @author xiekui
 * @date 2017-7-20
 */

public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        initViewEvents(view);
        return view;
    }

    protected abstract @LayoutRes int getLayoutResId();
    protected abstract void initViewEvents(View view);

    public BaseActivity getHoldActivity() {
        return mActivity;
    }
}
