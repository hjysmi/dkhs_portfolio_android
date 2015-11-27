package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.LinearLayout;
import com.dkhs.portfolio.bean.RecommendRewardBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.widget.DKHSTextView;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by xuetong on 2015/11/26.
 */
public class HomeRewardHandler extends SimpleItemHandler<RecommendRewardBean> {
    private Context context;
    private LayoutInflater inflater;

    private int width;
    private int height;
    private RelativeLayout.LayoutParams params;

    public HomeRewardHandler(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels*5/8;
        height = width/2;
        params = new RelativeLayout.LayoutParams(width,height);
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendRewardBean data, int position) {
        super.onBindView(vh, data, position);
        LinearLayout ll = vh.get(R.id.ll_gallery);
        List<TopicsBean> topicsBeans = data.getTopicsBeans();
        if(null!=topicsBeans&&topicsBeans.size()>0){
            for(TopicsBean topicsBean : topicsBeans){
                View view = inflater.inflate(R.layout.layout_home_recommend_reward, null);
                view.findViewById(R.id.fm).setLayoutParams(params);
                DKHSTextView textView = (DKHSTextView) view.findViewById(R.id.tv_title);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_bg);
                if(topicsBean.content_type == 40){
                    textView.setRewardValue(String.format(context.getString(R.string.blank_comment_count),topicsBean.rewarded_comment));
                }
                textView.setLineSpacing(5,1);
                textView.setText(topicsBean.recommend_title);
                ImageLoaderUtils.setImagDefault(topicsBean.recommend_image_md,imageView);
                ll.addView(view);
            }
        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_recommend_reward_root;
    }



}
