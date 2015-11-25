package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundBean;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundHandler extends SimpleItemHandler<RecommendFundBean> {
    private Context mContext;
    public RecommendFundHandler(Context context){
        mContext = context;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_recommend_fund;
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendFundBean data, int position) {
        super.onBindView(vh, data, position);
    }
}
