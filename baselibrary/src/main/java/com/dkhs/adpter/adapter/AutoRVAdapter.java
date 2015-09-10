package com.dkhs.adpter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;


public abstract class AutoRVAdapter extends RecyclerView.Adapter {

    protected List<?> mData;

    protected Context mContext;
    protected SparseArray<ItemHandler> mItemHandlerHashMap = new SparseArray<>();

    protected abstract void initHandlers(SparseArray<ItemHandler> itemHandlerHashMap);

    protected AutoRVAdapter(Context context, List<?> data) {
        mData = data;
        mContext = context;
        initHandlers(mItemHandlerHashMap);
    }

    protected void addHandler(int index, ItemHandler itemHandler) {
        mItemHandlerHashMap.put(index, itemHandler);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    protected abstract int getViewType(int position);


    protected ItemHandler getItemHandler(int index) {
        return mItemHandlerHashMap.get(index);
    }

    @Override
    public RcvAdapterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RcvAdapterItem(parent.getContext(), getItemHandler(viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHandler itemHandler = getItemHandler(getViewType(position));

        if (itemHandler == null) {
            throw new RuntimeException(mData.get(position).getClass() + "  缺少ItemHandler 类,导致不能绑定数据");
        } else {

            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), mData.get(position), position);
        }

    }


    public class RcvAdapterItem extends RecyclerView.ViewHolder {

        private ViewHolder vh;

        public RcvAdapterItem(Context context, ItemHandler t) {
            super((LayoutInflater.from(context).inflate(t.getLayoutResId(), null)));
            vh = ViewHolder.newInstant(itemView);
            itemView.setTag(vh);
        }


    }

}
