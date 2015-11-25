package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectCombinationViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectDividerViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectFundManagerViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectMoreViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectRelatedViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectStockFundViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectTitleViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.SelectUserViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.ViewBean;

import java.util.List;

/**
 * Created by xuetong on 2015/11/16.
 * modified by zcm on 2015/11/18
 */
public class SelectGeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray<ViewBean> viewDatas = new SparseArray<ViewBean>(7);
    private List<ViewBean> mItems;
    private ChangeFollowView changeFollowView;

    public SelectGeneralAdapter(List<ViewBean> items,Context context) {
        mItems = items;
        changeFollowView = new ChangeFollowView(context);
        changeFollowView.setmChangeListener(new ChangeFollowView.IChangeSuccessListener() {
            @Override
            public void onChange(SelectStockBean stockBean) {
                BusProvider.getInstance().post(stockBean);
            }
        });
        new SelectTitleViewBean(viewDatas);
        new SelectStockFundViewBean(viewDatas,changeFollowView);
        new SelectMoreViewBean(viewDatas);
        new SelectFundManagerViewBean(viewDatas);
        new SelectUserViewBean(viewDatas);
        new SelectRelatedViewBean(viewDatas);
        new SelectCombinationViewBean(viewDatas);
        new SelectDividerViewBean(viewDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        return viewDatas.get(viewType).onCreateViewHolder(container,null);
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
