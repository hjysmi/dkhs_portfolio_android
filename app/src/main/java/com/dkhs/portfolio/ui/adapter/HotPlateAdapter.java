package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

public class HotPlateAdapter extends BaseAdapter {

    private Context mContext;
    private List<SectorBean> mDataList;

    public HotPlateAdapter(Context context, List<SectorBean> datalist) {

        this.mContext = context;
        this.mDataList = datalist;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        // mStockQuotesBean = mDataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_market_hotplate, null);
            viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.market_text_name);
            viewHolder.tvTextPercent = (TextView) convertView.findViewById(R.id.market_list_item_index);
            viewHolder.tvTextStockName = (TextView) convertView.findViewById(R.id.market_list_item_percent);
            viewHolder.tvLayoutTitle = (LinearLayout) convertView.findViewById(R.id.market_layout_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        SectorBean item = mDataList.get(position);
        viewHolder.tvTextName.setText(item.getAbbr_name());

        float change = item.getPercentage();
        if (change > 0) {
            viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.red));
        } else if (change == 0) {
            viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        // viewHolder.tvTextItemIndex.setText( new DecimalFormat("##0.00").format(mStockQuotesBean.currentValue));

        viewHolder.tvTextPercent.setText(StringFromatUtils.get2PointPercentPlus(change));
        viewHolder.tvTextStockName.setText(item.getTop_symbol_name());
        // viewHolder.tvLayoutTitle.setOnClickListener(new OnItemListener(position));
        return convertView;
    }

    final static class ViewHodler {
        LinearLayout tvLayoutTitle;
        TextView tvTextStockName;
        TextView tvTextName;
        TextView tvTextPercent;
    }

    class OnItemListener implements OnClickListener {
        private int position;

        public OnItemListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // SelectStockBean itemStock = mDataList.get(position);
            //
            // mContext.startActivity(StockQuotesActivity.newIntent(mContext, itemStock));
        }

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
