package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import com.dkhs.adpter.adapter.*;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;

/**
 * @author zwm
 * @version 2.0
 * @ClassName LatestTopicsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class LatestTopicsAdapter extends AutoAdapter {


    public LatestTopicsAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
        addHandler(0,new TopicsHandler(mContext));
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }



}
