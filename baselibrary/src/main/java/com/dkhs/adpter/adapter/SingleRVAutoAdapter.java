package com.dkhs.adpter.adapter;

import android.content.Context;
import android.util.SparseArray;

import com.dkhs.adpter.handler.ItemHandler;

import java.util.List;


public abstract class SingleRVAutoAdapter extends AutoRVAdapter implements ItemHandler {


    @Override
    protected void initHandlers(SparseArray<ItemHandler> itemHandlerHashMap) {
        addHandler(0, this);
    }

    protected SingleRVAutoAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

}
