package com.dkhs.portfolio.ui.ItemView;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.utils.FundUtils;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * Created by xuetong on 2015/12/26.
 */
public class FundOrderOtherHandler extends SimpleItemHandler<FundPriceBean> {
    private Context mContext;

    public FundOrderOtherHandler(Context context) {
        this.mContext = context;
    }

    private String sort;
    private String fundType;

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
        vh.setTextView(R.id.tv_code, data.getCode());

        boolean allow_trade = data.isAllow_trade();
        if (!allow_trade) {
            //未代销
            vh.get(R.id.tv_sell).setVisibility(View.VISIBLE);
            vh.get(R.id.tv_rate).setVisibility(View.GONE);
            vh.get(R.id.tv_money).setVisibility(View.GONE);
            vh.setTextView(R.id.tv_sell, "未代销");
        } else {
            vh.get(R.id.tv_sell).setVisibility(View.GONE);
            vh.get(R.id.tv_rate).setVisibility(View.VISIBLE);
            vh.get(R.id.tv_money).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.tv_money, String.valueOf(data.getAmount_min_buy()) + "元起");
            double discount_rate_buy = data.getDiscount_rate_buy();
            if (discount_rate_buy == 0) {
                vh.setTextView(R.id.tv_rate, "0费率");
            } else {
                vh.setTextView(R.id.tv_rate, StringFromatUtils.getDiscount(discount_rate_buy, mContext));
            }
        }
        if (StockUitls.isSepFund(data.getSymbol_stype())) {
            //货币型或理财型
            vh.get(R.id.tv_value).setVisibility(View.GONE);
        } else {
            vh.get(R.id.tv_value).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.tv_value, "净值:" + data.getNet_value());
        }

        vh.setTextView(R.id.tv_risk, FundUtils.getInvestRiskByType(data.getInvestment_risk(), mContext) + "风险");
        vh.setTextView(R.id.tv_index, StringFromatUtils.get2PointPercent(data.getValue(sort)));

    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_market_top;
    }
}
