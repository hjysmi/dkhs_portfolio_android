package com.dkhs.adpter.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

import java.util.HashMap;
import java.util.List;


public class DKBaseAdapter extends BaseAdapter {

    protected List<?> mData;
    protected Context mContext;

    public static final int DEF_VIEWTYPE = 0;

    private SparseArray<ItemHandler> mItemHandlerHashMap = new SparseArray<>();
    private HashMap<String, Integer> viewMap = new HashMap<>();

    public DKBaseAdapter(Context context, List<?> data) {
        mData = data;
        mContext = context;
    }


    public int getViewTypeKey(int position) {
        Object item = mData.get(position);
        Integer viewKey = viewMap.get(item.getClass().getSimpleName());
        if (null == viewKey) {
            Log.e("DKBaseAdapter", " getViewTypeKey not found: " + item.getClass().getSimpleName());
        }
        return viewKey == null ? DEF_VIEWTYPE : viewKey.intValue();

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
        if (isBuildMulti) {
            return getViewTypeKey(position);
        }
        return getViewType(position);
    }

    protected int getViewType(int position) {
        return DEF_VIEWTYPE;
    }


    @Override
    public int getViewTypeCount() {
        return mItemHandlerHashMap.size() == 0 ? 1 : mItemHandlerHashMap.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ItemHandler item = getItemHandler(getItemViewType(position));
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(item.getLayoutResId(), null);
        }
        item.onBindView(ViewHolder.newInstant(convertView), mData.get(position), position);
        return convertView;
    }

    public ItemHandler getItemHandler(int itemKey) {

        return mItemHandlerHashMap.get(itemKey);
    }

    public DKBaseAdapter buildSingleItemView(ItemHandler itemHandler) {
        isBuildMulti = false;
        mItemHandlerHashMap.clear();
        mItemHandlerHashMap.put(DEF_VIEWTYPE, itemHandler);
        return this;
    }

    private boolean isBuildMulti;

    public DKBaseAdapter buildMultiItemView(Class clzz, ItemHandler itemHandler) {
        Log.d(this.getClass().getSimpleName(), " buildMultiItemView:" + clzz.getSimpleName());

        isBuildMulti = true;
        int viewType = mItemHandlerHashMap.size();
        viewMap.put(clzz.getSimpleName(), viewType);
        mItemHandlerHashMap.put(viewType, itemHandler);
        return this;
    }

    /**
     * must override
     * getViewType
     */
    public DKBaseAdapter buildCustonTypeItemView(int type, ItemHandler itemHandler) {
        isBuildMulti = false;
        mItemHandlerHashMap.put(type, itemHandler);
        return this;
    }


    public void updateData(List<?> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


}


