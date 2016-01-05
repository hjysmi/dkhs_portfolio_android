package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundManagerHandler extends SimpleItemHandler<FundManagerBean> {
    private Context mContext;

    public RecommendFundManagerHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh, final FundManagerBean data, int position) {
        vh.getTextView(R.id.tv_name).setText(data.name);
        vh.getTextView(R.id.tv_company).setText(data.recommend_title);
        vh.setTextView(R.id.tv_profit_title, UIUtils.getResString(mContext, R.string.index_rate_six_month));
        vh.setTextView(R.id.tv_week_win_rate_title, UIUtils.getResString(mContext, R.string.win_rate_six_month));
        vh.getTextView(R.id.tv_week_win_rate).setText(StringFromatUtils.get2PointPercent(data.win_rate_six_month));
        vh.getTextView(R.id.tv_week_win_rate).setTextColor(ColorTemplate.getUpOrDrownCSL(data.win_rate_six_month));
        vh.getTextView(R.id.tv_week_profit_rate).setTextColor(ColorTemplate.getUpOrDrownCSL(data.index_rate_six_month));
        vh.getTextView(R.id.tv_week_profit_rate).setText(StringFromatUtils.get2PointPercent(data.index_rate_six_month));
        ImageLoaderUtils.setHeanderImage(data.avatar_sm, vh.getImageView(R.id.iv_avatar));
        vh.get(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(FundManagerActivity.newIntent(mContext, data.id + ""));
            }
        });
        super.onBindView(vh, data, position);
    }
}
