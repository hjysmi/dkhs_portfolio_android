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
import android.widget.LinearLayout;
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
    private boolean isBuy;

    private List<String> volList;
    private List<String> priceList;

    private float mCompareValue;


    private int mLandTextFont;

    public FiveRangeAdapter(Context mContext, boolean isBuy, boolean isLandspace) {
        this.mContext = mContext;
        this.isBuy = isBuy;

        if (isLandspace) {
            mLandTextFont = 14;
        }

    }

    private View containerView;


    public void setContainerView(View containerView) {
        this.containerView = containerView;
    }


    public FiveRangeAdapter(Context mContext, boolean isBuy, String symbol, boolean isLandspace) {
        this(mContext, isBuy, isLandspace);
        this.symbol = symbol;
    }

    private String symbol;

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
                viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.content_ll);
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
            if (null != containerView) {
                viewHolder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, containerView.getMeasuredHeight() / 5));
            }

            if (null == priceList || priceList.size() <= position) {
                viewHolder.tvPrice.setText("—");
                viewHolder.tvPrice.setTextColor(ColorTemplate.DEF_GRAY);

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
                viewHolder.tvPrice.setTextColor(ColorTemplate.DEF_GRAY);
            }

            if (null == volList || volList.size() <= position) {
                viewHolder.tvVol.setText("0");

            } else if (isFloatText(volList.get(position))) {
                float vol = Float.parseFloat(volList.get(position));
                viewHolder.tvVol.setText(StringFromatUtils.convertToWanHand(vol));
            } else {
                viewHolder.tvVol.setText("0");
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
            return value != 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public final static class ViewHolder {

        TextView tvTag;
        TextView tvVol;
        TextView tvPrice;
        LinearLayout linearLayout;

    }

}
