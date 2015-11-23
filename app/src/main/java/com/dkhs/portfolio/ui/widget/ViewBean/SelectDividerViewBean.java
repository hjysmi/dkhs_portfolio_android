package com.dkhs.portfolio.ui.widget.ViewBean;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectDividerViewBean extends ViewBean{
    private static final int TYPE = 7;

    public SelectDividerViewBean() {
    }

    public SelectDividerViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_divider));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ViewUitls.fullSpanView(itemHolder);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
