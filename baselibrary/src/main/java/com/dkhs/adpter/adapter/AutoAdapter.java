package com.dkhs.adpter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ClassMap;
import com.dkhs.adpter.util.ViewHolder;


public abstract class AutoAdapter extends BaseAdapter {

    protected List<?> mData;
    protected Context mContext;

    private ClassMap mClassMap =new ClassMap();
    protected AutoAdapter(Context context,List<?> data) {
        mData = data;
        mContext = context;
        initHandlers(mClassMap);
    }

    protected abstract void initHandlers(ClassMap mAdapterItemMap);

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

        return mClassMap.getViewType(mData.get(position).getClass());
    }

    @Override
    public int getViewTypeCount() {
        return mClassMap.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHandler item =getItemHandler(mData.get(position).getClass());
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(item.getLayoutResId(), null);
        }
        item.onBindView(ViewHolder.newInstant(convertView), mData.get(position), position);
        return convertView;
    }

    protected ItemHandler getItemHandler(Class cla) {
        return mClassMap.get(cla.toString());
    }
    protected ItemHandler getItemHandler(String clsDefine) {
        return mClassMap.get(clsDefine);
    }
}


