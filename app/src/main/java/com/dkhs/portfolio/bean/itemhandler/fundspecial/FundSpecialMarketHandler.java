package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundSpecialMarketBean;
import com.dkhs.portfolio.ui.MarketSubpageActivity;
import com.dkhs.portfolio.ui.fragment.MarketSubpageFragment;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialMarketHandler extends SimpleItemHandler<RecommendFundSpecialMarketBean> implements View.OnClickListener {
    private Context mContext;

    public FundSpecialMarketHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.market_fund_market;
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendFundSpecialMarketBean item, int position) {
        vh.get(R.id.ll_fund_manager).setOnClickListener(this);
        vh.get(R.id.ll_profit_rank).setOnClickListener(this);
        vh.get(R.id.ll_hybrid).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO: 2015/12/8
        switch (v.getId()){
            case R.id.ll_fund_manager:
                UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_MANAGER_RANKING_WEEK));
                break;
            case R.id.ll_profit_rank:
                UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_ALL_RANKING_MONTH));
               // UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_PROFIT));
                break;
            case R.id.ll_hybrid:
                UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_MIXED_MONTH));
                break;
        }
    }
}
