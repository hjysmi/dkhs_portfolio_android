package com.dkhs.portfolio.ui;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class MyAssestsActivity extends ModelAcitivity {

    @ViewInject(R.id.tv_total_profit)
    private TextView tvTotalProfit;
    @ViewInject(R.id.tv_recent_profit)
    private TextView tvRecentProfit;
    @ViewInject(R.id.tv_total_assests)
    private TextView tvTotalAssests;

    @ViewInject(R.id.lv_assests)
    private ListView lvAssests;

    private MyAssestsAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_assests);
        ViewUtils.inject(this);
        setTitle(R.string.my_assets);
        TextView addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_setting_selecter),
                null, null, null);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 资产设置
            }
        });
        initIconResource();
        adapter = new MyAssestsAdapter();
        lvAssests.setAdapter(adapter);
        lvAssests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.print("position ....." + position);
            }
        });
    }

    private String[] titleTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.my_assests_title);
    private int[] iconRes;

    private void initIconResource() {
        TypedArray ar = mContext.getResources().obtainTypedArray(R.array.ic_assest_ids);
        int len = ar.length();
        iconRes = new int[len];
        for (int i = 0; i < len; i++) iconRes[i] = ar.getResourceId(i, 0);
        ar.recycle();
    }

    private class MyAssestsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return titleTexts.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return titleTexts[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_assests_info, null);
                holder = new ViewHolder();
                holder.ivImageDetail = (ImageView) convertView.findViewById(R.id.image_detail);
                holder.tvInfoTitle = (TextView) convertView.findViewById(R.id.tv_info_title);
                holder.tvInfoTip = (TextView) convertView.findViewById(R.id.tv_info_tip);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvInfoTitle.setText(titleTexts[position]);
            holder.tvInfoTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes[position],
                    0, 0, 0);
            return convertView;
        }

        private class ViewHolder {
            ImageView ivImageDetail;
            TextView tvInfoTitle;
            TextView tvInfoTip;
        }
    }

}
