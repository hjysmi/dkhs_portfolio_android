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
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TabFundsTitleChangeEvent;
import com.dkhs.portfolio.ui.fragment.TabFundsFragment;
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
public class OptionalFundsAdapter extends BaseAdatperSelectStockFund {

    public OptionalFundsAdapter(Context context) {
        super(context);
    }

    public OptionalFundsAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_optional_fund_price, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvPercentValue = (TextView) convertView.findViewById(R.id.tv_percent_value);
            viewHolder.tvTradeDay = (TextView) convertView.findViewById(R.id.tv_trade_day);
            viewHolder.ivWanShou = (ImageView) convertView.findViewById(R.id.iv_wanshou);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final SelectStockBean item = mDataList.get(position);
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.symbol);
        viewHolder.tvTradeDay.setText(item.tradeDay);

        if (!TextUtils.isEmpty(item.name) && item.name.length() > 8) {
            viewHolder.tvStockName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            viewHolder.tvStockName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        float perValue = 0;

        if (StockUitls.isSepFund(item.symbol_stype)) { //显示万份收益，七日年化
            viewHolder.ivWanShou.setVisibility(View.VISIBLE);
            viewHolder.tvCurrentValue.setText(item.tenthou_unit_incm + "");
            if (tabIndex == 0) {
                perValue = item.year_yld;
                viewHolder.tvPercentValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qiri, 0, 0
                        , 0);
//                viewHolder.tvPercentValue.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
                viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(perValue));
            } else {
                viewHolder.tvPercentValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0
                        , 0);
                viewHolder.tvPercentValue.setText("—");
            }

        } else {
            viewHolder.ivWanShou.setVisibility(View.GONE);
            viewHolder.tvPercentValue.setCompoundDrawablesWithIntrinsicBounds (0, 0, 0
                    , 0);
            if (tabIndex == 0) {
                perValue = item.percentage;
            } else if (tabIndex == 1) {
                perValue = item.change;
            } else if (tabIndex == 2) {
                perValue = item.total_capital;
            }
            viewHolder.tvCurrentValue.setText(item.currentValue + "");
            viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(perValue));

        }


        viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT_BOLD);
        viewHolder.tvPercentValue.setVisibility(View.VISIBLE);
        viewHolder.tvPercentValue.setBackgroundColor(ColorTemplate.getUpOrDrowBgColor(perValue));
        viewHolder.tvPercentValue.setOnClickListener(percentClick);


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
                BusProvider.getInstance().post(new TabFundsTitleChangeEvent(TabFundsFragment.TYPE_PER_DAY_UP));
                // PromptManager.showToast("Change tab text to:涨跌幅");

            } else if (tabIndex == 1) {
                // PromptManager.showToast("Change tab text to:涨跌额");
                BusProvider.getInstance().post(new TabFundsTitleChangeEvent(TabFundsFragment.TYPE_PER_MONTH_UP));

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                BusProvider.getInstance().post(new TabFundsTitleChangeEvent(TabFundsFragment.TYPE_PER_TYEAR_UP));

            }

            notifyDataSetChanged();

        }
    };

    final static class ViewHodler {
        TextView tvStockName;

        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvPercentValue;
        TextView tvTradeDay;
        ImageView ivWanShou;
    }
}
