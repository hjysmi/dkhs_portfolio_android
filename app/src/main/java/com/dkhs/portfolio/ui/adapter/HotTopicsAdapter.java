package com.dkhs.portfolio.ui.adapter;


import android.content.Context;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;

import java.util.HashMap;
import java.util.List;



/**
 * @author zwm
 * @version 2.0
 * @ClassName HotTopicsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class HotTopicsAdapter extends com.dkhs.adpter.adapter.AutoAdapter {

    private BannerHandler.RefreshEnable mRefreshEnable;



    public HotTopicsAdapter(Context context, List<?> data, BannerHandler.RefreshEnable refreshEnable) {
        super(context, data);
        mRefreshEnable = refreshEnable;
        initHandlers2();

    }


    protected void initHandlers2() {
        addHandler(0, new BannerHandler(mContext,mRefreshEnable));
        addHandler(1, new TopicsHandler(mContext));
    }

    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {

    }

    @Override
    protected int getViewType(int position) {

        if (mData.get(position) instanceof BannerTopicsBean) {
            return 0;
        } else {
            return 1;
        }
    }

}
