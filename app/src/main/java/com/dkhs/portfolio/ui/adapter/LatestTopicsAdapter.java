package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import java.util.List;

import com.dkhs.adpter.adapter.*;
import com.dkhs.adpter.util.ClassMap;
import com.dkhs.adpter.util.ViewHolder;
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
    protected void initHandlers(ClassMap mAdapterItemMap) {

        mAdapterItemMap.add(new TopicsHandler());
    }

}
