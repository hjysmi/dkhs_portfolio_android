package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

public class TodayNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private boolean isSecondYanbao;

    public TodayNewsAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;

    }

    public void setSecondYanBao(boolean isSecondyanbao) {
        this.isSecondYanbao = isSecondyanbao;
    }

    public int TYPE_HEAD = 0;
    public int TYPE_CONTENT = 1;

    @Override
    public int getItemViewType(int position) {
        if (mDataList != null && mDataList.size() > 0) {
            return position == 0 ? TYPE_HEAD : TYPE_CONTENT;
        } else {
            return TYPE_HEAD;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null == mDataList) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private int width;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHodler1 holder1;
        ViewHodler2 holder2;
        OptionNewsBean newsBean = mDataList.get(position);
        if(type == TYPE_HEAD){
            if (convertView == null || !(convertView.getTag() instanceof ViewHodler1)){
                convertView = View.inflate(mContext, R.layout.adapter_info_news_today_head, null);
                holder1 = new ViewHodler1();
                holder1.tvInfo = (TextView) convertView.findViewById(R.id.tv_info);
                holder1.ivNews = (ImageView) convertView.findViewById(R.id.iv_news);
                convertView.setTag(holder1);
            }
            holder1 = (ViewHodler1) convertView.getTag();
            holder1.tvInfo.setText(newsBean.getTitle());
            if(!TextUtils.isEmpty(newsBean.getMedias().get(0).getImage_md())){
                ImageLoaderUtils.setImagDefault(newsBean.getMedias().get(0).getImage_md(), holder1.ivNews);
            }
        }else{
            if (convertView == null || !(convertView.getTag() instanceof ViewHodler2)){
                convertView = View.inflate(mContext, R.layout.adapter_info_news_today_content, null);
                holder2 = new ViewHodler2();
                holder2.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
                holder2.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder2.tvRelated = (TextView) convertView.findViewById(R.id.tv_related);
                holder2.ivNews = (ImageView) convertView.findViewById(R.id.iv_news);
                convertView.setTag(holder2);
            }
            holder2 = (ViewHodler2) convertView.getTag();
            holder2.tvTime.setText(TimeUtils.getMMDDString(newsBean.getPublish()));
            holder2.tvTitle.setText(newsBean.getTitle());
            final List<OptionNewsBean.Symbols> symbols = newsBean.getSymbols();
            if(width == 0){
                final ViewHodler2 finalHolder = holder2;
                finalHolder.tvRelated.setTag(position);
                holder2.tvRelated.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        width = finalHolder.tvRelated.getWidth();
                        if(symbols != null && symbols.size() > 0){
                            fillReleatedData(finalHolder.tvRelated, symbols);
                        }

                    }
                });
            }else{
                if(symbols != null && symbols.size() > 0){
                    fillReleatedData(holder2.tvRelated,symbols);
                }
            }
            if(!TextUtils.isEmpty(newsBean.getRecommendImageSm())){
                ImageLoaderUtils.setImagDefault(newsBean.getRecommendImageSm(), holder2.ivNews);
            }
        }

        return convertView;
    }
    private void fillReleatedData(TextView tv,List<OptionNewsBean.Symbols> symbols){
        StringBuilder releated = new StringBuilder("相关股票:");
        if(symbols.size() == 1){
            releated.append(symbols.get(0).getAbbrName());
        }else{
            releated.append(symbols.get(0).getAbbrName());
            releated.append("、");
            releated.append(symbols.get(1).getAbbrName());
            if(releated.length() * tv.getTextSize() > width){
                int start = 5+symbols.get(0).getAbbrName().length();
                int end = start + symbols.get(1).getAbbrName().length() + 1;
                releated.replace(start,end,"等");
            }else{
                if (symbols.size() > 2){
                    releated.append("等");
                }
            }
        }
        tv.setText(releated);
    }

    // final static class ViewHodler {
    // TextView tvTextName;
    // TextView tvTextNameNum;
    // TextView tvTextDate;
    // TextView tvTextDates;
    // TextView tvTextFrom;
    // }

    final static class ViewHodler1 {
        TextView tvInfo;
        ImageView ivNews;
    }
    final static class ViewHodler2 {
        TextView tvTitle;
        TextView tvRelated;
        TextView tvTime;
        ImageView ivNews;
    }
}
