/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class OptionalPriceAdapter extends BaseAdatperSelectStockFund {

    public OptionalPriceAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_optional_stock_price, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvPercentValue = (TextView) convertView.findViewById(R.id.tv_percent_value);
            viewHolder.tvIncearseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final SelectStockBean item = mDataList.get(position);
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.code);
        ColorStateList textCsl = null;
        if (item.isStop || StockUitls.isDelistStock(item.list_status)) {
            textCsl = ColorTemplate.getTextColor(R.color.theme_color);
        } else {
            textCsl = ColorTemplate.getUpOrDrownCSL(item.percentage);
        }
        if (StockUitls.isDelistStock(item.list_status)) {
            viewHolder.tvIncearseValue.setText("退市");
            viewHolder.tvIncearseValue.setTypeface(Typeface.DEFAULT);
            viewHolder.tvIncearseValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
        } else if (item.isStop) {
            viewHolder.tvIncearseValue.setText("停牌");
            viewHolder.tvIncearseValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
            viewHolder.tvIncearseValue.setTypeface(Typeface.DEFAULT);
        } else {
            viewHolder.tvIncearseValue.setTextColor(textCsl);
            viewHolder.tvIncearseValue.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
            viewHolder.tvPercentValue.setVisibility(View.VISIBLE);
            viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
            if (StockUitls.isShangZhengB(item.code)) {

                viewHolder.tvIncearseValue.setText(StringFromatUtils.get3Point(item.change));
            } else {

                viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
            }
        }
        if (StockUitls.isShangZhengB(item.code)) {
            viewHolder.tvCurrentValue.setText(StringFromatUtils.get3Point(item.currentValue));

        } else {
            viewHolder.tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));
        }
        viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        viewHolder.tvCurrentValue.setTextColor(textCsl);
        viewHolder.tvPercentValue.setTextColor(textCsl);

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;

        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvPercentValue;
        TextView tvIncearseValue;
    }
}
