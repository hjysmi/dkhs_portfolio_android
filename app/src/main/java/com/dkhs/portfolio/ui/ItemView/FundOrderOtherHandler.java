package com.dkhs.portfolio.ui.ItemView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.utils.FundUtils;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by xuetong on 2015/12/26.
 */
public class FundOrderOtherHandler extends SimpleItemHandler<FundPriceBean> {
    private Context mContext;
    private static final int TYPE_RISK_UNKNOW = -1;

    public FundOrderOtherHandler(Context context) {
        this.mContext = context;
    }

    private String sort;
    private String fundType;
    private int[] colors = new int[]{R.color.transparent, R.color.fund_special_green, R.color.fund_special_medium_low_green, R.color.fund_special_yellow, R.color.fund_special_medium_high_green, R.color.fund_special_red};

    public void setSortAndType(String fundType, String sort) {
        this.fundType = fundType;
        this.sort = sort;
    }

    @Override
    public void onBindView(ViewHolder vh, FundPriceBean data, int position) {
        super.onBindView(vh, data, position);
        float value = data.getValue(sort);
        vh.setTextView(R.id.tv_name, data.getAbbrname());
        vh.setTextView(R.id.tv_code, data.getCode());
        boolean allow_trade = data.isAllow_trade();
        if (!allow_trade) {
            //未代销
            vh.get(R.id.tv_sell).setVisibility(View.VISIBLE);
            vh.get(R.id.tv_rate).setVisibility(View.GONE);
            vh.get(R.id.tv_money).setVisibility(View.GONE);
            vh.setTextView(R.id.tv_sell, UIUtils.getResString(mContext, R.string.no_sell));
        } else {
            vh.get(R.id.tv_sell).setVisibility(View.GONE);
            vh.get(R.id.tv_rate).setVisibility(View.VISIBLE);
            vh.get(R.id.tv_money).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.tv_money, String.valueOf(data.getAmount_min_buy()) + UIUtils.getResString(mContext, R.string.min_money));
            double discount_rate_buy = data.getDiscount_rate_buy();
            if (discount_rate_buy == 0) {
                vh.setTextView(R.id.tv_rate, UIUtils.getResString(mContext, R.string.zero_rate));
            } else {
                vh.setTextView(R.id.tv_rate, StringFromatUtils.getDiscount(discount_rate_buy, mContext));
            }
        }
        if (StockUitls.isSepFund(data.getSymbol_stype())) {
            //货币型或理财型
            vh.get(R.id.tv_value).setVisibility(View.GONE);
        } else {
            vh.get(R.id.tv_value).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.tv_value, UIUtils.getResString(mContext, R.string.net_values) + String.valueOf(data.getNet_value()));
        }
        TextView tv_risk = vh.get(R.id.tv_risk);
        if (TYPE_RISK_UNKNOW == data.getInvestment_risk()) {
            tv_risk.setVisibility(View.GONE);
        } else {
            tv_risk.setVisibility(View.VISIBLE);
            setRiskColor(tv_risk, data.getInvestment_risk());
            tv_risk.setText(FundUtils.getInvestRiskByType(data.getInvestment_risk(), mContext) + UIUtils.getResString(mContext, R.string.risk));
        }

        vh.setTextView(R.id.tv_index, StringFromatUtils.get2PointPercent(data.getValue(sort)));
    }

    /**
     * 根据不同的风险等级设置不同的风险颜色
     *
     * @param tv_risk
     * @param investment_risk
     * @return
     */
    private GradientDrawable setRiskColor(TextView tv_risk, int investment_risk) {
        GradientDrawable gd = new GradientDrawable();
        int color;
        try{
            color = UIUtils.getResColor(mContext, colors[investment_risk]);
        }catch (Exception e){
            color = UIUtils.getResColor(mContext, colors[0]);
        }
        gd.setStroke(1, color);
        gd.setColor(mContext.getResources().getColor(R.color.transparent));
        gd.setCornerRadius((float) (UIUtils.dp2px(8)));
        tv_risk.setBackgroundDrawable(gd);
        tv_risk.setTextColor(color);
        return gd;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_market_top;
    }
}
