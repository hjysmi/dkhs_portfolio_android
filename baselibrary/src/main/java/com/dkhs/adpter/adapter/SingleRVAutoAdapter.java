package com.dkhs.adpter.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import com.dkhs.adpter.handler.ItemHandler;


public  abstract  class SingleRVAutoAdapter extends  AutoRVAdapter implements ItemHandler{


    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
        addHandler(0,this);
    }

    protected SingleRVAutoAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

}
