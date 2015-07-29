package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName CombinationHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class CombinationHandler implements ItemHandler<List<CombinationBean>> {
    @Override
    public int getLayoutResId() {
        return R.layout.item_combination;
    }
    @Override
    public void onBindView(ViewHolder vh, List<CombinationBean> data, int position) {
        vh.get(R.id.combination5).setVisibility(View.GONE);
        vh.get(R.id.combination4).setVisibility(View.GONE);
        vh.get(R.id.combination3).setVisibility(View.GONE);
        vh.get(R.id.combination2).setVisibility(View.GONE);
        vh.get(R.id.combination1).setVisibility(View.GONE);
        switch (data.size()) {
            default:
            case 5:
                vh.get(R.id.combination5).setVisibility(View.VISIBLE);
                CombinationBean combinationBean5=data.get(4);
                vh.setTextView(R.id.combination_name5,combinationBean5.getName());
                vh.setTextView(R.id.combination_create_date5,TimeUtils.getSimpleDay(combinationBean5.getCreateTime()));
                vh.getTextView(R.id.combination_value5).setTextColor(ColorTemplate.getUpOrDrownCSL(combinationBean5.getCumulative()));
                vh.getTextView(R.id.combination_value5).setText(StringFromatUtils.get2PointPercent(combinationBean5.getCumulative()));
            case 4:
                vh.get(R.id.combination4).setVisibility(View.VISIBLE);
                CombinationBean combinationBean4=data.get(3);
                vh.setTextView(R.id.combination_name4,combinationBean4.getName());
                vh.setTextView(R.id.combination_create_date4,TimeUtils.getSimpleDay(combinationBean4.getCreateTime()));
                vh.getTextView(R.id.combination_value4).setTextColor(ColorTemplate.getUpOrDrownCSL(combinationBean4.getCumulative()));
                vh.getTextView(R.id.combination_value4).setText(StringFromatUtils.get2PointPercent(combinationBean4.getCumulative()));
                 
            case 3:
                vh.get(R.id.combination3).setVisibility(View.VISIBLE);
                CombinationBean combinationBean3=data.get(2);
                vh.setTextView(R.id.combination_name3,combinationBean3.getName());
                vh.setTextView(R.id.combination_create_date3,TimeUtils.getSimpleDay(combinationBean3.getCreateTime()));
                vh.getTextView(R.id.combination_value3).setTextColor(ColorTemplate.getUpOrDrownCSL(combinationBean3.getCumulative()));
                vh.getTextView(R.id.combination_value3).setText(StringFromatUtils.get2PointPercent(combinationBean3.getCumulative()));
                
            case 2:
                vh.get(R.id.combination2).setVisibility(View.VISIBLE);
                CombinationBean combinationBean2=data.get(1);
                vh.setTextView(R.id.combination_name2,combinationBean2.getName());
                vh.setTextView(R.id.combination_create_date2,TimeUtils.getSimpleDay(combinationBean2.getCreateTime()));
                vh.getTextView(R.id.combination_value2).setTextColor(ColorTemplate.getUpOrDrownCSL(combinationBean2.getCumulative()));
                vh.getTextView(R.id.combination_value2).setText(StringFromatUtils.get2PointPercent(combinationBean2.getCumulative()));
               
            case 1:
                vh.get(R.id.combination1).setVisibility(View.VISIBLE);
                CombinationBean combinationBean1=data.get(0);
                vh.setTextView(R.id.combination_name1,combinationBean1.getName());
                vh.setTextView(R.id.combination_create_date1,TimeUtils.getSimpleDay(combinationBean1.getCreateTime()));
                vh.getTextView(R.id.combination_value1).setTextColor(ColorTemplate.getUpOrDrownCSL(combinationBean1.getCumulative()));
                vh.getTextView(R.id.combination_value1).setText(StringFromatUtils.get2PointPercent(combinationBean1.getCumulative()));
                break;
            case 0:

                break;
        }
    }

}
