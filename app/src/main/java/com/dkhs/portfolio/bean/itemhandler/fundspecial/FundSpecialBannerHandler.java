package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.LinearLayout;
import com.dkhs.portfolio.bean.RecommendFundSpecialBannerBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialBannerHandler extends SimpleItemHandler<Message> {
    private Context mContext;

    public FundSpecialBannerHandler(Context context) {
        mContext = context;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels * 5 / 8 + mContext.getResources().getDimensionPixelOffset(R.dimen.widget_padding_normal);
        int height = width / 2;
        params = new RelativeLayout.LayoutParams(width, height);
    }
    private RelativeLayout.LayoutParams params;
    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};

    @Override
    public int getLayoutResId() {
        return R.layout.layout_fund_recommend_banner;
    }

    @Override
    public void onBindView(ViewHolder vh, final Message msg, int position) {
        final List<RecommendFundSpecialBannerBean> lists = (List<RecommendFundSpecialBannerBean>) msg.obj;
        if(lists == null || lists.size() == 0){
            return;
        }
        if(lists.get(0).isFirstTimeLoad){
            LinearLayout ll = vh.get(R.id.ll_fund);
            ll.removeAllViews();
            View leftView = new View(mContext);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelOffset(R.dimen.widget_margin_medium), 1));
            ll.addView(leftView);
            for(final RecommendFundSpecialBannerBean item : lists){
                View bannerItem =  View.inflate(mContext, R.layout.item_market_fund_banner, null);;
                bannerItem.setLayoutParams(params);
                if(item.isFirstTimeLoad){
                    bannerItem.findViewById(R.id.ll_content).setBackgroundColor(Color.parseColor(colorRandom[new Random().nextInt(colorRandom.length)]));
                    item.isFirstTimeLoad = false;
                }
                TextView tv_title = (TextView) bannerItem.findViewById(R.id.tv_title);
                TextView tv_desc = (TextView) bannerItem.findViewById(R.id.tv_desc);
                bannerItem.setLayoutParams(params);
                tv_title.setText(item.getRecommend_title());
                tv_desc.setText(item.getRecommend_desc());
                if(!TextUtils.isEmpty(item.getRecommend_image_sm())){
                    ImageLoaderUtils.setImage(item.getRecommend_image_sm(),(ImageView) bannerItem.findViewById(R.id.iv_bg));
                }
                bannerItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TopicsDetailActivity.startActivity(mContext, item.getId());

                    }
                });
                ll.addView(bannerItem);
            }
        }
    }
}
