package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.RecommendFund;
import com.dkhs.portfolio.bean.RecommendFundBean;

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
        private ArrayList<RecommendFund> mData;
        private Context mContext;
        public MyAdapter(Context context,ArrayList<RecommendFund> data){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(mContext,R.layout.item_home_recommend_fund,null);
            return v;
        }
    }
}
