package com.wingtech.diagnostic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asus.atd.smmitest.R;
import com.wingtech.diagnostic.bean.TestCaseResult;

import java.util.ArrayList;

/**
 * @author xiekui
 * @date 2017-7-18
 */

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder>{
    private Context mContext;
    private ArrayList<TestCaseResult> mDatas;

    public TestResultAdapter(Context context, ArrayList<TestCaseResult> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public TestResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestResultViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_test_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TestResultViewHolder holder, int position) {
        TestCaseResult t = mDatas.get(position);
        holder.mIcon.setImageDrawable(t.getIcon());
        holder.mTitle.setText(t.getTitle());
        holder.mIndicator.setImageResource(t.isResult() ? R.drawable.ic_pass : R.drawable.ic_fail);
        holder.mFailed.setText(t.isResult() ?
                mContext.getString(R.string.list_item_result_failed, 0)
                : mContext.getString(R.string.list_item_result_failed, 1));

        holder.mPassed.setText(t.isResult() ?
                mContext.getString(R.string.list_item_result_passed, 1)
                : mContext.getString(R.string.list_item_result_passed, 0));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class TestResultViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mTitle;
        TextView mPassed;
        TextView mFailed;
        ImageView mIndicator;
        View mTextField;

        public TestResultViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.test_icon);
            mTitle = (TextView) itemView.findViewById(R.id.test_title);
            mPassed = (TextView) itemView.findViewById(R.id.test_passed);
            mFailed = (TextView) itemView.findViewById(R.id.test_failed);
            mIndicator = (ImageView) itemView.findViewById(R.id.test_indicator);
            mTextField = (View) itemView.findViewById(R.id.test_result_field);
            mTextField.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.VISIBLE);
        }
    }
}
