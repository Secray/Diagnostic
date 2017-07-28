package com.wingtech.diagnostic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingtech.diagnostic.R;
import com.wingtech.diagnostic.bean.SingleTestCase;
import com.wingtech.diagnostic.listener.OnItemClickListener;

import java.util.ArrayList;


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
        ImageView mIcon;
        TextView mTitle;
        ImageView mIndicator;
        public SingleTestViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.test_icon);
            mTitle = (TextView) itemView.findViewById(R.id.test_title);
            mIndicator = (ImageView) itemView.findViewById(R.id.test_indicator);
            itemView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
