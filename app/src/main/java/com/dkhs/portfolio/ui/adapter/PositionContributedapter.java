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
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class PositionContributedapter extends BaseAdapter {
    private Context mContext;
    private List<ConStockBean> stockList;

    public PositionContributedapter(Context mContext, List<ConStockBean> stocks) {
        this.mContext = mContext;
        this.stockList = stocks;
        // setSurpusValue();
    }

    public void setList(List stocklist) {
        this.stockList = stocklist;
        // setSurpusValue();
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_positiondetail_increase, null);
            viewHolder.colorView = convertView.findViewById(R.id.view_color);
            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockCode = (TextView) convertView.findViewById(R.id.tv_stock_code);
            viewHolder.tvCenterValue = (TextView) convertView.findViewById(R.id.tv_center_value);
            viewHolder.tvRightValue = (TextView) convertView.findViewById(R.id.tv_right_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConStockBean item = stockList.get(position);
        viewHolder.colorView.setBackgroundColor(item.getDutyColor());
        viewHolder.tvCenterValue.setText(StringFromatUtils.get2PointPercent(item.getPercent()));
        return convertView;
    }

//    public void setSurpusValue() {
//        int surpusValu = 1;
//        for (int i = 0; i < stockList.size(); i++) {
//            surpusValu -= stockList.get(i).getPercent();
//        }
//        maxValue = surpusValu;
//
//    }

    private void notifySurpusValue(int value) {
        ConStockBean sur = stockList.get(stockList.size() - 1);
        stockList.get(stockList.size() - 1).setPercent(sur.getPercent() + value);
        notifyDataSetChanged();
    }

    public final static class ViewHolder {

        View colorView;
        TextView tvStockName;
        TextView tvStockCode;
        TextView tvCenterValue;
        TextView tvRightValue;
    }
    //
    // private IDutyNotify mDutyNotify;
    //
    // public void setDutyNotifyListener(IDutyNotify dutyNotify) {
    // this.mDutyNotify = dutyNotify;
    // }
    //
    // public interface IDutyNotify {
    // void notifyRefresh(int position, int value);
    //
    // void updateSurpus(int value);
    // }

}
