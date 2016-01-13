package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHodler holder = null;
        OptionNewsBean newsBean = mDataList.get(position);
        if (convertView == null) {
            holder = new ViewHodler();
            if (type == TYPE_HEAD) {
                convertView = View.inflate(mContext, R.layout.adapter_info_news_today_head, null);
                holder.tvInfo = (TextView) convertView.findViewById(R.id.tv_info);
                holder.ivNews = (ImageView) convertView.findViewById(R.id.iv_news);
                convertView.setTag(holder);
            } else {
                convertView = View.inflate(mContext, R.layout.adapter_info_news_today_content, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_info);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_info);
                holder.tvRelated = (TextView) convertView.findViewById(R.id.tv_related);
                holder.ivNews = (ImageView) convertView.findViewById(R.id.iv_news);
                convertView.setTag(holder);
            }
        }
        holder = (ViewHodler) convertView.getTag();
        if(type == TYPE_HEAD){
            holder.tvInfo.setText(newsBean.getText());
//            ImageLoaderUtils.setImage(newsBean.getUser().getHeadPitureMD(),holder.ivNews);
        }else{
            holder.tvTime.setText(TimeUtils.getBriefTimeString(newsBean.getPublish()));
            holder.tvTitle.setText(newsBean.getTitle());
            holder.tvRelated.setText(TimeUtils.getBriefTimeString(newsBean.getPublish()));
        }

        return convertView;
    }

    // final static class ViewHodler {
    // TextView tvTextName;
    // TextView tvTextNameNum;
    // TextView tvTextDate;
    // TextView tvTextDates;
    // TextView tvTextFrom;
    // }

    final static class ViewHodler {
        TextView tvInfo;
        TextView tvTitle;
        TextView tvRelated;
        TextView tvTime;
        ImageView ivNews;
    }
}
