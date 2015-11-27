package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendPortfolio;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

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
        vh.getTextView(R.id.tv_name).setText(data.getName());
        vh.getTextView(R.id.tv_company).setText(data.getRecommend_title());
        vh.getTextView(R.id.tv_week_profit_rate).setText(data.getChng_pct_week());
        ImageLoaderUtils.setHeanderImage(data.getAvatar_sm(), vh.getImageView(R.id.iv_avatar));
        super.onBindView(vh, data, position);
    }
}
