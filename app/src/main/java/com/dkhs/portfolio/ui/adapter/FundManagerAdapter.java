package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ManagersEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.adpter.adapter.SingleAutoAdapter;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FunManagerAdapter
 * @date 2015/6/09.10:07
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FundManagerAdapter extends SingleAutoAdapter {

    public FundManagerAdapter(Context context, List<?> list) {
        super(context, list);
    }



    @Override
    public int getLayoutResId() {
        return  R.layout.item_fund_manager;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {
        ManagersEntity bean = (ManagersEntity) mData.get(position);
        vh.setTextView(R.id.tv_manamger_name, bean.getName());
        vh.setTextView(R.id.tv_manamger_day, bean.getStart_date());
        vh.getTextView(R.id.tv_income_text).setTextColor(ColorTemplate.getUpOrDrownCSL(bean.getCp_rate()));
        vh.setTextView(R.id.tv_income_text, StringFromatUtils.get2PointPercent(bean.getCp_rate()));

    }
}
