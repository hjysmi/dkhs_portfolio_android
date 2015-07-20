package com.dkhs.adpter.adapter;

import android.content.Context;

import java.util.List;
import com.dkhs.adpter.listener.ItemHandler;
import com.dkhs.adpter.util.ClassHashMap;


public   abstract   class SingleRVAutoAdapter extends  AutoRVAdapter implements ItemHandler{


    protected SingleRVAutoAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(ClassHashMap classHashMap) {
        classHashMap.add("default", this);
    }


    @Override
    public Class<?> getDataClass() {
        return null;
    }


    @Override
    protected ItemHandler getItemHandler(int viewType) {
        return getItemHandler("default");
    }
}
