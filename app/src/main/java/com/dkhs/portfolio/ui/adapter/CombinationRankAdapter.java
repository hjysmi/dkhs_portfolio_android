/**
 * @Title FundsOrderAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:57:34
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.CombinationRankFragment;
import com.dkhs.portfolio.ui.fragment.MarketCombinationFragment;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午4:57:34
 */
public class CombinationRankAdapter extends BaseAdapter implements OnCheckedChangeListener {

    private Context mContext;
    private List<CombinationBean> mDataList;
    private String mOrderType;

    public CombinationRankAdapter(Context context, List<CombinationBean> dataList, String orderType) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mOrderType = orderType;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public int getCount() {
        if (null == mDataList)
            return 0;
        return mDataList.size();
    }

    /**
     * @param position
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param position
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // View view = View.inflate(mActivity, R.layout.item_funds_order_list, null);
        // return view;
        //

        ViewHolder viewHolder = null;
        View row = convertView;
        CombinationBean item = mDataList.get(position);
        float increasePercent = 0;
        String textResId = "";
        if (mOrderType.contains(CombinationRankFragment.ORDER_TYPE_DAY)) {
            increasePercent = item.getChng_pct_day();
            textResId = mContext.getString(R.string.day_income_rate);
        } else if (mOrderType.contains(CombinationRankFragment.ORDER_TYPE_WEEK)) {
            increasePercent = item.getChng_pct_week();
            textResId = mContext.getString(R.string.week_income_rate);
        } else if (mOrderType.contains(CombinationRankFragment.ORDER_TYPE_MONTH)) {
            textResId = mContext.getString(R.string.month_income_rate);
            increasePercent = item.getChng_pct_month();
        } else if (mOrderType.contains(CombinationRankFragment.ORDER_TYPE_ALL)) {
            textResId = mContext.getString(R.string.all_income_rate);
            increasePercent = item.getCumulative();
        }
        if (row == null || row.getTag().equals("champion")) {
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(mContext).inflate(R.layout.item_funds_order_list, parent, false);
            viewHolder.tvCombinationName = (TextView) row.findViewById(R.id.tv_champion_combination);
            viewHolder.cbFollowed = (CheckBox) row.findViewById(R.id.cb_add_follow);
            viewHolder.tvUserName = (TextView) row.findViewById(R.id.tv_create_user);
            viewHolder.tvValue = (TextView) row.findViewById(R.id.tv_value);
            viewHolder.tvIncomeText = (TextView) row.findViewById(R.id.tv_income_text);
            viewHolder.valueView = row.findViewById(R.id.layout_value);
            viewHolder.ivJiangPai = (ImageView) row.findViewById(R.id.iv_jiangpai);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.cbFollowed.setOnCheckedChangeListener(null);
        viewHolder.cbFollowed.setTag(item);

        if (!PortfolioApplication.hasUserLogin()) {
            if (null != MarketCombinationFragment.mVisitorData) {
                viewHolder.cbFollowed.setChecked(MarketCombinationFragment.mVisitorData.contains(item));
            }
        } else {

            viewHolder.cbFollowed.setChecked(item.isFollowed());
        }
        viewHolder.cbFollowed.setOnCheckedChangeListener(this);

        viewHolder.tvIncomeText.setText(textResId);

        // viewHolder.tvDesc.setText(mActivity.getString(R.string.desc_format, item.getDescription()));
        viewHolder.tvUserName.setText(mContext.getString(R.string.format_create_name, item.getUser().getUsername()));
        viewHolder.tvValue.setText(StringFromatUtils.get2Point(increasePercent));
        if (increasePercent == 0) {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_gray);
        } else if (increasePercent > 0) {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_red);
        } else {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_green);
        }

        if (position == 0) {
            viewHolder.ivJiangPai.setImageResource(R.drawable.ic_jinpai);
            viewHolder.ivJiangPai.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            viewHolder.ivJiangPai.setImageResource(R.drawable.ic_yinpai);
            viewHolder.ivJiangPai.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            viewHolder.ivJiangPai.setImageResource(R.drawable.ic_tongpai);
            viewHolder.ivJiangPai.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivJiangPai.setVisibility(View.GONE);

        }

        viewHolder.tvCombinationName.setText(item.getName());
        return row;
    }

    public final static class ViewHolder {
        TextView tvUserName;
        TextView tvValue;
        TextView tvCombinationName;
        View valueView;
        TextView tvIncomeText;
        CheckBox cbFollowed;
        ImageView ivJiangPai;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CombinationBean csBean = (CombinationBean) buttonView.getTag();
        if (!isChecked) {
            buttonView.setChecked(true);
            showDelDialog(buttonView, isChecked);
        } else {
            csBean.setFollowed(isChecked);
            updateDataList(csBean);
            delFollowCombinatio(csBean);
        }
        // if (PortfolioApplication.getInstance().hasUserLogin()) {
        //
        // } else {
        //
        // if (isChecked) {
        //
        // } else {
        //
        // }
        // }
    }

    public void showDelDialog(final CompoundButton buttonView, final boolean isChecked) {

        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        builder.setMessage(R.string.dialog_message_delfollow_combination);
        // builder.setTitle(R.string.tips);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // delFollowCombinatio();

                CombinationBean csBean = (CombinationBean) buttonView.getTag();
                csBean.setFollowed(false);
                updateDataList(csBean);
                // buttonView.setChecked(false);
                delFollowCombinatio(csBean);
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();

    }

    private void delFollowCombinatio(CombinationBean mCombinationBean) {
        if (PortfolioApplication.hasUserLogin()) {

            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            } else {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), delFollowComListener);

            }
        } else {
            if (mCombinationBean.isFollowed()) {
                new VisitorDataEngine().saveCombination(mCombinationBean);
                if (null != MarketCombinationFragment.mVisitorData
                        && !MarketCombinationFragment.mVisitorData.contains(mCombinationBean)) {
                    MarketCombinationFragment.mVisitorData.add(mCombinationBean);
                }
                PromptManager.showFollowToast();
            } else {
                new VisitorDataEngine().delCombinationBean(mCombinationBean);
                if (null != MarketCombinationFragment.mVisitorData
                        && MarketCombinationFragment.mVisitorData.contains(mCombinationBean)) {
                    MarketCombinationFragment.mVisitorData.remove(mCombinationBean);
                }
                PromptManager.showDelFollowToast();
            }
            // btnAddOptional.setEnabled(true);
            // addOptionalButton(mCombinationBean.isFollowed());
        }
    }

    private void updateDataList(CombinationBean mCombinationBean) {
        try {
            int index = mDataList.indexOf(mCombinationBean);
            mDataList.get(index).setFollowed(mCombinationBean.isFollowed());
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ParseHttpListener followComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
        }

        ;

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            // PromptManager.showToast(R.string.msg_follow_success);
            PromptManager.showFollowToast();

        }
    };
    ParseHttpListener delFollowComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
        }

        ;

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.showDelFollowToast();

        }
    };

}
