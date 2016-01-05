package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.dkhs.portfolio.bean.RecommendRewardBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.widget.DKHSTextView;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Arrays;
import java.util.Collections;
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
    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};
    private List<String> colors;

    public HomeRewardHandler(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels * 5 / 8;
        height = width / 2;
        params = new RelativeLayout.LayoutParams(width, height);
        colors = Arrays.asList(colorRandom);
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendRewardBean data, final int position) {
        super.onBindView(vh, data, position);
        vh.getConvertView().setBackgroundColor(vh.getContext().getResources().getColor(R.color.white));
        LinearLayout ll = vh.get(R.id.ll_gallery);
        ll.removeAllViews();
        List<TopicsBean> topicsBeans = data.getTopicsBeans();
        View leftView = new View(context);
        leftView.setLayoutParams(new ViewGroup.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.widget_margin_medium), 1));
        ll.addView(leftView);
        if (topicsBeans == null) {
            return;
        }
        Collections.shuffle(colors);
        for (int i = 0; i < topicsBeans.size(); i++) {
            final int index = i % colors.size();
            TopicsBean topicsBean = topicsBeans.get(i);
            View view = inflater.inflate(R.layout.layout_home_recommend_reward, null);
            view.findViewById(R.id.fm).setLayoutParams(params);
            DKHSTextView textView = (DKHSTextView) view.findViewById(R.id.tv_title);
            final ImageView imageView = (ImageView) view.findViewById(R.id.iv_bg);
            if (topicsBean.content_type == 40) {
                textView.setRewardValue(String.format(context.getString(R.string.blank_comment_count), topicsBean.reward_amount));
                textView.setLineSpacing(5, 1);
                textView.setText("&nbsp;" + topicsBean.recommend_title);
            } else {
                textView.setText(topicsBean.recommend_title);
            }

            ImageView iv_user = (ImageView) view.findViewById(R.id.iv_avatar);
            final ImageView iv_mask = (ImageView) view.findViewById(R.id.iv_mask);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);

            if (topicsBean.user != null && !TextUtils.isEmpty(topicsBean.user.getUsername())) {
                tv_name.setText(topicsBean.user.getUsername());
            } else {
                tv_name.setText("");
            }
            tv_time.setText(topicsBean.created_at_relative);
            tv_name.setText(topicsBean.user.getUsername());
            if (topicsBean.user != null && !TextUtils.isEmpty(topicsBean.user.getAvatar_md())) {
                ImageLoaderUtils.setHeanderImage(topicsBean.user.getAvatar_md(), iv_user);
            } else {
                iv_user.setImageResource(R.drawable.ic_user_head);
            }

            ImageLoaderUtils.loadImage(topicsBean.recommend_image_md, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    iv_mask.setVisibility(View.GONE);
                    imageView.setBackgroundColor(Color.parseColor(colors.get(index)));
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    iv_mask.setVisibility(View.GONE);
                    imageView.setBackgroundColor(Color.parseColor(colors.get(index)));

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (null != s && !TextUtils.isEmpty(s.trim())) {
                        iv_mask.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        iv_mask.setVisibility(View.GONE);
                        imageView.setBackgroundColor(Color.parseColor(colors.get(index)));
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    iv_mask.setVisibility(View.GONE);
                    imageView.setBackgroundColor(Color.parseColor(colors.get(index)));

                }
            });
            ll.addView(view);
            final TopicsBean bean = topicsBean;
            view.findViewById(R.id.fm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicsDetailActivity.startActivity(context, bean.getId());
                }
            });
        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_recommend_reward_root;
    }


}
