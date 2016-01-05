package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Arrays;
import java.util.Collections;
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
        colors = Arrays.asList(colorRandom);

    }

    private RelativeLayout.LayoutParams params;
    private static String[] colorRandom = new String[]{"#70aba0", "#e4b524", "#86b2f6", "#f77d7b", "#f4ad56", "#f9760b"};
    private List<String> colors;

    @Override
    public int getLayoutResId() {
        return R.layout.layout_fund_recommend_banner;
    }

    @Override
    public void onBindView(ViewHolder vh, final Message msg, int position) {
        final List<RecommendFundSpecialBannerBean> lists = (List<RecommendFundSpecialBannerBean>) msg.obj;
        if (lists == null || lists.size() == 0) {
            return;
        }
        if (lists.get(0).isFirstTimeLoad) {
            Collections.shuffle(colors);
            LinearLayout ll = vh.get(R.id.ll_fund);
            ll.removeAllViews();
            View leftView = new View(mContext);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(mContext.getResources().getDimensionPixelOffset(R.dimen.widget_margin_medium), 1));
            ll.addView(leftView);
            for (int i = 0; i < lists.size(); i++) {
                final int index = i % colors.size();
                final RecommendFundSpecialBannerBean item = lists.get(i);
                View bannerItem = View.inflate(mContext, R.layout.item_market_fund_banner, null);
                bannerItem.setLayoutParams(params);
                final ImageView iv_mask = (ImageView) bannerItem.findViewById(R.id.iv_mask);
                final ImageView imageView = (ImageView) bannerItem.findViewById(R.id.iv_bg);
                if (TextUtils.isEmpty(item.getRecommend_image_sm())) {
                    if (item.isFirstTimeLoad) {
                        imageView.setBackgroundColor(Color.parseColor(colors.get(index)));
                        item.isFirstTimeLoad = false;
                    }
                } else {
                    ImageLoaderUtils.loadImage(item.getRecommend_image_sm(), new ImageLoadingListener() {
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
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                                imageView.setBackgroundDrawable(bitmapDrawable);
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
                }
                if (TextUtils.isEmpty(item.getRecommend_image_sm()) && item.isFirstTimeLoad) {
                    imageView.setBackgroundColor(Color.parseColor(colors.get(index)));
                    item.isFirstTimeLoad = false;
                }
                TextView tv_title = (TextView) bannerItem.findViewById(R.id.tv_title);
                TextView tv_desc = (TextView) bannerItem.findViewById(R.id.tv_desc);
                bannerItem.setLayoutParams(params);
                tv_title.setText(item.getRecommend_title());
                tv_desc.setText(item.getRecommend_desc());
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
