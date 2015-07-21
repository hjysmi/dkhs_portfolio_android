package com.dkhs.adpter.adapter;

import android.content.Context;

import java.util.List;

import com.dkhs.adpter.listener.ItemHandler;
import com.dkhs.adpter.util.ClassMap;


public  abstract  class SingleAutoAdapter extends  AutoAdapter implements ItemHandler {


    protected SingleAutoAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(ClassMap classMap) {
        classMap.add("default", this);
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public Class<?> getDataClass() {
        return null;
    }


    @Override
    protected ItemHandler getItemHandler(Class cla) {
        return    getItemHandler("default");
    }
}
