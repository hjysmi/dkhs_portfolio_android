package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.util.SparseArray;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.CombinationsBean;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreBean;
import com.dkhs.portfolio.bean.MoreFootBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.bean.itemhandler.FooterHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CombinationHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CombinationHeaderHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommentHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.LoadingHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.MoreFootHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.MoreHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;

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
    protected void initHandlers(SparseArray<ItemHandler> itemHandlerHashMap) {


        addHandler(0, new NoDataHandler());
        addHandler(1, new CombinationHandler(mContext));
        addHandler(2, new CombinationHeaderHandler());
        CommentHandler commentHandler = new CommentHandler(mContext, false);
        commentHandler.setReplyComment(true);
        addHandler(3, commentHandler);
        addHandler(4, new LoadingHandler());
        addHandler(5, new MoreHandler(mContext));
        addHandler(6, new MoreFootHandler(mContext));
        addHandler(7, new TopicsHandler(mContext, false));
        addHandler(8, new FooterHandler());
    }

    @Override
    protected int getViewType(int position) {
        if (mData.get(position) instanceof CombinationsBean) {

            return 1;
        }
        if (mData.get(position) instanceof UserEntity) {

            return 2;
        }
        if (mData.get(position) instanceof CommentBean) {

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
        if (mData.get(position) instanceof TopicsBean) {

            return 7;
        }
        if (mData.get(position) instanceof UserHomePageFooterBean) {

            return 8;
        }


        return 0;
    }
}
