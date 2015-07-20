package com.dkhs.portfolio.ui.adapter;


import android.content.Context;

import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;

import java.util.List;

import com.dkhs.adpter.util.ClassHashMap;


/**
 * @author zwm
 * @version 2.0
 * @ClassName HotTopicsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class HotTopicsAdapter extends com.dkhs.adpter.adapter.AutoRVAdapter {


    public HotTopicsAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(ClassHashMap adapterItemMap) {
        adapterItemMap.add(new BannerHandler(mContext));
        adapterItemMap.add(new TopicsHandler());
    }


}
