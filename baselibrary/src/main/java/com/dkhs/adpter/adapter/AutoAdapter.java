package com.dkhs.adpter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

import java.util.HashMap;
import java.util.List;


public abstract class AutoAdapter extends BaseAdapter {

    protected List<?> mData;
    protected Context mContext;

    private HashMap<Integer,ItemHandler> mItemHandlerHashMap =new HashMap<>();
    protected AutoAdapter(Context context,List<?> data) {
        mData = data;
        mContext = context;
        initHandlers(mItemHandlerHashMap);
    }

    protected abstract void initHandlers(HashMap<Integer,ItemHandler> itemHandlerHashMap);


    protected void addHandler(int index,ItemHandler itemHandler){
        mItemHandlerHashMap.put(index,itemHandler);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    protected abstract int getViewType(int position);

    @Override
    public int getViewTypeCount() {
        return mItemHandlerHashMap.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHandler item =getItemHandler(getViewType(position));
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(item.getLayoutResId(), null);
        }
        item.onBindView(ViewHolder.newInstant(convertView), mData.get(position), position);
        return convertView;
    }

    protected ItemHandler getItemHandler(int index) {
        return mItemHandlerHashMap.get(index);
    }

}


