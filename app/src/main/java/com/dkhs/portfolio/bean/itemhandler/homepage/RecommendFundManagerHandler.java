package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundManager;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundManagerHandler extends SimpleItemHandler<RecommendFundManager> {
    private Context mContext;
    public RecommendFundManagerHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendFundManager data, int position) {
        super.onBindView(vh, data, position);
    }
}
