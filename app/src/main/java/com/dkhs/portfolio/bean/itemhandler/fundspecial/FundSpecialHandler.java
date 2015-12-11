package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFundSpecialLineBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.UIUtils;

import org.parceler.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zcm on 2015/12/08.
 */
public class FundSpecialHandler extends SimpleItemHandler<RecommendFundSpecialLineBean> {
    private Context mContext;
    private int[] textColors = {R.color.fund_special_green,R.color.fund_special_yellow,R.color.fund_special_red};
    private int[] textSolidColors = {R.color.fund_special_green_translation,R.color.fund_special_yellow_translation,R.color.fund_special_red_translation};
    public FundSpecialHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.market_fund_special;
    }

    @Override
    public void onBindView(ViewHolder vh, final RecommendFundSpecialLineBean item, int position) {
        vh.getTextView(R.id.tv_special).setText(item.getRecommend_title());
        vh.getTextView(R.id.tv_special_name).setText(item.getAbbr_name());
        vh.getTextView(R.id.tv_special_percent).setText(item.getAbbr_name());
        int textColor = textColors[item.position];
        int textSolidColor = textSolidColors[item.position];
        vh.getTextView(R.id.tv_special).setTextColor(mContext.getResources().getColor(textColor));
        vh.getTextView(R.id.tv_special_name).setTextColor(mContext.getResources().getColor(textColor));
        vh.getTextView(R.id.tv_special_percent).setTextColor(mContext.getResources().getColor(textColor));
        String[] tags = item.getRecommend_desc().split(",");
        ((LinearLayout)vh.get(R.id.ll_special_tag)).removeAllViews();
        if(tags != null && tags.length > 0){
            if(tags.length > 5){
                List<String> arrayList = new ArrayList();
                for(int i = 0; i < 5; i++){
                    arrayList.add(tags[i]);
                }
                tags = arrayList.toArray(new String[]{});
            }
            for(String tag : tags){
                if(TextUtils.isEmpty(tag))
                    break;
                View child = View.inflate(mContext, R.layout.layout_special_tag, null);
                TextView tv_tag = (TextView) child.findViewById(R.id.tv_tag);
                tv_tag.setText(tag);
                tv_tag.setTextColor(mContext.getResources().getColor(textColor));
                GradientDrawable myGrad = (GradientDrawable)tv_tag.getBackground();
//                GradientDrawable myGrad = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_tv_specila_tag);
                myGrad.setColor(mContext.getResources().getColor(textSolidColor));
                tv_tag.setBackgroundDrawable(myGrad);
                ((LinearLayout)vh.get(R.id.ll_special_tag)).addView(child);
            }
        }
        if(!TextUtils.isEmpty(item.getRecommend_image_sm())){
            ImageLoaderUtils.setImage(item.getRecommend_image_sm(), vh.getImageView(R.id.iv_bg));
        }
        vh.get(R.id.fl_sepcial_feature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicsDetailActivity.startActivity(mContext,item.getId());
            }
        });
    }
}
