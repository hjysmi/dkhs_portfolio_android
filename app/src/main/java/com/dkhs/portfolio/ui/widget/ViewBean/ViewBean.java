package com.dkhs.portfolio.ui.widget.ViewBean;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zjz on 2015/7/3.
 */
public  abstract class ViewBean {
    ViewBean() {}

    public ViewBean(SparseArray<ViewBean> viewDatas) {
        viewDatas.put(getViewType(), this);
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool);
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {}
    public abstract int getViewType();

    protected static View inflate(ViewGroup container, int layoutId) {
        return LayoutInflater.from(container.getContext()).inflate(layoutId, container, false);
    }
}
