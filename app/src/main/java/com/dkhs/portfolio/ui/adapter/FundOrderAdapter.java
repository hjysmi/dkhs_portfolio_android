package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
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
        super(context, list, R.layout.item_optional_fund_price);
    }

    private String sort;
    private String fundType;

    public void setSortAndType(String fundType, String sort) {
        this.fundType = fundType;
        this.sort = sort;
    }

    @Override
    public void getView33(int position, View v, ViewHolderUtils.ViewHolder vh) {
        FundPriceBean fundBean = (FundPriceBean) list.get(position);
        vh.setTextView(R.id.tv_stock_name, fundBean.getAbbrname());
        vh.setTextView(R.id.tv_stock_num, "(" + fundBean.getSymbol() + ")");
        vh.setTextView(R.id.tv_trade_day, fundBean.getTradedate() );
        double value = fundBean.getValue(sort);
        /**
         * (306, '货币型','hb'),
         (307, '理财型','lc'),
         */
        vh.get(R.id.tv_percent_value).setBackgroundColor(0);
        vh.getTextView(R.id.tv_percent_value).setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        vh.getTextView(R.id.tv_percent_value).setPadding(0,0,context.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin),0);

        if (!TextUtils.isEmpty(fundBean.getAbbrname()) && fundBean.getAbbrname().length() > 8) {
            vh.getTextView(R.id.tv_stock_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            vh.getTextView(R.id.tv_stock_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        if (fundType.equals("hb") || fundType.equals("lc")) {
            //货币型 理财型
            vh.get(R.id.iv_wanshou).setVisibility(View.GONE);

            vh.getTextView(R.id.tv_percent_value).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0
                    , 0);
            vh.setTextView(R.id.tv_current_value, fundBean.getTenthou_unit_incm() + "");
            vh.setTextView(R.id.tv_percent_value, StringFromatUtils.get2PointPercent(fundBean.getYear_yld()));


        } else {
            if (StockUitls.isSepFund(fundBean.getSymbol_stype())) {

                LogUtils.e("isSepFund           ");
                vh.get(R.id.iv_wanshou).setVisibility(View.VISIBLE);

                vh.getTextView(R.id.tv_percent_value).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qiri_gray, 0, 0
                        , 0);
                vh.setTextView(R.id.tv_current_value,  fundBean.getTenthou_unit_incm() + "");
                vh.setTextView(R.id.tv_percent_value, StringFromatUtils.get2PointPercent(fundBean.getYear_yld()) );
            } else {

                vh.getTextView(R.id.tv_current_value).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0
                        , 0);
                vh.get(R.id.iv_wanshou).setVisibility(View.GONE);
                vh.setTextView(R.id.tv_current_value, fundBean.getNet_value() + "");
                vh.setTextView(R.id.tv_percent_value, StringFromatUtils.get2PointPercent(fundBean.getValue(sort)));
            }
        }

        if (value > 0) {

            vh.getTextView(R.id.tv_percent_value).setTextColor(context.getResources().getColorStateList(R.color.theme_color));
        } else if(value ==0) {
            vh.getTextView(R.id.tv_percent_value).setTextColor(context.getResources().getColorStateList(R.color.tag_gray));

        }else{
            vh.getTextView(R.id.tv_percent_value).setTextColor(context.getResources().getColorStateList(R.color.tag_green));

        }
    }
}
