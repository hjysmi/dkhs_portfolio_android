package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.ui.MainActivity;

/**
 * Created by wuyongsen on 2015/12/08.
 */
public class FundSpecialMoreHandler extends SimpleItemHandler<HomeMoreBean> {
    private Context mContext;
    public FundSpecialMoreHandler(Context context){
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
                        MainActivity.gotoFundManagerRanking(mContext);
                        break;
                    case HomeMoreBean.TYPE_FUND:
                        MainActivity.gotoFundsRanking(mContext);
                        break;
                    case HomeMoreBean.TYPE_PORTFOLIO:
                        MainActivity.gotoCombinationRankingActivity(mContext);
                        break;
                    case HomeMoreBean.TYPE_REWARD:
                        MainActivity.gotoTopicsHome(mContext);
                        break;
                    case HomeMoreBean.TYPE_TOPIC:
                        MainActivity.gotoHostTopicsActivity(mContext);
                        break;
                }
            }
        });
        vh.getTextView(R.id.title).setText(data.getTitle());
        if(data.hide){
            vh.get(R.id.divider).setVisibility(View.GONE);
        }else{
            vh.get(R.id.divider).setVisibility(View.VISIBLE);
        }
    }
}
