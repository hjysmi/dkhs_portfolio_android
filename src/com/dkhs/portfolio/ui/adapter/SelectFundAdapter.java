/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class SelectFundAdapter extends BaseAdapter {

    private Context mContext;
    private int dataLenght = 5;

    public SelectFundAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {

        return dataLenght;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_refer_fund, null);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_item_select_fund);
            viewHolder.ivDelIcon = (ImageView) convertView.findViewById(R.id.iv_item_del);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        viewHolder.ivDelIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dataLenght--;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    final static class ViewHodler {
        TextView tvName;
        ImageView ivDelIcon;
    }
}
