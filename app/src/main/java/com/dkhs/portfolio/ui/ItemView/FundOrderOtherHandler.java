package com.dkhs.portfolio.ui.ItemView;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;

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


    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_market_top;
    }
}
