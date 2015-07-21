package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.adpter.adapter.SingleAutoAdapter;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;


/**
 * @author zwm
 * @version 2.0
 * @ClassName FundManagerRankingAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/13.
 */
public class FundManagerRankingAdapter  extends SingleAutoAdapter {



    private String sortKey;

    public FundManagerRankingAdapter(Context context, List<?> list) {
        super(context, list);
    }

    public void setSortKey(String key){
        sortKey=key;
    }


    @Override
    public int getLayoutResId() {
        return  R.layout.item_fund_manager_ranking;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {
        FundManagerBean fundManagerBean= (FundManagerBean) mData.get(position);
        vh.setTextView(R.id.tv_name,fundManagerBean.name);
        vh.setTextView(R.id.tv_join_time,fundManagerBean.work_seniority+"");

        ImageLoaderUtils.setHeanderImage(fundManagerBean.avatar_md,vh.getImageView(R.id.im_avatar));

        vh.setTextView(R.id.tv_percent_value,fundManagerBean.getValueString(sortKey));

    }
}
