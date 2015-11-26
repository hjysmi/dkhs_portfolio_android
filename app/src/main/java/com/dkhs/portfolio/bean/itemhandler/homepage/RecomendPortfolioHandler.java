package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendPortfolio;

/**
 * Created by wuyongsen on 2015/11/26.
 */
public class RecomendPortfolioHandler extends SimpleItemHandler<RecommendPortfolio> {
    private Context mContext;
    public RecomendPortfolioHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendPortfolio data, int position) {
        vh.get(R.id.ll_week_win_rate).setVisibility(View.GONE);
        super.onBindView(vh, data, position);
    }
}
