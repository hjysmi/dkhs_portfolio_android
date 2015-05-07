/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class DiscussFlowAdapter extends BaseAdapter {
    private Context mContext;

    public DiscussFlowAdapter(Context mContext) {
        this.mContext = mContext;

    }

    public void setList(List stocklist) {

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_time_flow, null);
            viewHolder.ivUserHeader = (ImageView) convertView.findViewById(R.id.iv_user_header);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_discuss_name);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_discuss_time);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.view_discuss_content);
            viewHolder.tvSource = (TextView) convertView.findViewById(R.id.tv_discuss_source);
            viewHolder.tvAgreeCount = (TextView) convertView.findViewById(R.id.tv_discuss_agree);
            viewHolder.tvMessageCount = (TextView) convertView.findViewById(R.id.tv_discuss_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // ConStockBean item = stockList.get(position);

        return convertView;
    }

    public final static class ViewHolder {

        ImageView ivUserHeader;
        TextView tvUserName;
        TextView tvTime;
        TextView tvContent;
        TextView tvSource;
        TextView tvAgreeCount;
        TextView tvMessageCount;
    }

}
