package com.wingtech.diagnostic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingtech.diagnostic.listener.OnItemClickListener;
import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.bean.SingleTestCase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author xiekui
 * @date 2017-7-19
 */

public class SingleTestAdapter extends RecyclerView.Adapter<SingleTestAdapter.SingleTestViewHolder>{
    private Context mContext;
    private ArrayList<SingleTestCase> mSingleTestCases;
    private OnItemClickListener mOnItemClickListener;

    public SingleTestAdapter(Context context, ArrayList<SingleTestCase> data) {
        mContext = context;
        mSingleTestCases = data;
    }

    @Override
    public SingleTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleTestViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_single_test_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SingleTestViewHolder holder, int position) {
        SingleTestCase t = mSingleTestCases.get(position);
        holder.mIcon.setImageDrawable(t.getIcon());
        holder.mTitle.setText(t.getTitle());
        holder.mIndicator.setImageResource(t.isPassed() ? R.drawable.ic_pass : R.drawable.ic_fail);
        holder.mIndicator.setVisibility(t.isShowResult() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mSingleTestCases.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class SingleTestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.test_icon)
        ImageView mIcon;
        @BindView(R.id.test_title)
        TextView mTitle;
        @BindView(R.id.test_indicator)
        ImageView mIndicator;
        public SingleTestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root)
        void onClick(View view) {
            mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
