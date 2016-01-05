package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundManagerHandler extends SimpleItemHandler<FundManagerBean> {
    private Context mContext;
    public RecommendFundManagerHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh,final  FundManagerBean data, int position) {
        vh.getTextView(R.id.tv_name).setText(data.name);
        vh.getTextView(R.id.tv_company).setText(data.recommend_title);
        vh.getTextView(R.id.tv_week_win_rate).setText(StringFromatUtils.get2PointPercent(data.getValue("-win_rate_week")));
        vh.getTextView(R.id.tv_week_profit_rate).setText(StringFromatUtils.get2PointPercent(data.index_rate_week));
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
