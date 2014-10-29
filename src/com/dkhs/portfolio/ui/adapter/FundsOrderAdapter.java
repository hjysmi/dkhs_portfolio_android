/**
 * @Title FundsOrderAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:57:34
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.utils.StringFromatUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName FundsOrderAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:57:34
 * @version 1.0
 */
public class FundsOrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChampionBean> mDataList;

    public FundsOrderAdapter(Context context, List<ChampionBean> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int getCount() {
        if (null == mDataList)
            return 0;
        return mDataList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // View view = View.inflate(mContext, R.layout.item_funds_order_list, null);
        // return view;
        //

        ViewHolder viewHolder = null;
        View row = convertView;
        ChampionBean item = mDataList.get(position);
        if (position == 0) {
            row = View.inflate(mContext, R.layout.layout_champion, null);
            row.setTag("champion");
            TextView tvFundsName = (TextView) row.findViewById(R.id.tv_champion_combination);
            tvFundsName.setText(item.getName());

            ((TextView) row.findViewById(R.id.tv_create_user)).setText(mContext.getString(R.string.format_create_name,
                    item.getUser().getUsername()));
            ((TextView) row.findViewById(R.id.tv_value)).setText(StringFromatUtils.get2PointPercentPlus(item
                    .getIncreasePercent()));
            ((TextView) row.findViewById(R.id.tv_desc)).setText(mContext.getString(R.string.desc_format,
                    item.getDescription()));

            return row;
        }
        if (row == null || row.getTag().equals("champion")) {
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(mContext).inflate(R.layout.item_funds_order_list, null);
            viewHolder.tvCombinationName = (TextView) row.findViewById(R.id.tv_champion_combination);
            viewHolder.tvUserName = (TextView) row.findViewById(R.id.tv_create_user);
            viewHolder.tvDesc = (TextView) row.findViewById(R.id.tv_desc);
            viewHolder.tvValue = (TextView) row.findViewById(R.id.tv_value);
            viewHolder.tvIndex = (TextView) row.findViewById(R.id.tv_order_index);
            viewHolder.ivTrophy = (ImageView) row.findViewById(R.id.iv_gold_award);
            // viewHolder.ivDel = (ImageButton) row.findViewById(R.id.ib_del_conbin);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        if (position == 1) {
            viewHolder.ivTrophy.setImageResource(R.drawable.ic_sliver_award);
            viewHolder.ivTrophy.setVisibility(View.VISIBLE);
            viewHolder.tvIndex.setVisibility(View.GONE);
        } else if (position == 2) {
            viewHolder.ivTrophy.setImageResource(R.drawable.ic_bronze_award);
            viewHolder.ivTrophy.setVisibility(View.VISIBLE);
            viewHolder.tvIndex.setVisibility(View.GONE);

        } else {
            viewHolder.ivTrophy.setVisibility(View.INVISIBLE);
            viewHolder.tvIndex.setVisibility(View.VISIBLE);
            viewHolder.tvIndex.setText(position + "");
        }

        viewHolder.tvDesc.setText(mContext.getString(R.string.desc_format, item.getDescription()));
        viewHolder.tvUserName.setText(mContext.getString(R.string.format_create_name, item.getUser().getUsername()));
        viewHolder.tvValue.setText(StringFromatUtils.get2PointPercentPlus(item.getIncreasePercent()));
        viewHolder.tvCombinationName.setText(item.getName());
        return row;
    }

    public final static class ViewHolder {
        TextView tvUserName;
        TextView tvValue;
        TextView tvCombinationName;
        TextView tvDesc;
        TextView tvIndex;

        ImageView ivTrophy;

    }

}
