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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
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
    public FiveRangeAdapter(Context mContext) {
        this.mContext = mContext;

    }

    // public void setList(List stocklist) {
    //
    // notifyDataSetChanged();
    // }

    @Override
    public int getCount() {
        if (isBuy) {
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_five_range, null);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // ConStockBean item = stockList.get(position);

        return convertView;
    }

    public final static class ViewHolder {

        TextView tvUserName;

    }

}
