package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

import java.util.List;


/**
 * @author zwm
 * @version 2.0
 * @ClassName FundManagerRankingAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/13.
 */
public class FundManagerRankingAdapter  extends  AutoAdapter {



    private String sortKey;

    public FundManagerRankingAdapter(Context context, List<?> list) {
        super(context, list);
    }

    public void setSortKey(String key){
        sortKey=key;
    }

    @Override
    public int setLayoutID() {
        return R.layout.item_fund_manager_ranking;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {


        FundManagerBean fundManagerBean= (FundManagerBean) list.get(position);
        vh.setTextView(R.id.tv_name,fundManagerBean.name);
        vh.setTextView(R.id.tv_join_time,fundManagerBean.work_seniority+"");

        ImageLoaderUtils.setHeanderImage(fundManagerBean.avatar_md,vh.getImageView(R.id.im_avatar));

        vh.setTextView(R.id.tv_percent_value,fundManagerBean.getValueString(sortKey));

    }


}
