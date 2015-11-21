package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import android.app.Activity;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.UIUtils;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreFundManagerHandler extends SimpleItemHandler<FundManagerBean>{

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_user_manager_detail;
    }
    @Override
    public void onBindView(final ViewHolder vh, final FundManagerBean mFundManagerBean, int position) {
        final View itemView = vh.getConvertView();
        System.out.println("mFundManagerBean="+mFundManagerBean.toString());
        vh.getTextView(R.id.tv_user_name).setText(mFundManagerBean.name);
        vh.getTextView(R.id.tv_time).setText("从业"+mFundManagerBean.work_seniority);
        ImageLoaderUtils.setHeanderImage(mFundManagerBean.avatar_md, vh.getImageView(R.id.user_head));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView.getContext(), FundManagerActivity.newIntent((Activity) itemView.getContext(), mFundManagerBean));
            }
        });
    }
}
