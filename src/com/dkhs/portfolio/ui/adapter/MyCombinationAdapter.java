/**
 * @Title MyCombinationAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-11 下午4:22:30
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.SlideListView.MessageItem;
import com.dkhs.portfolio.ui.widget.SlideListView;
import com.dkhs.portfolio.ui.widget.SlideView;
import com.dkhs.portfolio.ui.widget.SlideView.OnSlideListener;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName MyCombinationAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-11 下午4:22:30
 * @version 1.0
 */
public class MyCombinationAdapter extends BaseAdapter implements OnSlideListener, OnClickListener {
    private Context mContext;
    private List<SlideListView.MessageItem<CombinationBean>> mDataList;
    private LayoutInflater mInflater;

    public MyCombinationAdapter(Context context, List<SlideListView.MessageItem<CombinationBean>> datas) {
        this.mContext = context;
        this.mDataList = datas;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = View.inflate(mContext, R.layout.item_new_combination, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(this);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        MessageItem item = mDataList.get(position);
        item.slideView = slideView;
        item.slideView.shrink();

        final CombinationBean comBean = (CombinationBean) item.data;

        holder.tvTitle.setText(comBean.getName());

        float currenValue = comBean.getChng_pct_day();
        holder.tvCurrent.setTextColor(ColorTemplate.getUpOrDrownCSL(currenValue));
        holder.tvCurrent.setText(StringFromatUtils.get2PointPercentPlus(currenValue));

        float addValue = comBean.getAddUpValue();
        holder.tvAddup.setTextColor(ColorTemplate.getUpOrDrownCSL(addValue));
        holder.tvAddup.setText(StringFromatUtils.get2PointPercentPlus(addValue));

        holder.deleteHolder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                delCombination(position);

            }
        });

        return slideView;
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvCurrent;
        public TextView tvAddup;
        // public TextView tvIndex;
        // public TextView tvDesc;
        public ViewGroup deleteHolder;

        ViewHolder(View row) {
            tvTitle = (TextView) row.findViewById(R.id.tv_combin_title);
            tvCurrent = (TextView) row.findViewById(R.id.tv_mycob_curren_value);
            tvAddup = (TextView) row.findViewById(R.id.tv_mycob_add_value);
            // tvIndex = (TextView) row.findViewById(R.id.tv_combination_index);
            // tvDesc = (TextView) row.findViewById(R.id.tv_combination_desc);

            deleteHolder = (ViewGroup) row.findViewById(R.id.holder);
        }
    }

    private SlideView mLastSlideViewWithStatusOn;

    private void delCombination(int position) {
        final MessageItem item = mDataList.get(position);
        CombinationBean mCombination = (CombinationBean) item.data;
        if (PortfolioApplication.hasUserLogin()) {

            new MyCombinationEngineImpl().deleteCombination(mCombination.getId() + "", new ParseHttpListener() {

                @Override
                public void onSuccess(String result) {
                    // mCombinationAdapter.getDelPosition().clear();
                    mDataList.remove(item);
                    notifyDataSetChanged();
                    // rvConbinationAdatper.notifyDataSetChanged();
                    // rvConbinationAdatper.notifyItemRemoved(position)
                    // mAdapter.notifyDataSetChanged();
                    // upateDelViewStatus();
                }

                @Override
                public void onFailure(int errCode, String errMsg) {
                    super.onFailure(errCode, errMsg);
                    // Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
                }

                /**
                 * @Title
                 * @Description TODO: (用一句话描述这个方法的功能)
                 * @return
                 */
                @Override
                public void beforeRequest() {
                    // TODO Auto-generated method stub
                    super.beforeRequest();
                }

                /**
                 * @Title
                 * @Description TODO: (用一句话描述这个方法的功能)
                 * @return
                 */
                @Override
                public void requestCallBack() {
                    // TODO Auto-generated method stub
                    super.requestCallBack();
                    // refreshData();
                    // refresh();
                }

                @Override
                protected Object parseDateTask(String jsonData) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                protected void afterParseData(Object object) {
                    // TODO Auto-generated method stub

                }

            }.setLoadingDialog(mContext, "", false));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.holder) {
            // Log.e(TAG, "onClick v=" + v);
            // System.out.println("Delete item");

        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

}
