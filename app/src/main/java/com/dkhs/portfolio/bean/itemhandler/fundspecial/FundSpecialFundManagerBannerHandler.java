package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialFundManagerBean;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialFundManagerBannerHandler extends SimpleItemHandler<RecommendFundSpecialFundManagerBean> {
    private Context mContext;

    public FundSpecialFundManagerBannerHandler(Context context) {
        mContext = context;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels * 5 / 8 + mContext.getResources().getDimensionPixelOffset(R.dimen.widget_padding_normal);
        int height = width / 2;
        params = new RelativeLayout.LayoutParams(width, height);
        colors = Arrays.asList(colorRandom);
    }

    private RelativeLayout.LayoutParams params;
    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};
    private List<String> colors;

    @Override
    public int getLayoutResId() {
        return R.layout.layout_fund_recommend_fundmanager_banner;
    }

    @Override
    public void onBindView(ViewHolder vh, final RecommendFundSpecialFundManagerBean managerBean, int position) {
        final List<FundManagerBean> lists = managerBean.lists;
        if (lists == null) {
            return;
        }
        if (managerBean.isFirstTimeLoad) {
            Collections.shuffle(colors);
            managerBean.isFirstTimeLoad = false;
            LinearLayout ll = vh.get(R.id.ll_fund);
            ll.removeAllViews();
            View leftView = new View(mContext);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelOffset(R.dimen.widget_margin_medium), 1));
            ll.addView(leftView);
            for (int i = 0; i < lists.size(); i++) {
                final int index = i % colors.size();
                final FundManagerBean item = lists.get(i);
                View bannerItem = View.inflate(mContext, R.layout.item_market_fundmanager_banner, null);
                ;
                bannerItem.setLayoutParams(params);
                bannerItem.findViewById(R.id.iv_bg).setBackgroundColor(Color.parseColor(colors.get(index)));
                ImageView iv_user_header = (ImageView) bannerItem.findViewById(R.id.iv_user_header);
                TextView tv_user_name = (TextView) bannerItem.findViewById(R.id.tv_user_name);
                LinearLayout ll_special_tag = (LinearLayout) bannerItem.findViewById(R.id.ll_special_tag);
                TextView tv_total_profit = (TextView) bannerItem.findViewById(R.id.tv_total_profit);
                bannerItem.setLayoutParams(params);

                tv_user_name.setText(item.name);
                tv_total_profit.setText(StringFromatUtils.get2PointPercent(item.index_rate_all));
                if (!TextUtils.isEmpty(item.avatar_sm)) {
                    ImageLoaderUtils.setHeanderImage(item.avatar_sm, iv_user_header);
                }
                String[] tags = item.recommend_desc.split(",");
                if (tags != null && tags.length > 0) {
                    if (tags.length > 3) {
                        tags = Arrays.copyOfRange(tags, 0, 3);
                    }
                    for (String tag : tags) {
                        if (TextUtils.isEmpty(tag))
                            break;
                        View child = View.inflate(mContext, R.layout.layout_special_fundmanager_tag, null);
                        TextView tv_tag = (TextView) child.findViewById(R.id.tv_tag);
                        tv_tag.setText(tag);
                        tv_tag.setTextColor(mContext.getResources().getColor(R.color.white));
                        GradientDrawable myGrad = (GradientDrawable) tv_tag.getBackground();
                        myGrad.setColor(mContext.getResources().getColor(R.color.white_10));
                        tv_tag.setBackgroundDrawable(myGrad);
                        ll_special_tag.addView(child);
                    }
                }
                bannerItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.startAnimationActivity((Activity) mContext, FundManagerActivity.newIntent(mContext, item.id + ""));
                    }
                });
                ll.addView(bannerItem);
            }
        }
    }
}
