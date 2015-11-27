package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundManager;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

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
        vh.getTextView(R.id.tv_name).setText(data.getName());
        vh.getTextView(R.id.tv_company).setText(data.getRecommend_title());
        vh.getTextView(R.id.tv_week_win_rate).setText(data.getWin_rate_week());
        vh.getTextView(R.id.tv_week_profit_rate).setText(data.getIndex_rate_week());
        ImageLoaderUtils.setHeanderImage(data.getAvatar_sm(), vh.getImageView(R.id.iv_avatar));
        super.onBindView(vh, data, position);
    }
}
