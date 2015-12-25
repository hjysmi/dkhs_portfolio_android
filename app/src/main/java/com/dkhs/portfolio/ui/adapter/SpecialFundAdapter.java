package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.utils.FundUtils;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AchivementAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/8.
 */
public class SpecialFundAdapter extends SimpleItemHandler<TopicsBean.SymbolsBean> {




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
    public void onBindView(ViewHolder vh, TopicsBean.SymbolsBean data, int position) {
        vh.setTextView(R.id.symbol, map.get(data.symbol_stype + ""));

        vh.setTextView(R.id.rateTV, mContext.getString(R.string.percent_six_month));

        if (!TextUtils.isEmpty(data.abbr_name) && data.abbr_name.length() > 8) {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            vh.setTextView(R.id.abbr_name, data.abbr_name.subSequence(0, 8) + "...");
        } else {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            vh.setTextView(R.id.abbr_name, data.abbr_name);
        }

        vh.getTextView(R.id.tv_discount_value).setText(StringFromatUtils.getDiscount(data.discount_rate_buy, vh.getContext()));

        setText(vh.getTextView(R.id.cp_rate), data.percent_six_month);

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
        shRateTV.setText(String.valueOf(data.amount_min_buy));
        shMarketRateTV.setText(FundUtils.getInvestRiskByType(data.investment_risk,vh.getContext()));
        if (StockUitls.isSepFund(data.symbol_stype)) {
            vh.getTextView(R.id.rateTV).setText(vh.getContext().getText(R.string.year_yld));
            setText(vh.getTextView(R.id.cp_rate), Double.valueOf(data.year_yld));
        } else {
            vh.getTextView(R.id.rateTV).setText(vh.getContext().getText(R.string.rate));
            setText(vh.getTextView(R.id.cp_rate), data.percent_six_month);
        }
        vh.getConvertView().setOnClickListener(new OnItemClick(data));
        LinearLayout ll_tags = vh.get(R.id.ll_tags);
        ll_tags.removeAllViews();
        ll_tags.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(data.recommend_desc)){
            ll_tags.setVisibility(View.VISIBLE);
            String[] tags = data.recommend_desc.split(",");
            if(tags != null && tags.length > 0){
                for(String tag : tags){
                    if(TextUtils.isEmpty(tag))
                        break;
                    View child = View.inflate(mContext, R.layout.layout_special_tag, null);
                    TextView tv_tag = (TextView) child.findViewById(R.id.tv_tag);
                    tv_tag.setText(tag);
                    tv_tag.setTextColor(mContext.getResources().getColor(R.color.theme_blue));
                    GradientDrawable myGrad = (GradientDrawable)tv_tag.getBackground();
                    myGrad.setColor(mContext.getResources().getColor(R.color.theme_blue_transparent));
                    tv_tag.setBackgroundDrawable(myGrad);
                    ll_tags.addView(child);
                }
            }
        }
        vh.getButton(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("wys","buy");
            }
        });
    }


    class OnItemClick implements View.OnClickListener {
        TopicsBean.SymbolsBean symbolsBean;
        long lastClick;

        public OnItemClick(TopicsBean.SymbolsBean symbolsBean) {
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
