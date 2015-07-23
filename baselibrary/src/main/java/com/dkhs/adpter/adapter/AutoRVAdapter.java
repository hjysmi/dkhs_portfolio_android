package com.dkhs.adpter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;


public abstract class AutoRVAdapter extends RecyclerView.Adapter {

    protected List<?> mData;

    protected  Context mContext;
    protected HashMap<Integer,ItemHandler> mItemHandlerHashMap =new HashMap<>();

    protected abstract void initHandlers(HashMap<Integer,ItemHandler> itemHandlerHashMap);




    protected AutoRVAdapter(Context context,List<?> data) {
        mData = data;
        mContext = context;
        initHandlers(mItemHandlerHashMap);
    }
    protected void addHandler(int index,ItemHandler itemHandler){
        mItemHandlerHashMap.put(index,itemHandler);
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

    @Override
    public RcvAdapterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  RcvAdapterItem(parent.getContext(), getItemHandler(viewType));
    }

    protected ItemHandler getItemHandler(int position) {
        return mItemHandlerHashMap.get(getItemViewType(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ItemHandler itemHandler = getItemHandler(getViewType(position));

        if(itemHandler== null){
            throw  new RuntimeException(mData.get(position).getClass()+"  缺少ItemHandler 类,导致不能绑定数据");
        }else {

            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), mData.get(position), position);
        }

    }



    public  class RcvAdapterItem extends RecyclerView.ViewHolder {

        private ViewHolder vh;

        public RcvAdapterItem(Context context,ItemHandler t) {
            super((LayoutInflater.from(context).inflate(t.getLayoutResId(), null)));
            vh = ViewHolder.newInstant(itemView);
            itemView.setTag(vh);
        }





    }

}
