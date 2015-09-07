package com.dkhs.portfolio.ui.ItemView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.utils.ImageLoaderUtils;


/**
 * @author zwm
 * @version 2.0
 * @ClassName FundManagerRankingAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/13.
 */
public class FMRankingItemHandler extends SimpleItemHandler<FundManagerBean> {


    private String sortKey;

    public FMRankingItemHandler() {

    }

    public void setSortKey(String key) {
        sortKey = key;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_fund_manager_ranking;
    }

    @Override
    public void onBindView(ViewHolder vh, FundManagerBean data, int position) {
        FundManagerBean fundManagerBean = data;
        vh.setTextView(R.id.tv_name, fundManagerBean.name);
        vh.setTextView(R.id.tv_join_time, fundManagerBean.work_seniority + "");

        ImageLoaderUtils.setHeanderImage(fundManagerBean.avatar_md, vh.getImageView(R.id.im_avatar));

        vh.setTextView(R.id.tv_percent_value, fundManagerBean.getValueString(sortKey));

    }
}