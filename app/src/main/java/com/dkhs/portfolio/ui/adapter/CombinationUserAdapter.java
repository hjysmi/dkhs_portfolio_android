package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.CommendBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreBean;
import com.dkhs.portfolio.bean.MoreFootBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CombinationHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CombinationHeaderHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommendHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.LoadingHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.MoreHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.MoreFootHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;

import java.util.HashMap;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName CombinationUserAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class CombinationUserAdapter extends com.dkhs.adpter.adapter.AutoRVAdapter {
    public CombinationUserAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {


        addHandler(0, new NoDataHandler());
        addHandler(1, new CombinationHandler());
        addHandler(2, new CombinationHeaderHandler());
        addHandler(3, new CommendHandler());
        addHandler(4, new LoadingHandler());
        addHandler(5, new MoreHandler());
        addHandler(6, new MoreFootHandler());
    }

    @Override
    protected int getViewType(int position) {
        if (mData.get(position) instanceof List) {

            return 1;
        }
        if (mData.get(position) instanceof UserEntity) {

            return 2;
        }
        if (mData.get(position) instanceof CommendBean) {

            return 3;
        }
        if (mData.get(position) instanceof LoadingBean) {

            return 4;
        }
        if (mData.get(position) instanceof MoreBean) {

            return 5;
        }
        if (mData.get(position) instanceof MoreFootBean) {

            return 6;
        }
        if (mData.get(position) instanceof NoDataBean) {

            return 0;
        }


        return 0;
    }
}
