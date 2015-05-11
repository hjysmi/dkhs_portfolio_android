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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-25 下午3:35:49
 */
public class FiveRangeAdapter extends BaseAdapter {
    private Context mContext;
    // private StockQuotesBean mStockBean;
    private boolean isBuy;
    // private BuyPrice mBuyPrice;
    // private SellPrice mSellPrice;
    // private List<FiveRangeItem> dataList;

    private List<String> volList;
    private List<String> priceList;

    private float mCompareValue;

    private ListView.LayoutParams mItemViewLayoutParams;
    private boolean isLandspace;

    private int mLandTextFont;
//
//    public FiveRangeAdapter(Context mContext, boolean isBuy) {
//        this.mContext = mContext;
//        this.isBuy = isBuy;
//        mItemViewLayoutParams = new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        mItemViewLayoutParams.height = (int) (mContext.getResources().getDisplayMetrics().widthPixels / 13f);
//
//    }

    public FiveRangeAdapter(Context mContext, boolean isBuy, boolean isLandspace) {
        this.mContext = mContext;
        this.isBuy = isBuy;
        mItemViewLayoutParams = new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mItemViewLayoutParams.height = (int) (mContext.getResources().getDisplayMetrics().widthPixels / 13f);

        if (isLandspace) {
            mLandTextFont = 14;
        }

    }

    public FiveRangeAdapter(Context mContext, boolean isBuy, String symbol, boolean isLandspace) {
        this(mContext, isBuy, isLandspace);
        this.symbol = symbol;
    }

    private String symbol;

    //
    // public void setList(List<FiveRangeItem> dList, String symbol) {
    // this.dataList = dList;
    // this.symbol = symbol;
    // notifyDataSetChanged();
    // }
    public void setList(List<String> vList, List<String> pList, String symbol) {
        this.volList = vList;
        this.priceList = pList;
        this.symbol = symbol;
        notifyDataSetChanged();
    }

    public void setCompareValue(float compareValue) {
        this.mCompareValue = compareValue;
    }

    @Override
    public int getCount() {
        // if (null != dataList) {
        // return dataList.size();
        // }
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

        try {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_five_range, null);
                viewHolder.tvTag = (TextView) convertView.findViewById(R.id.tv_buytext);
                viewHolder.tvVol = (TextView) convertView.findViewById(R.id.tv_range_vol);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_detail_value);
                if (mLandTextFont > 0) {

                    viewHolder.tvTag.setTextSize(mLandTextFont);
                    viewHolder.tvVol.setTextSize(mLandTextFont);
                    viewHolder.tvPrice.setTextSize(mLandTextFont);
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // FiveRangeItem item = dataList.get(position);
            // convertView.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().widthPixels /
            // 13f);
            // if (!item.price.contains("-.---")) {
            if (null == priceList || priceList.size() <= position) {
                viewHolder.tvPrice.setText("—");

            } else if (isFloatText(priceList.get(position))) {
                float price = Float.parseFloat(priceList.get(position));
                viewHolder.tvPrice.setTextColor(ColorTemplate.getTextColor(price, mCompareValue));
                if (!TextUtils.isEmpty(symbol) && StockUitls.isShangZhengB(symbol)) {
                    viewHolder.tvPrice.setText(StringFromatUtils.get3Point(price));
                } else {
                    viewHolder.tvPrice.setText(StringFromatUtils.get2Point(price));
                }
            } else {
                viewHolder.tvPrice.setText("—");
            }

            if (null == volList || volList.size() <= position) {
                viewHolder.tvVol.setText("—");

            } else if (isFloatText(volList.get(position))) {
                float vol = Float.parseFloat(volList.get(position));
                viewHolder.tvVol.setText(StringFromatUtils.convertToWanHand(vol));
            } else {
                viewHolder.tvVol.setText("—");
            }

            if (isBuy) {
                viewHolder.tvTag.setText("买" + (position + 1));
            } else {
                viewHolder.tvTag.setText("卖" + (getCount() - position));

            }
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return convertView;
    }

    private boolean isFloatText(String str) {
        try {
            float value = Float.parseFloat(str);
            if (value != 0) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    public final static class ViewHolder {

        TextView tvTag;
        TextView tvVol;
        TextView tvPrice;

    }

}
