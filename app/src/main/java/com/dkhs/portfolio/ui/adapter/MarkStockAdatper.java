package com.dkhs.portfolio.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.dkhs.portfolio.ui.widget.ViewBean.MarkGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkIndexViewPool;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkPlateGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkStockViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkTitleViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.ViewBean;

import java.util.List;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkStockAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray<ViewBean> viewDatas = new SparseArray<ViewBean>(3);
    private MarkIndexViewPool mViewPool;
    private List<ViewBean> mItems;

    public MarkStockAdatper(List<ViewBean> items, MarkIndexViewPool mViewPool) {
        this.mViewPool = mViewPool;
        mItems = items;


        new MarkTitleViewBean(viewDatas);
        new MarkGridViewBean(viewDatas);
        new MarkPlateGridViewBean(viewDatas);
        new MarkStockViewBean(viewDatas);
    }

    public void clear() {
        if (null != mItems) {
            mItems.clear();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        return viewDatas.get(viewType).onCreateViewHolder(container, mViewPool);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, int position) {
        mItems.get(position).onBindViewHolder(itemHolder);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
