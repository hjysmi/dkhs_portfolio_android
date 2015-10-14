package com.dkhs.portfolio.bean.itemhandler.combinationdetail;


import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class RewardAdoptedHandler extends SimpleItemHandler<CommentBean> {


    private Context mContext;

    public RewardAdoptedHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_adopt_reply;
    }


    @Override
    public void onBindView(final ViewHolder vh, final CommentBean data, int position) {

    }


}