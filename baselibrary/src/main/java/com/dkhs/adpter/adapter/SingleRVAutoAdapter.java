package com.dkhs.adpter.adapter;

import android.content.Context;

import java.util.List;
import com.dkhs.adpter.listener.ItemHandler;
import com.dkhs.adpter.util.ClassMap;


public   abstract   class SingleRVAutoAdapter extends  AutoRVAdapter implements ItemHandler{


    protected SingleRVAutoAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(ClassMap classMap) {
        classMap.add("default", this);
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
