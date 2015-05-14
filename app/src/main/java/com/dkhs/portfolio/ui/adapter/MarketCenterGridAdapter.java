/**
 * @Title MarketCenterGridAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-18 下午5:24:37
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName MarketCenterGridAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-18 下午5:24:37
 * @version 1.0
 */
public class MarketCenterGridAdapter extends BaseAdapter {

    private List<SelectStockBean> mDataList;
    private LayoutInflater mInflater;
    private Context mcontext;
    // private GridView mGridView;
    // private int mCount = 0;
    private boolean isPlate;

    public MarketCenterGridAdapter(Context context, List<SelectStockBean> datalist, boolean isplate) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = datalist;
        this.isPlate = isplate;
        this.mcontext = context;

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mark_center, parent, false);
            mViewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            mViewHolder.tvTitleName = (TextView) convertView.findViewById(R.id.tv_title_name);
            mViewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_main_value);
            mViewHolder.tvIncrease = (TextView) convertView.findViewById(R.id.tv_incease_value);
            mViewHolder.tvPercent = (TextView) convertView.findViewById(R.id.tv_incease_ratio);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (isPlate) {
            mViewHolder.tvStockName.setVisibility(View.VISIBLE);
        } else {

            mViewHolder.tvStockName.setVisibility(View.GONE);
        }

        SelectStockBean item = mDataList.get(position);

        float change = item.percentage;
        mViewHolder.tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
        if (change > 0) {
            mViewHolder.tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                    mcontext.getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
        } else if (change < 0) {
            mViewHolder.tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                    mcontext.getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
        } else {
            mViewHolder.tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        mViewHolder.tvTitleName.setText(item.name);
        mViewHolder.tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));
        mViewHolder.tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.percentage));
        mViewHolder.tvIncrease.setText(StringFromatUtils.get2PointPlus(item.change));

        return convertView;
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView tvStockName;
        public TextView tvTitleName;
        public TextView tvCurrentValue;
        public TextView tvIncrease;
        public TextView tvPercent;

    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

}