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
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TabStockTitleChangeEvent;
import com.dkhs.portfolio.ui.fragment.TabStockFragment;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午3:36:46
 */
public class OptionalPriceAdapter extends BaseAdatperSelectStockFund {

    public OptionalPriceAdapter(Context context) {
        super(context);
    }

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
        viewHolder.tvStockNum.setText(item.symbol);
        ColorStateList textCsl = null;
        if (item.isStop || StockUitls.isDelistStock(item.list_status)) {
            textCsl = ColorTemplate.getTextColor(R.color.theme_color);
            viewHolder.tvPercentValue.setBackgroundColor(mContext.getResources().getColor(R.color.stock_gray_bg));
        } else {
            textCsl = ColorTemplate.getUpOrDrownCSL(item.percentage);
            viewHolder.tvPercentValue.setBackgroundColor(ColorTemplate.getUpOrDrowBgColor(item.percentage));
        }
        if (StockUitls.isDelistStock(item.list_status)) {
            viewHolder.tvPercentValue.setText("退市");
            viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT);
            // viewHolder.tvIncearseValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
        } else if (item.isStop) {
            viewHolder.tvPercentValue.setText("停牌");
            // viewHolder.tvPercentValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
            viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT);
        } else {
            // viewHolder.tvIncearseValue.setTextColor(textCsl);
            viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT_BOLD);
            // viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
            viewHolder.tvPercentValue.setVisibility(View.VISIBLE);

            if (tabIndex == 0) {
                viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
            } else if (tabIndex == 1) {

                if (StockUitls.isShangZhengB(item.symbol)) {

                    viewHolder.tvPercentValue.setText(StringFromatUtils.get3Point(item.change));
                } else {

                    viewHolder.tvPercentValue.setText(StringFromatUtils.get2Point(item.change));
                }

            }
            if (StockUitls.isShangZhengB(item.symbol)) {

                viewHolder.tvIncearseValue.setText(StringFromatUtils.get3Point(item.change));
            } else {

                viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
            }
        }

        if (tabIndex == 2) {
            if (item.total_capital == 0 || StockUitls.isIndexStock(item.symbol_type)) {

                viewHolder.tvPercentValue.setText("—");
            } else {
                viewHolder.tvPercentValue.setText(StringFromatUtils.convertToWan(item.total_capital));
            }
        }

        if (StockUitls.isShangZhengB(item.symbol)) {
            viewHolder.tvCurrentValue.setText(StringFromatUtils.get3Point(item.currentValue));

        } else {
            viewHolder.tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));
        }
        // viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        viewHolder.tvCurrentValue.setTextColor(textCsl);
        viewHolder.tvCurrentValue.setTextColor(ColorTemplate.getTextColor(R.color.black));
        viewHolder.tvPercentValue.setOnClickListener(percentClick);

        // animation.setDuration(500);
        // convertView.startAnimation(animation);
        // animation = null;
        // ObjectAnimator.ofFloat(convertView, "translationY", 500, 0);

        return convertView;
    }

    int tabIndex = 0;
    // private static final int updat

    private OnClickListener percentClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // change percent value
            tabIndex++;
            tabIndex = tabIndex % 3;
            if (tabIndex == 0) {
                BusProvider.getInstance().post(new TabStockTitleChangeEvent(TabStockFragment.TYPE_PERCENTAGE_UP));
                // PromptManager.showToast("Change tab text to:涨跌幅");

            } else if (tabIndex == 1) {
                // PromptManager.showToast("Change tab text to:涨跌额");
                BusProvider.getInstance().post(new TabStockTitleChangeEvent(TabStockFragment.TYPE_CHANGE_UP));

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                BusProvider.getInstance().post(new TabStockTitleChangeEvent(TabStockFragment.TYPE_TCAPITAL_UP));

            }

            notifyDataSetChanged();

        }
    };

    final static class ViewHodler {
        TextView tvStockName;

        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvPercentValue;
        TextView tvIncearseValue;
    }
}
