package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.adpter.adapter.AutoAdapter;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsDetailHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;

import java.util.HashMap;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName LatestTopicsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class TopicsDetailAdapter extends AutoAdapter{


    public TopicsDetailAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
        addHandler(0,new TopicsDetailHandler(mContext));
    }

    @Override
    protected int getViewType(int position) {

        if(mData.get(position) instanceof TopicsBean){
            return 0;
//        }else{
//            return 1;
        }
        return 0;

    }


}
