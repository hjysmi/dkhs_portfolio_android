package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.MarketSubpageActivity;
import com.dkhs.portfolio.ui.fragment.MarketSubpageFragment;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialTitleHandler extends SimpleItemHandler<FundSpecialTitleType> {
    private Context mContext;

    public FundSpecialTitleHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_more_data;
    }

    @Override
    public void onBindView(ViewHolder vh, final FundSpecialTitleType data, int position) {
        switch (data) {
            case TITLE_FUND_MARKET:
                vh.getTextView(R.id.title).setText(R.string.fund_market);
                vh.getTextView(R.id.more).setText(R.string.look_at_all);
                vh.getTextView(R.id.more).setVisibility(View.VISIBLE);
                vh.get(R.id.divider).setVisibility(View.GONE);
                break;
            case TITLE_FUND_MANAGER:
                vh.getTextView(R.id.title).setText(R.string.fund_manager);
//                vh.getTextView(R.id.more).setText(R.string.more);
                vh.getTextView(R.id.more).setVisibility(View.GONE);
                vh.get(R.id.divider).setVisibility(View.GONE);
                break;
            case TITLE_SPECIAL:
                vh.getTextView(R.id.title).setText(R.string.special_financing);
                vh.getTextView(R.id.more).setVisibility(View.GONE);
                vh.get(R.id.divider).setVisibility(View.VISIBLE);
                vh.get(R.id.divider).setBackgroundColor(mContext.getResources().getColor(R.color.drivi_line));

                break;
        }
        vh.get(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data) {
                    case TITLE_FUND_MARKET:
                        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_ALL_RANKING_YEAR));
                        break;
                }
            }
        });
    }
}
