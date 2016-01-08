package com.dkhs.portfolio.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BuyFundActivity;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.utils.FundUtils;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AchivementAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/8.
 */
public class SpecialFundAdapter extends SimpleItemHandler<FundQuoteBean> {


    private Map<String, String> map = new HashMap<>();


    private Context mContext;

    public SpecialFundAdapter(Context mContext) {
        this.mContext = mContext;
        String[] key = mContext.getResources().getStringArray(R.array.fund_type_keys);
        int[] values = mContext.getResources().getIntArray(R.array.fund_stype_values);

        for (int i = 0; i < values.length; i++) {
            String value = values[i] + "";
            map.put(value, key[i]);
        }
    }


    private void setText(TextView textView, double value) {
        if (value > 0) {

            textView.setTextColor(mContext.getResources().getColorStateList(R.color.tag_red));
        } else if (value == 0) {
            textView.setTextColor(mContext.getResources().getColorStateList(R.color.tag_gray));

        } else {
            textView.setTextColor(mContext.getResources().getColorStateList(R.color.tag_green));

        }
        textView.setText(StringFromatUtils.get2PointPercent((float) value));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_special_fund;
    }

    @Override
    public void onBindView(ViewHolder vh, final FundQuoteBean data, int position) {
        vh.setTextView(R.id.symbol, map.get(data.getSymbol_stype() + ""));


        if (!TextUtils.isEmpty(data.getAbbrName()) && data.getAbbrName().length() > 8) {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            vh.setTextView(R.id.abbr_name, data.getAbbrName().subSequence(0, 8) + "...");
        } else {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            vh.setTextView(R.id.abbr_name, data.getAbbrName());
        }


        setText(vh.getTextView(R.id.cp_rate), data.getPercent_six_month());

        TextView shRateTV = vh.getTextView(R.id.sh_rate);
        TextView sh300TV = vh.getTextView(R.id.sh300);
        TextView shMarketTV = vh.getTextView(R.id.win_market);
        TextView shMarketRateTV = vh.getTextView(R.id.win_rate);
        shRateTV.setText(R.string.null_number);
        shMarketTV.setText(R.string.null_number);
        sh300TV.setVisibility(View.VISIBLE);
        shRateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sh300TV.setText(R.string.amount_min_buy);
        shMarketTV.setText(R.string.investment_risk);
        shMarketRateTV.setText(FundUtils.getInvestRiskByType(data.getInvestment_risk(), vh.getContext()));
        if (StockUitls.isSepFund(data.getSymbol_stype())) {
            vh.getTextView(R.id.rateTV).setText(vh.getContext().getText(R.string.year_yld));
            setText(vh.getTextView(R.id.cp_rate), data.getYear_yld());
        } else {
            vh.getTextView(R.id.rateTV).setText(vh.getContext().getText(R.string.percent_six_month));
            setText(vh.getTextView(R.id.cp_rate), data.getPercent_six_month());
        }
        vh.getConvertView().setOnClickListener(new OnItemClick(data));
        LinearLayout ll_tags = vh.get(R.id.ll_tags);
        ll_tags.removeAllViews();
        ll_tags.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(data.getRecommend_desc())) {
            ll_tags.setVisibility(View.VISIBLE);
            String[] tags = data.getRecommend_desc().split(",");
            if (tags != null && tags.length > 0) {
                tags = Arrays.copyOfRange(tags, 0, 5);
                for (String tag : tags) {
                    if (TextUtils.isEmpty(tag))
                        break;
                    View child = View.inflate(mContext, R.layout.layout_special_tag, null);
                    TextView tv_tag = (TextView) child.findViewById(R.id.tv_tag);
                    tv_tag.setText(tag);
                    tv_tag.setTextColor(mContext.getResources().getColor(R.color.theme_blue));
                    GradientDrawable myGrad = (GradientDrawable) tv_tag.getBackground();
                    myGrad.setColor(mContext.getResources().getColor(R.color.theme_blue_transparent));
                    tv_tag.setBackgroundDrawable(myGrad);
                    ll_tags.addView(child);
                }
            }
        }
        Button buyBtn = vh.getButton(R.id.btn_buy);
        if (data.isAllow_buy()) {
            vh.getTextView(R.id.tv_discount_value).setText(StringFromatUtils.getDiscount(data.getFare_ratio_buy(), data.getDiscount_rate_buy(), vh.getContext()));
            buyBtn.setEnabled(true);
            shRateTV.setText(String.valueOf(data.getAmount_min_buy()));
            shRateTV.setTextColor(vh.getContext().getResources().getColor(R.color.black));
            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) mContext, BuyFundActivity.buyIntent(mContext, data));
                }
            });
        } else {
            buyBtn.setEnabled(false);
            buyBtn.setTextColor(vh.getContext().getResources().getColor(R.color.white));
            vh.getTextView(R.id.tv_discount_value).setText(R.string.null_number);
            shRateTV.setText(R.string.null_number);
            shRateTV.setTextColor(vh.getContext().getResources().getColor(R.color.tag_gray));
        }
    }


    class OnItemClick implements View.OnClickListener {
        FundQuoteBean symbolsBean;
        long lastClick;

        public OnItemClick(FundQuoteBean symbolsBean) {
            this.symbolsBean = symbolsBean;
        }

        @Override
        public void onClick(View v) {
            if ((System.currentTimeMillis() - lastClick) > 500) {
                mContext.startActivity(FundDetailActivity.newIntent(mContext, SelectStockBean.copy(symbolsBean)));
                lastClick = System.currentTimeMillis();
            }
        }
    }


}
