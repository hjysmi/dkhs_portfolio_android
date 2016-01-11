package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AchivementAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/8.
 */
public class AchivementAdapter extends SimpleItemHandler<FundManagerInfoBean.AchivementsEntity> {


    private int selectIndex = -1;

    private BenefitChartView benefitChartView;


    private Map<String, String> map = new HashMap<>();

    private ViewHolder preVh;

    private List<FundManagerInfoBean.AchivementsEntity> mData = new ArrayList<>();
    private Context mContext;
    private IRemoveChartViewListener mListener;

    public AchivementAdapter(Context mContext, List<FundManagerInfoBean.AchivementsEntity> list) {
        mData = list;
        this.mContext = mContext;
        String[] key = mContext.getResources().getStringArray(R.array.fund_type_keys);
        int[] values = mContext.getResources().getIntArray(R.array.fund_stype_values);

        for (int i = 0; i < values.length; i++) {
            String value = values[i] + "";
            map.put(value, key[i]);
        }
        benefitChartView = new BenefitChartView(mContext);

    }

    public void setListener(IRemoveChartViewListener listener){
        mListener = listener;
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
        return R.layout.item_achivement;
    }

    @Override
    public void onBindView(ViewHolder vh, FundManagerInfoBean.AchivementsEntity data, int position) {
        FundManagerInfoBean.AchivementsEntity achivementsEntity = data;
        vh.setTextView(R.id.symbol, map.get(achivementsEntity.getFund().getSymbol_stype() + ""));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(achivementsEntity.getStart_date());

        if (position == 0 && achivementsEntity.getEnd_date() == null) {
            vh.setTextView(R.id.headTV, mContext.getString(R.string.current_management_of_the_fund));
            vh.get(R.id.headTV).setVisibility(View.VISIBLE);
        } else {
            vh.setTextView(R.id.headTV, mContext.getString(R.string.history_management_of_the_fund));
            vh.get(R.id.headTV).setVisibility(View.VISIBLE);
        }
        if(position + 1 < mData.size()){
            FundManagerInfoBean.AchivementsEntity nextAchivementsEntity = mData.get(position + 1);
            if(achivementsEntity.getEnd_date() == null && nextAchivementsEntity.getEnd_date() != null){
                vh.get(R.id.view_divider).setVisibility(View.GONE);
            }
        }

        if (position > 0) {
            FundManagerInfoBean.AchivementsEntity preAchivementsEntity = mData.get(position - 1);

            if (preAchivementsEntity.getEnd_date() == null && achivementsEntity.getEnd_date() != null) {
                vh.setTextView(R.id.headTV, mContext.getString(R.string.history_management_of_the_fund));
                vh.get(R.id.headTV).setVisibility(View.VISIBLE);
            } else {

                vh.get(R.id.headTV).setVisibility(View.GONE);
            }

        }


        if (achivementsEntity.getEnd_date() == null) {
            stringBuilder.append(mContext.getString(R.string.up_to_now));
        } else {
            stringBuilder.append(mContext.getString(R.string.between_date)).append(achivementsEntity.getEnd_date());
        }
        vh.setTextView(R.id.tv_date, stringBuilder.toString());
        vh.setTextView(R.id.rateTV, mContext.getString(R.string.rate_total));
        if (selectIndex == position) {


            if (achivementsEntity.isExpend()) {
                AnimationHelper.expandView(vh.get(R.id.ll_chart), mContext.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight), false, null);
                vh.setTextView(R.id.tv_chart_switch, mContext.getString(R.string.collapse_benefit_curve));
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0);
                ViewGroup chatView = vh.get(R.id.ll_chart);
                if (chatView.getChildCount() == 0)
                    addBenefitView(chatView, achivementsEntity, false);
            }
        } else {
            if (achivementsEntity.isExpend()) {
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), true, null);
                achivementsEntity.setExpend(false);
            } else {
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), false, null);
            }
            vh.setTextView(R.id.tv_chart_switch, mContext.getString(R.string.expend_benefit_curve));
            vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
        }

        if (!TextUtils.isEmpty(achivementsEntity.getFund().getAbbr_name()) && achivementsEntity.getFund().getAbbr_name().length() > 8) {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            vh.setTextView(R.id.abbr_name, achivementsEntity.getFund().getAbbr_name().subSequence(0, 8) + "...");
        } else {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            vh.setTextView(R.id.abbr_name, achivementsEntity.getFund().getAbbr_name());
        }

        setText(vh.getTextView(R.id.cp_rate), achivementsEntity.getCp_rate());

        TextView shRateTV = vh.getTextView(R.id.sh_rate);
        TextView sh300TV = vh.getTextView(R.id.sh300);
        TextView shMarketTV = vh.getTextView(R.id.win_market);
        TextView shMarketRateTV = vh.getTextView(R.id.win_rate);

//        if (StockUitls.isSepFund(achivementsEntity.getFund().getSymbol_stype())) {
//
//            shRateTV.setText(R.string.null_number);
//            shRateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
//            shRateTV.setTextColor(mContext.getResources().getColorStateList(R.color.tag_gray));
//            sh300TV.setVisibility(View.GONE);
//
//        } else {

            shRateTV.setText(R.string.null_number);
            shMarketTV.setText(R.string.null_number);
            shRateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            sh300TV.setVisibility(View.VISIBLE);
            switch (achivementsEntity.getFund().getSymbol_stype()) {
                case 300:
                case 301:
                    //股票、混合型
                    sh300TV.setText(R.string.sh300);
                    shMarketTV.setText(R.string.win_market);
                    setText(shRateTV, achivementsEntity.getSh300_rate());
                    setText(shMarketRateTV, achivementsEntity.getCp_rate() -achivementsEntity.getSh300_rate());
                    break;
                case 306:
                case 307:
                    //货币、理财型
                    sh300TV.setText(R.string.wanshou);
                    shMarketTV.setText(R.string.qirinianhua);
                    setText(shRateTV, achivementsEntity.getSh300_rate());
                    setText(shMarketRateTV, achivementsEntity.getCp_rate() - achivementsEntity.getSh300_rate());
                    break;
                default:
                    //指数、其他型
                    sh300TV.setText(R.string.unit_value);
                    shMarketTV.setText(R.string.rate);
                    setText(shRateTV, achivementsEntity.getSh300_rate());
                    setText(shMarketRateTV, achivementsEntity.getCp_rate() - achivementsEntity.getSh300_rate());
                    break;
            }
//        }


        vh.get(R.id.ll_chart_switch).setOnClickListener(new OnClickImp(vh, achivementsEntity, position));
        vh.getConvertView().setOnClickListener(new OnItemClick(data));
    }


    class OnItemClick implements View.OnClickListener {
        FundManagerInfoBean.AchivementsEntity achivementsEntity;
        long lastClick;

        public OnItemClick(FundManagerInfoBean.AchivementsEntity achivementsEntity) {
            this.achivementsEntity = achivementsEntity;
        }

        @Override
        public void onClick(View v) {
            if ((System.currentTimeMillis() - lastClick) > 500) {
                mContext.startActivity(FundDetailActivity.newIntent(mContext, SelectStockBean.copy(achivementsEntity)));
                lastClick = System.currentTimeMillis();
            }
        }
    }


    class OnClickImp implements View.OnClickListener {

        ViewHolder vh;
        FundManagerInfoBean.AchivementsEntity achivementsEntity;
        int position;

        public OnClickImp(ViewHolder vh, FundManagerInfoBean.AchivementsEntity achivementsEntity, int position) {
            this.vh = vh;
            this.achivementsEntity = achivementsEntity;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (achivementsEntity.isExpend()) {

                ViewGroup chatView = vh.get(R.id.ll_chart);

                chatView.removeAllViews();
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), true, null);
                achivementsEntity.setExpend(false);
                vh.setTextView(R.id.tv_chart_switch, mContext.getString(R.string.expend_benefit_curve));
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);

            } else {
                AnimationHelper.expandView(vh.get(R.id.ll_chart), mContext.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight), true, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        achivementsEntity.setExpend(true);
                        vh.setTextView(R.id.tv_chart_switch, mContext.getString(R.string.collapse_benefit_curve));

                        vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                ViewGroup chatView = vh.get(R.id.ll_chart);

                AchivementAdapter.this.selectIndex = position;
                addBenefitView(chatView, achivementsEntity, true);
//                AchivementAdapter.this.notifyDataSetChanged();


            }

        }

    }

    private void addBenefitView(ViewGroup viewGroup, FundManagerInfoBean.AchivementsEntity achivementsEntity, boolean reDraw) {
        viewGroup.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        View view = benefitChartView.getBenifitView();
        if (view.getParent() != null) {
            ViewGroup vG = (ViewGroup) view.getParent();
            vG.removeView(view);
            if(mListener != null){
                mListener.collapseView();
            }
        }

        viewGroup.addView(view, params);
        if (reDraw)
            benefitChartView.draw(achivementsEntity);

    }

    public interface IRemoveChartViewListener {
        /**
         * 用于收起之前的曲线
         */
        void  collapseView();
    }


}
