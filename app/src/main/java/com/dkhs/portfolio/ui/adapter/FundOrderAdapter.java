package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

import org.parceler.apache.commons.lang.StringUtils;

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
        super(context, list, R.layout.item_fund);
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
        vh.setTextView(R.id.market_text_name, fundBean.getAbbrname());
        vh.setTextView(R.id.market_text_name_num, "(" + fundBean.getSymbol() + ")");

        vh.setTextView(R.id.market_list_date, fundBean.getTradedate() + "");

        double value = fundBean.getValue(sort);


        /**
         * (306, '货币型','hb'),
         (307, '理财型','lc'),
         */
        if (fundType.equals("hb") || fundType.equals("lc")) {
            //货币型 理财型
            vh.get(R.id.iv_wanshou).setVisibility(View.GONE);
            vh.get(R.id.im_qiri).setVisibility(View.GONE);
            vh.setTextView(R.id.market_list_item_index, fundBean.getTenthou_unit_incm() + "");
            vh.setTextView(R.id.market_list_item_percent, StringFromatUtils.get2Point(fundBean.getYear_yld() ));
//            vh.setTextView(R.id.market_list_item_percent, ""+fundBean.getYear_yld() );


        } else {
            if (StockUitls.isSepFund(fundBean.getSymbol_stype())) {
                vh.get(R.id.iv_wanshou).setVisibility(View.VISIBLE);
                vh.get(R.id.im_qiri).setVisibility(View.VISIBLE);
                vh.setTextView(R.id.market_list_item_index,  fundBean.getTenthou_unit_incm() + "");
                vh.setTextView(R.id.market_list_item_percent, StringFromatUtils.get2Point( fundBean.getYear_yld()) );
//                vh.setTextView(R.id.market_list_item_percent, ""+ fundBean.getYear_yld());
            } else {
                vh.get(R.id.iv_wanshou).setVisibility(View.GONE);
                vh.get(R.id.im_qiri).setVisibility(View.GONE);
                vh.setTextView(R.id.market_list_item_index, fundBean.getNet_value() + "");
                vh.setTextView(R.id.market_list_item_percent, StringFromatUtils.get2Point(fundBean.getValue(sort)));
//                vh.setTextView(R.id.market_list_item_percent, ""+fundBean.getValue(sort));
            }
        }

        if (value != 0) {

            vh.getTextView(R.id.market_list_item_percent).setTextColor(context.getResources().getColorStateList(R.color.red));
        } else {
            vh.getTextView(R.id.market_list_item_percent).setTextColor(context.getResources().getColorStateList(R.color.tag_gray));

        }
    }
}
