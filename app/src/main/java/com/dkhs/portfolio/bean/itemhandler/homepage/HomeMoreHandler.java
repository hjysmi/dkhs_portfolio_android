package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.ui.InfoActivity;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.MarketSubpageActivity;
import com.dkhs.portfolio.ui.fragment.MarketSubpageFragment;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class HomeMoreHandler extends SimpleItemHandler<HomeMoreBean> {
    private Context mContext;
    public HomeMoreHandler(Context context){
        mContext = context;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_more_data;
    }

    @Override
    public void onBindView(ViewHolder vh, final HomeMoreBean data, int position) {
        vh.get(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data.type) {
                    case HomeMoreBean.TYPE_FUND_MANAGER:
                        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_MANAGER_RANKING_SIX_MONTH));
                        break;
                    case HomeMoreBean.TYPE_FUND:
                        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_ALL_RANKING_MONTH));
                        break;
                    case HomeMoreBean.TYPE_PORTFOLIO:
                        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_COMBINATION));
                        break;
                    case HomeMoreBean.TYPE_REWARD:
                        MainActivity.gotoTopicsHome(mContext);
                        break;
                    case HomeMoreBean.TYPE_TOPIC:
                        MainActivity.gotoHostTopicsActivity(mContext);
                        break;
                    case HomeMoreBean.TYPE_NEWS:
                        mContext.startActivity(new Intent(mContext, InfoActivity.class));
                        break;
                }
            }
        });
        vh.getTextView(R.id.title).setText(data.getTitle());
        vh.get(R.id.divider).setBackgroundColor(vh.getContext().getResources().getColor(R.color.drivi_line));
        if(data.hide){
            vh.get(R.id.divider).setVisibility(View.GONE);
        }else{
            vh.get(R.id.divider).setVisibility(View.VISIBLE);
        }
    }
}
