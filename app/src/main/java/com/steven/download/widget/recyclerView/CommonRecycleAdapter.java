package com.steven.download.widget.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private int mLayoutId;
    private List<T> mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private MultipleTypeSupport<T> mTypeSupport;

    /**
     * @param context  上下文
     * @param mDatas   数据源
     * @param layoutId 布局id
     */
    public CommonRecycleAdapter(Context context, List<T> mDatas, int layoutId) {
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * @param context     上下文
     * @param datas       数据源
     * @param typeSupport 不同类型的布局
     */
    public CommonRecycleAdapter(Context context, List<T> datas, MultipleTypeSupport<T> typeSupport) {
        this(context, datas, -1);
        this.mTypeSupport = typeSupport;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mTypeSupport != null) {
            //多布局
            mLayoutId = viewType;
        }
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, final int position) {
        convert(holder, mDatas.get(position), position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        //多布局
        if (mTypeSupport != null) {
            return mTypeSupport.getLayoutId(mDatas.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //点击事件
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public abstract void convert(CommonViewHolder holder, T t, int position);

}
