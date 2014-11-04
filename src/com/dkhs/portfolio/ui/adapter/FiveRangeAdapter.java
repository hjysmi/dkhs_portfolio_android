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
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FiveRangeItem;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.bean.StockQuotesBean.BuyPrice;
import com.dkhs.portfolio.bean.StockQuotesBean.SellPrice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class FiveRangeAdapter extends BaseAdapter {
    private Context mContext;
    private StockQuotesBean mStockBean;
    private boolean isBuy;
    private BuyPrice mBuyPrice;
    private SellPrice mSellPrice;
    private List<FiveRangeItem> dataList;

    private float mCompareValue;

    // public FiveRangeAdapter(Context mContext, StockQuotesBean stockBean, boolean isBuy) {
    // this.mContext = mContext;
    // this.mStockBean = stockBean;
    // this.isBuy = isBuy;
    // if (null != mStockBean && mStockBean.getBuyPrice() != null) {
    // mBuyPrice = mStockBean.getBuyPrice();
    //
    // }
    // if (null != mStockBean && mStockBean.getSellPrice() != null) {
    // mSellPrice = mStockBean.getSellPrice();
    //
    // }
    //
    // }
    private ListView.LayoutParams mItemViewLayoutParams;

    public FiveRangeAdapter(Context mContext, boolean isBuy) {
        this.mContext = mContext;
        this.isBuy = isBuy;
        mItemViewLayoutParams = new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mItemViewLayoutParams.height = (int) (mContext.getResources().getDisplayMetrics().widthPixels / 13f);

    }

    public void setList(List<FiveRangeItem> dList) {
        this.dataList = dList;
        notifyDataSetChanged();
    }

    public void setCompareValue(float compareValue) {
        this.mCompareValue = compareValue;
    }

    @Override
    public int getCount() {
        if (null != dataList) {
            return dataList.size();
        }
        return 0;
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
        try {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_five_range, null);
                viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tv_buytext);
                viewHolder.tvVol = (TextView) convertView.findViewById(R.id.tv_range_vol);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_detail_value);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            FiveRangeItem item = dataList.get(position);
            // convertView.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().widthPixels /
            // 13f);
            // if (!item.price.contains("-.---")) {
            System.out.println("position:" + position + " item:" + item.price);
            if (Float.parseFloat(item.price) == 0) {
                viewHolder.tvPrice.setText("—");
            } else {

                viewHolder.tvPrice
                        .setTextColor(ColorTemplate.getTextColor(Float.parseFloat(item.price), mCompareValue));
                viewHolder.tvPrice.setText(StringFromatUtils.get2Point(Float.parseFloat(item.price)));
            }

            viewHolder.tvVol.setText(StringFromatUtils.convertToWan(item.vol));

            viewHolder.tvTag.setText(item.tag);
            // convertView.setLayoutParams(mItemViewLayoutParams);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return convertView;
    }

    public final static class ViewHolder {

        TextView tvTag;
        TextView tvVol;
        TextView tvPrice;

    }

}
