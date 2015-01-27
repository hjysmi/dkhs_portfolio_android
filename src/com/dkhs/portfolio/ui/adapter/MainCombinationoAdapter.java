/**
 * @Title MainCombinationoAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-22 上午9:18:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName MainCombinationoAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-22 上午9:18:02
 * @version 1.0
 */
public class MainCombinationoAdapter extends BaseAdapter {

    private Context mContext;
    private List<CombinationBean> dataList;
    private GridView.LayoutParams mItemViewLayoutParams;

    public MainCombinationoAdapter(Context context, List<CombinationBean> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public int getCount() {

        return 2;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (dataList.size() > 0 && position < dataList.size()) {

            view = View.inflate(mContext, R.layout.item_main_combination, null);
            TextView cNameText = (TextView) view.findViewById(R.id.tv_mycombination);
            TextView cCurrentText = (TextView) view.findViewById(R.id.tv_current_value);
            TextView cAddupText = (TextView) view.findViewById(R.id.tv_addup_value);
            CombinationBean item = dataList.get(position);
            cNameText.setText(item.getName());
            float currenValue = item.getChng_pct_day();
            cCurrentText.setTextColor(ColorTemplate.getUpOrDrownCSL(currenValue));
            cCurrentText.setText(StringFromatUtils.get2PointPercentPlus(currenValue));

            float addValue = item.getAddUpValue();
            cAddupText.setTextColor(ColorTemplate.getUpOrDrownCSL(addValue));
            cAddupText.setText(StringFromatUtils.get2PointPercentPlus(addValue));
            // view.findViewById(R.id.ll_item_main).setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            //
            // System.out.println("====================item_main_combination==================");
            //
            // mContext.startActivity(CombinationDetailActivity.newIntent(mContext, dataList.get(position)));
            //
            // }
            // });
        } else {
            view = View.inflate(mContext, R.layout.add_image, null);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = PositionAdjustActivity.newIntent(mContext, null);
                    mContext.startActivity(intent);
                }
            });
        }
        view.setLayoutParams(mItemViewLayoutParams);

        return view;
    }

    public void setItemHeight(int height) {

        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, height);
        notifyDataSetChanged();

    }

}
