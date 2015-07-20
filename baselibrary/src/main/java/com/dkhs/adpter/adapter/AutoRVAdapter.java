package com.dkhs.adpter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.dkhs.adpter.listener.ItemHandler;
import com.dkhs.adpter.util.ClassMap;
import com.dkhs.adpter.util.ViewHolder;


public abstract class AutoRVAdapter extends RecyclerView.Adapter {

    protected List<?> mData;

    protected  Context mContext;
    protected ClassMap mClassMap =new ClassMap();

    protected abstract void initHandlers(ClassMap classMap);


    protected AutoRVAdapter(Context context,List<?> data) {
        mData = data;
        mContext = context;
        initHandlers(mClassMap);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mClassMap.getViewType(mData.get(position).getClass());
    }

    @Override
    public RcvAdapterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  RcvAdapterItem(parent.getContext(), getItemHandler(viewType));
    }

    protected ItemHandler getItemHandler(int viewType) {
        return mClassMap.get(viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ItemHandler itemHandler = getItemHandler(mData.get(position).getClass());

        if(itemHandler== null){
            throw  new RuntimeException(mData.get(position).getClass()+"  缺少ItemHandler 类,导致不能绑定数据");
        }else {

            itemHandler.onBindView((ViewHolder) holder.itemView.getTag(), mData.get(position), position);
        }

    }

    protected ItemHandler getItemHandler(Class<?> c) {
        return mClassMap.get(c);
    }
    protected ItemHandler getItemHandler(String  c) {
        return mClassMap.get(c);
    }

    public  class RcvAdapterItem extends RecyclerView.ViewHolder {

        private ViewHolder vh;

        /**
         * 构造方法
         *
         * @param context     context对象
         * @param layoutResId 这个item布局文件的id
         */
        public RcvAdapterItem(Context context,ItemHandler t) {
            super((LayoutInflater.from(context).inflate(t.getLayoutResId(), null)));
            vh = ViewHolder.newInstant(itemView);
            itemView.setTag(vh);
        }





    }

}
