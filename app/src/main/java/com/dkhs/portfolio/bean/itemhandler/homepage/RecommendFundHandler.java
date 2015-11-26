package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.RecommendFundBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.ArrayList;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundHandler extends SimpleItemHandler<RecommendFundBean> {
    private Context mContext;
    public RecommendFundHandler(Context context){
        mContext = context;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.layout_home_recommend_fund;
    }

    @Override
    public void onBindView(ViewHolder vh, RecommendFundBean data, int position) {
        GridView fundGv = vh.get(R.id.gv_fund);
        fundGv.setAdapter(new MyAdapter(mContext,data.data));
        super.onBindView(vh, data, position);
    }

    static class MyAdapter extends BaseAdapter{
        private ArrayList<FundPriceBean> mData;
        private Context mContext;
        public MyAdapter(Context context,ArrayList<FundPriceBean> data){
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            FundPriceBean fund = mData.get(position);
            View v = View.inflate(mContext, R.layout.item_home_recommend_fund, null);
            TextView fundName = (TextView) v.findViewById(R.id.tv_fund_name);
            TextView porfitRate = (TextView) v.findViewById(R.id.tv_profit_rate);
            TextView recommendTitle = (TextView) v.findViewById(R.id.tv_recommend_title);
            TextView desc = (TextView) v.findViewById(R.id.tv_fund_desc);
            fundName.setText(fund.getAbbrname());
            porfitRate.setText(StringFromatUtils.getPercentSpan(String.valueOf(fund.getPercent_six_month())));
            recommendTitle.setText(fund.getRecommend_title());
            desc.setText(fund.getRecommend_desc());
            v.findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(FundDetailActivity.newIntent(mContext, SelectStockBean.copy(mData.get(position))));
                }
            });
            return v;
        }
    }
}
