package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.nineoldandroids.animation.Animator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AchivementsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/8.
 */
public class AchivementAdapter extends AutoAdapter {



    private int selectIndex=-1;

    private  BenefitChartView benefitChartView;



    private Map<String,String> map=new HashMap<>();

    private  ViewHolderUtils.ViewHolder preVh;


    public AchivementAdapter(Context context, List<?> list) {
        super(context, list);

        String[]   key=context.getResources().getStringArray(R.array.fund_type_keys);
        int[]  values=context.getResources().getIntArray(R.array.fund_stype_values);

        for (int i = 0; i < values.length; i++) {
            String value = values[i]+"";
            map.put(value,key[i]);
        }
        benefitChartView=new BenefitChartView(context);

    }

    @Override
    public int setLayoutID() {
        return R.layout.item_achivement;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {




        getView33(position, v, vh);
    }

    public void getView33(int position, View v, ViewHolderUtils.ViewHolder vh) {
        FundManagerInfoBean.AchivementsEntity achivementsEntity= (FundManagerInfoBean.AchivementsEntity) list.get(position);
        vh.setTextView(R.id.symbol,map.get(achivementsEntity.getFund().getSymbol_stype()+""));
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(achivementsEntity.getStart_date());

        if(position==0 && achivementsEntity.getEnd_date() == null){
            vh.setTextView(R.id.headTV,"当前管理的基金");
            vh.get(R.id.headTV).setVisibility(View.VISIBLE);
        }else{
            vh.setTextView(R.id.headTV,"历史管理的基金");
            vh.get(R.id.headTV).setVisibility(View.VISIBLE);
        }


        if(position>0){
            FundManagerInfoBean.AchivementsEntity preAchivementsEntity= (FundManagerInfoBean.AchivementsEntity) list.get(position-1);

            if(preAchivementsEntity.getEnd_date()==null && achivementsEntity.getEnd_date() != null ){
                vh.setTextView(R.id.headTV,"历史管理的基金");
                vh.get(R.id.headTV).setVisibility(View.VISIBLE);
            }else{
                vh.get(R.id.headTV).setVisibility(View.GONE);
            }

        }




        if(achivementsEntity.getEnd_date()==null){
            stringBuilder.append(context.getString(R.string.up_to_now));
        }else{
            stringBuilder.append(context.getString(R.string.between_date)).append(achivementsEntity.getEnd_date());
        }
        vh.setTextView(R.id.tv_date,stringBuilder.toString());
        vh.setTextView(R.id.rateTV,context. getString(R.string.rate_total));
        if(selectIndex ==position){


            if (achivementsEntity.isExpend()) {
                AnimationHelper.expandView(vh.get(R.id.ll_chart), context.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight),false, null);
                vh.setTextView(R.id.tv_chart_switch,context. getString(R.string.collapse_benefit_curve));
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up,0,0,0);
                ViewGroup chatView=vh.get(R.id.ll_chart);
                if(chatView.getChildCount()==0)
                    addBenefitView(chatView,achivementsEntity,false);
            }
        }else{
            if (achivementsEntity.isExpend()) {
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), true,null);
                achivementsEntity.setExpend(false);
            }else{
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), false, null);
            }
             vh.setTextView(R.id.tv_chart_switch, context.getString(R.string.expend_benefit_curve));
             vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
        }

        if (!TextUtils.isEmpty(achivementsEntity.getFund().getAbbr_name()) && achivementsEntity.getFund().getAbbr_name().length() > 8) {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            vh.setTextView(R.id.abbr_name,achivementsEntity.getFund().getAbbr_name().subSequence(0,8)+"...");
        } else {
            vh.getTextView(R.id.abbr_name).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            vh.setTextView(R.id.abbr_name,achivementsEntity.getFund().getAbbr_name());
        }

        setText(vh.getTextView(R.id.cp_rate),achivementsEntity.getCp_rate());

        TextView shRateTV=vh.getTextView(R.id.sh_rate);
        TextView sh300TV=vh.getTextView(R.id.sh300);

        if(StockUitls.isSepFund(achivementsEntity.getFund().getSymbol_stype())){

            shRateTV.setText(R.string.null_number);
            shRateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            shRateTV.setTextColor(context.getResources().getColorStateList(R.color.tag_gray));
            sh300TV.setVisibility(View.GONE);

        }else {

            shRateTV.setText(R.string.null_number);
            shRateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            sh300TV.setVisibility(View.VISIBLE);
            setText(vh.getTextView(R.id.sh_rate), achivementsEntity.getSh300_rate());
        }



        vh.get(R.id.ll_chart_switch).setOnClickListener(new OnClickImp(vh,achivementsEntity,position));
        vh.getRootView().setOnClickListener(new OnItemClick ((FundManagerInfoBean.AchivementsEntity) list.get(position)));

    }

    private void setText(TextView textView, double value) {
        if (value > 0) {

            textView.setTextColor(context.getResources().getColorStateList(R.color.tag_red));
        } else if(value ==0) {
            textView.setTextColor(context.getResources().getColorStateList(R.color.tag_gray));

        }else{
            textView.setTextColor(context.getResources().getColorStateList(R.color.tag_green));

        }
        textView.setText(StringFromatUtils.get2PointPercent((float) value));
    }


    class OnItemClick implements View.OnClickListener{
        FundManagerInfoBean.AchivementsEntity achivementsEntity;

        public OnItemClick(FundManagerInfoBean.AchivementsEntity achivementsEntity) {
            this.achivementsEntity = achivementsEntity;
        }

        @Override
        public void onClick(View v) {
            context.startActivity(FundDetailActivity.newIntent(context, SelectStockBean.copy(achivementsEntity)));

        }
    }


    class OnClickImp implements View.OnClickListener{

        ViewHolderUtils.ViewHolder vh;
        FundManagerInfoBean.AchivementsEntity achivementsEntity;
        int position;

        public OnClickImp(ViewHolderUtils.ViewHolder vh, FundManagerInfoBean.AchivementsEntity achivementsEntity, int position) {
            this.vh = vh;
            this.achivementsEntity = achivementsEntity;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(achivementsEntity.isExpend()){

                ViewGroup chatView=vh.get(R.id.ll_chart);

                chatView.removeAllViews();
                AnimationHelper.collapseView(  vh.get(R.id.ll_chart),true,null);
                achivementsEntity.setExpend(false);
                vh.setTextView(R.id.tv_chart_switch, context. getString(R.string.expend_benefit_curve));
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down,0,0,0);

            }else{
                AnimationHelper.expandView(vh.get(R.id.ll_chart), context.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight), true, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        achivementsEntity.setExpend(true);
                        vh.setTextView(R.id.tv_chart_switch, context.getString(R.string.collapse_benefit_curve));

                        vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                ViewGroup chatView=vh.get(R.id.ll_chart);

                addBenefitView(chatView,achivementsEntity,true);
                AchivementAdapter.this.selectIndex=position;
                AchivementAdapter.this.notifyDataSetChanged();


            }

        }

    }

    private void addBenefitView(ViewGroup viewGroup,FundManagerInfoBean.AchivementsEntity achivementsEntity ,boolean reDraw){
        viewGroup.removeAllViews();
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

       View view= benefitChartView.getBenifitView();
        if(view.getParent() !=null){
            ViewGroup vG= (ViewGroup) view.getParent();
            vG.removeView(view);
        }

        viewGroup.addView(view,params);
        if(reDraw)
        benefitChartView.draw(achivementsEntity);

    }


}
