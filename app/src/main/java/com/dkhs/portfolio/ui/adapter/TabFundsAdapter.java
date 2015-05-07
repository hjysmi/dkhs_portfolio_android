/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FollowComListEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TabFundTitleChangeEvent;
import com.dkhs.portfolio.ui.eventbus.TabStockTitleChangeEvent;
import com.dkhs.portfolio.ui.fragment.TabStockFragment;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class TabFundsAdapter extends BaseAdapter {

    public Context mContext;
    public List<CombinationBean> mDataList = new ArrayList<CombinationBean>();

    public TabFundsAdapter(Context context, List<CombinationBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_optional_stock_price, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvPercentValue = (TextView) convertView.findViewById(R.id.tv_percent_value);
            // viewHolder.tvIncearseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final CombinationBean item = mDataList.get(position);
        viewHolder.tvStockName.setText(item.getName());
        viewHolder.tvStockNum.setText(item.getUser().getUsername());
        viewHolder.tvCurrentValue.setTextColor(ColorTemplate.getTextColor(R.color.black));
        viewHolder.tvCurrentValue.setText(StringFromatUtils.get4Point(item.getNetvalue()));

        float precentValue = 0;
        if (tabIndex == 0) { // 显示日收益率
            precentValue = item.getChng_pct_day();
        } else if (tabIndex == 1) {// 显示周收益率
            precentValue = item.getChng_pct_week();

        } else { // 显示月收益率
            precentValue = item.getChng_pct_month();

        }

        viewHolder.tvPercentValue.setBackgroundColor(ColorTemplate.getUpOrDrowBgColor(precentValue));
        viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercentPlus(precentValue));
        viewHolder.tvPercentValue.setOnClickListener(percentClick);

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;

        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvPercentValue;
        // TextView tvIncearseValue;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
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

    int tabIndex = 0;
    // private static final int updat

    private OnClickListener percentClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // change percent value
            tabIndex++;
            tabIndex = tabIndex % 3;
            if (tabIndex == 0) {
                BusProvider.getInstance().post(new TabFundTitleChangeEvent(FollowComListEngineImpl.ORDER_DAY_UP));
                // PromptManager.showToast("Change tab text to:日收益");

            } else if (tabIndex == 1) {
                // PromptManager.showToast("Change tab text to:周收益");
                BusProvider.getInstance().post(new TabFundTitleChangeEvent(FollowComListEngineImpl.ORDER_WEEK_UP));

            } else {
                // PromptManager.showToast("Change tab text to:月收益");
                BusProvider.getInstance().post(new TabFundTitleChangeEvent(FollowComListEngineImpl.ORDER_MONTH_UP));

            }

            notifyDataSetChanged();

        }
    };
}
