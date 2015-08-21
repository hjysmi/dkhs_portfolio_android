package com.dkhs.adpter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.dkhs.adpter.itemhandler.LoadMoreHandler;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AutoLoadMoreRVAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/17.
 */
public class AutoLoadMoreRVAdapter extends AutoRVAdapter {

    private AutoRVAdapter mAutoRVAdapter;
    private boolean showLoadFootView = true;

    private AutoLoadMoreRVAdapter(Context context, List<?> data, AutoRVAdapter adapter) {
        super(context, data);
        mAutoRVAdapter = adapter;
        mAutoRVAdapter.initHandlers(mItemHandlerHashMap);

    }


    public static AutoLoadMoreRVAdapter warp(AutoRVAdapter adapter) {
        if (adapter instanceof AutoLoadMoreRVAdapter) {
            return (AutoLoadMoreRVAdapter) adapter;
        }

        return new AutoLoadMoreRVAdapter(adapter.mContext, adapter.mData, adapter);
    }

    @Override
    public int getViewType(int position) {

        int type;
        if (position == mData.size()) {
            type = -1;
        } else {
            type = mAutoRVAdapter.getViewType(position);
        }
        return type;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ItemHandler itemHandler;
        if (position == mData.size()) {
            itemHandler = getItemHandler(-1);
            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), null, position);
        } else {
            itemHandler = getItemHandler(getViewType(position));
            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), mData.get(position), position);
        }

    }

    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
        addHandler(-1, new LoadMoreHandler());
    }

    @Override
    public int getItemCount() {

        if (showLoadFootView) {
            return super.getItemCount() + 1;
        } else {
            return super.getItemCount();
        }
    }

    public boolean isShowLoadFootView() {
        return showLoadFootView;
    }

    public void setShowLoadFootView(boolean showLoadFootView) {
        this.showLoadFootView = showLoadFootView;
    }
}
