package com.dkhs.portfolio.ui.ItemView;

import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;


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
        return R.layout.item_fundmanager_top;
    }

    @Override
    public void onBindView(ViewHolder vh, FundManagerBean data, int position) {
        FundManagerBean fundManagerBean = data;
        vh.setTextView(R.id.tv_name, fundManagerBean.name);
        vh.setTextView(R.id.tv_join_time, fundManagerBean.work_seniority + "");
        String company = fundManagerBean.fund_company;
        if(!TextUtils.isEmpty(company) && !company.equals("null")){
            vh.getTextView(R.id.tv_fund_company).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.tv_fund_company, company);
        }else{
            vh.getTextView(R.id.tv_fund_company).setVisibility(View.GONE);
        }
        Float winRate = fundManagerBean.getWinRate(sortKey);
        String winRateStr = "";
        if(winRate != null && winRate != 0){
            winRateStr = winRate.toString();
        }
        CharSequence winRateTv = StringFromatUtils.getPercentSpan(winRateStr);
        vh.setTextView(R.id.tv_win_rate_week, String.format(vh.getContext().getString(R.string.format_win_rate_week), winRateTv));
        ImageLoaderUtils.setHeanderImage(fundManagerBean.avatar_md, vh.getImageView(R.id.im_avatar));
        vh.setTextView(R.id.tv_percent_value, fundManagerBean.getValueString(sortKey));
    }
}
