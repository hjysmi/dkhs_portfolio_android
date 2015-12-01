package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * Created by wuyongsen on 2015/11/26.
 */
public class RecomendPortfolioHandler extends SimpleItemHandler<CombinationBean> {
    private Context mContext;
    public RecomendPortfolioHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh,  final CombinationBean data, int position) {
        vh.get(R.id.ll_week_win_rate).setVisibility(View.GONE);
        vh.getTextView(R.id.tv_name).setText(data.getName());
        vh.getTextView(R.id.tv_company).setText(data.getRecommend_title());
        vh.getTextView(R.id.tv_week_profit_rate).setText(StringFromatUtils.get2PointPercent(data.getChng_pct_month()));
        vh.getTextView(R.id.tv_profit_title).setText(mContext.getText(R.string.month_profit_rate));
        ImageLoaderUtils.setHeanderImage(data.getUser().getAvatar_sm(), vh.getImageView(R.id.iv_avatar));
        vh.get(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(CombinationDetailActivity.newIntent(mContext, data));
            }
        });
        super.onBindView(vh, data, position);
    }
}
