package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName FundOrderAdapter
 * @Description TODO 基金排行榜适配器
 * @date 2015/6/2.
 */
public class FundOrderAdapter extends AutoAdapter {


    public FundOrderAdapter(Context context, List<?> list) {
        super(context, list);
    }

    private String sort;
    private String fundType;

    public void setSortAndType(String fundType, String sort) {
        this.fundType = fundType;
        this.sort = sort;
    }

    @Override
    public int setLayoutID() {
        return R.layout.item_optional_fund_price;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {
        FundPriceBean fundBean = (FundPriceBean) list.get(position);
        vh.setTextView(R.id.tv_stock_name, fundBean.getAbbrname());
        vh.setTextView(R.id.tv_stock_num, fundBean.getSymbol());
        vh.setTextView(R.id.tv_trade_day, TimeUtils.simpleDateToMonthDay(fundBean.getTradedate()));
        float value = fundBean.getValue(sort);
        /**
         * (306, '货币型','hb'),
         (307, '理财型','lc'),
         */
        vh.get(R.id.ll_percent_value).setBackgroundColor(0);
        vh.getTextView(R.id.tv_percent_value).setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        vh.getTextView(R.id.tv_percent_value).setPadding(0, 0, context.getResources().getDimensionPixelOffset(R.dimen.padding_kline), 0);

        if (!TextUtils.isEmpty(fundBean.getAbbrname()) && fundBean.getAbbrname().length() > 8) {
            vh.getTextView(R.id.tv_stock_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            vh.getTextView(R.id.tv_stock_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        vh.getImageView(R.id.iv_qiri).setImageResource(R.drawable.ic_qiri_gray);



        if (StockUitls.isSepFund(fundBean.getSymbol_stype())) {

            if (fundType.equals("hb") || fundType.equals("lc")) {
                vh.get(R.id.iv_wanshou).setVisibility(View.GONE);
                vh.getImageView(R.id.iv_qiri).setVisibility(View.GONE);
            }else {
                vh.get(R.id.iv_wanshou).setVisibility(View.VISIBLE);
                vh.getImageView(R.id.iv_qiri).setVisibility(View.VISIBLE);
            }

            vh.setTextView(R.id.tv_current_value, StringFromatUtils.get4Point(fundBean.getTenthou_unit_incm()));
            vh.setTextView(R.id.tv_percent_value, StringFromatUtils.get2PointPercent(fundBean.getValue(sort)));
        } else {
            vh.get(R.id.iv_wanshou).setVisibility(View.GONE);
            vh.getImageView(R.id.iv_qiri).setVisibility(View.GONE);
            vh.setTextView(R.id.tv_current_value, StringFromatUtils.get4Point(fundBean.getNet_value()));
            vh.setTextView(R.id.tv_percent_value, StringFromatUtils.get2PointPercent(fundBean.getValue(sort)));
        }




            vh.getTextView(R.id.tv_percent_value).setTextColor(ColorTemplate.getPercentColor(value));

        if(StockUitls.isDelistStock(fundBean.getList_status())){
            LogUtils.e("isDelistStock");
            vh.getTextView(R.id.tv_percent_value).setTextColor(context.getResources().getColorStateList(R.color.tag_gray));
            vh.setTextView(R.id.tv_percent_value, context.getString(  R.string.exit_stock));

        }else if(fundBean.isStop()){
            LogUtils.e("isStop");
            vh.getTextView(R.id.tv_percent_value).setTextColor(context.getResources().getColorStateList(R.color.tag_gray));
            vh.setTextView(R.id.tv_percent_value, context.getString(  R.string.delist));
        }


    }
}
