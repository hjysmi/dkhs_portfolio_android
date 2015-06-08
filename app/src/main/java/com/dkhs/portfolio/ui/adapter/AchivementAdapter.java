package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.ui.widget.CompareIndexView;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.nineoldandroids.animation.Animator;

import org.parceler.apache.commons.lang.StringUtils;

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



    private Map<String,String> map=new HashMap<>();

    private  ViewHolderUtils.ViewHolder preVh;


    public AchivementAdapter(Context context, List<?> list) {
        super(context, list, R.layout.item_achivement);

        String[]   key=context.getResources().getStringArray(R.array.fund_type_keys);
        String[]  values=context.getResources().getStringArray(R.array.fund_type_values);

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            map.put(value,key[i]);
        }

    }

    @Override
    public void getView33(int position, View v, ViewHolderUtils.ViewHolder vh) {
        FundManagerInfoBean.AchivementsEntity achivementsEntity= (FundManagerInfoBean.AchivementsEntity) list.get(position);

        vh.setTextView(R.id.abbr_name,achivementsEntity.getFund().getAbbr_name());


        vh.setTextView(R.id.symbol,map.get(achivementsEntity.getFund().getSymbol_stype()+""));
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(achivementsEntity.getStart_date());
        if(achivementsEntity.getEnd_date()==null){
            stringBuilder.append("至今");
        }else{
            stringBuilder.append(" 至 ").append(achivementsEntity.getEnd_date());
        }
        vh.setTextView(R.id.tv_date,stringBuilder.toString());
        if(selectIndex ==position){


            if (achivementsEntity.isExpend()) {
                AnimationHelper.expandView(vh.get(R.id.ll_chart), context.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight),false, null);
                vh.setTextView(R.id.tv_chart_switch,"收起收益曲线");
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up,0,0,0);
            }

        }else{
            if (achivementsEntity.isExpend()) {
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), true);
                achivementsEntity.setExpend(false);
            }else{
                AnimationHelper.collapseView(vh.get(R.id.ll_chart), false);
            }
             vh.setTextView(R.id.tv_chart_switch, "显示收益曲线");
            vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down,0,0,0);
        }

        setText(vh.getTextView(R.id.cp_rate),achivementsEntity.getCp_rate());
        setText(vh.getTextView(R.id.sh_rate),achivementsEntity.getSh300_rate());
        vh.get(R.id.ll_chart_switch).setOnClickListener(new OnClickImp(vh,achivementsEntity,position));

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
                AnimationHelper.collapseView(  vh.get(R.id.ll_chart),true);
                achivementsEntity.setExpend(false);
                vh.setTextView(R.id.tv_chart_switch,"显示收益曲线");
                vh.getTextView(R.id.tv_chart_switch).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down,0,0,0);

            }else{
                AnimationHelper.expandView(vh.get(R.id.ll_chart), context.getResources().getDimensionPixelOffset(R.dimen.chartViewHeight), true, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        achivementsEntity.setExpend(true);
                        vh.setTextView(R.id.tv_chart_switch,"收起收益曲线");
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
                chatView.removeAllViews();
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                chatView.addView(new CompareIndexView(context,achivementsEntity).initView(),params);
                AchivementAdapter.this.selectIndex=position;
                AchivementAdapter.this.notifyDataSetChanged();


            }

        }
    }


}
