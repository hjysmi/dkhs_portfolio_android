package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ManagersEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FunManagerAdapter
 * @date 2015/6/09.10:07
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FundManagerAdapter extends AutoAdapter {

    public FundManagerAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutID() {
        return R.layout.item_fund_manager;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {

        ManagersEntity bean = (ManagersEntity) list.get(position);
        vh.setTextView(R.id.tv_manamger_name, bean.getName());
        vh.setTextView(R.id.tv_manamger_day, bean.getStart_date());
        vh.getTextView(R.id.tv_income_text).setTextColor(ColorTemplate.getUpOrDrownCSL(bean.getCp_rate()));
        vh.setTextView(R.id.tv_income_text, StringFromatUtils.get2PointPercent(bean.getCp_rate()));

//        vh.setTextView(R.id.tv_user_name, peopleBean.getUsername());
//        vh.setTextView(R.id.tv_followers, context.getResources().getString(R.string.followers) + ":"
//                + StringFromatUtils.handleNumber(peopleBean.getFollowed_by_count()));
//        vh.setTextView(
//                R.id.tv_friends,
//                context.getResources().getString(R.string.following) + ":"
//                        + StringFromatUtils.handleNumber(peopleBean.getFriends_count()));

    }


}
