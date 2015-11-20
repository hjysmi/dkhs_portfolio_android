package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreFundManagerHandler extends SimpleItemHandler<FundManagerBean>{

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_user_manager;
    }
    @Override
    public void onBindView(final ViewHolder vh, final FundManagerBean mFundManagerBean, int position) {
        final View itemView = vh.getConvertView();
        vh.getTextView(R.id.tv_user_name).setText(mFundManagerBean.name);
        ImageLoaderUtils.setHeanderImage(mFundManagerBean.avatar_md, vh.getImageView(R.id.user_head));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView.getContext(), FundManagerActivity.newIntent((Activity) itemView.getContext(), mFundManagerBean));
            }
        });
    }
}
