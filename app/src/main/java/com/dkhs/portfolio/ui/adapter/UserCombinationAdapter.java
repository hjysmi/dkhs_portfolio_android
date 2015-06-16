/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-25 下午3:35:49
 */
public class UserCombinationAdapter extends BaseAdapter {
    private Context mContext;

    private List<CombinationBean> stockList;

    public UserCombinationAdapter(Context mContext, List<CombinationBean> stocks) {
        this.mContext = mContext;
        this.stockList = stocks;

    }

    @Override
    public int getCount() {
        return this.stockList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == stockList)
            return null;
        else {
            return stockList.get(position);
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_combination, null);
            viewHolder.tvCName = (TextView) convertView.findViewById(R.id.tv_combination_name);
            viewHolder.tvCenterValue = (TextView) convertView.findViewById(R.id.tv_center_value);
            viewHolder.tvRightValue = (TextView) convertView.findViewById(R.id.tv_right_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CombinationBean item = stockList.get(position);

        viewHolder.tvCenterValue.setText(TimeUtils.getSimpleDay(item.getCreateTime()));
        // viewHolder.colorView.setBackgroundColor(item.getDutyColor());
        // viewHolder.tvRightValue.setVisibility(View.VISIBLE);
        // viewHolder.tvStockCode.setText(item.getStockCode());
        // viewHolder.tvStockCode.setVisibility(View.VISIBLE);
        //
        viewHolder.tvCName.setText(item.getName());
        viewHolder.tvRightValue.setTextColor(ColorTemplate.getUpOrDrownCSL(item.getCumulative()));
        viewHolder.tvRightValue.setText(StringFromatUtils.get2PointPercent(item.getCumulative()));

        return convertView;
    }

    public final static class ViewHolder {

        TextView tvCName;
        TextView tvCenterValue;
        TextView tvRightValue;
    }

}
