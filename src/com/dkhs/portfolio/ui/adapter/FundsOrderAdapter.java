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
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.fragment.FundsOrderFragment;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @ClassName FundsOrderAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:57:34
 * @version 1.0
 */
public class FundsOrderAdapter extends BaseAdapter implements OnCheckedChangeListener {

    private Context mContext;
    private List<CombinationBean> mDataList;
    private String mOrderType;

    public FundsOrderAdapter(Context context, List<CombinationBean> dataList, String orderType) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mOrderType = orderType;
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
        CombinationBean item = mDataList.get(position);
        float increasePercent = 0;
        String textResId = "";
        if (mOrderType.contains(FundsOrderFragment.ORDER_TYPE_DAY)) {
            increasePercent = item.getChng_pct_day();
            textResId = mContext.getString(R.string.day_income_rate);
        } else if (mOrderType.contains(FundsOrderFragment.ORDER_TYPE_WEEK)) {
            increasePercent = item.getChng_pct_week();
            textResId = mContext.getString(R.string.week_income_rate);
        } else if (mOrderType.contains(FundsOrderFragment.ORDER_TYPE_MONTH)) {
            textResId = mContext.getString(R.string.month_income_rate);
            increasePercent = item.getChng_pct_month();
        } else if (mOrderType.contains(FundsOrderFragment.ORDER_TYPE_ALL)) {
            textResId = mContext.getString(R.string.all_income_rate);
            increasePercent = item.getCumulative();
        }
        if (row == null || row.getTag().equals("champion")) {
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(mContext).inflate(R.layout.item_funds_order_list, null);
            viewHolder.tvCombinationName = (TextView) row.findViewById(R.id.tv_champion_combination);
            viewHolder.cbFollowed = (CheckBox) row.findViewById(R.id.cb_add_follow);
            viewHolder.tvUserName = (TextView) row.findViewById(R.id.tv_create_user);
            viewHolder.tvValue = (TextView) row.findViewById(R.id.tv_value);
            viewHolder.tvIncomeText = (TextView) row.findViewById(R.id.tv_income_text);
            viewHolder.valueView = row.findViewById(R.id.layout_value);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.cbFollowed.setOnCheckedChangeListener(null);
        viewHolder.cbFollowed.setTag(item);

        viewHolder.cbFollowed.setChecked(item.isFollowed());
        viewHolder.cbFollowed.setOnCheckedChangeListener(this);

        viewHolder.tvIncomeText.setText(textResId);

        // viewHolder.tvDesc.setText(mContext.getString(R.string.desc_format, item.getDescription()));
        viewHolder.tvUserName.setText(mContext.getString(R.string.format_create_name, item.getUser().getUsername()));
        viewHolder.tvValue.setText(StringFromatUtils.get2PointPercent(increasePercent));
        if (increasePercent == 0) {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_gray);
        } else if (increasePercent > 0) {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_red);
        } else {
            viewHolder.valueView.setBackgroundResource(R.drawable.bg_order_green);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
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
        if (PortfolioApplication.getInstance().hasUserLogin()) {

            if (mCombinationBean.isFollowed()) {
                new FollowComEngineImpl().followCombinations(mCombinationBean.getId(), followComListener);
            } else {
                new FollowComEngineImpl().defFollowCombinations(mCombinationBean.getId(), delFollowComListener);

            }
        } else {
            if (mCombinationBean.isFollowed()) {
                new VisitorDataEngine().saveCombination(mCombinationBean);
            } else {
                new VisitorDataEngine().delCombinationBean(mCombinationBean);
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
        };

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.showToast(R.string.msg_follow_success);
            // mCombinationBean.setFollowed(!mCombinationBean.isFollowed());
            // addOptionalButton(mCombinationBean.isFollowed());
            // if (mCombinationBean.isFollowed()) {
            // addFollowSuccess();
            // } else {
            // delFollowSuccess();
            // }
            // try {
            // int index = mDataList.indexOf(object);
            // mDataList.get(index).setFollowed();
            // } catch (Exception e) {
            // e.printStackTrace();
            // }

        }
    };
    ParseHttpListener delFollowComListener = new ParseHttpListener<Object>() {

        @Override
        public void requestCallBack() {
            super.requestCallBack();
        };

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.showToast(R.string.msg_def_follow_success);
            // mCombinationBean.setFollowed(!mCombinationBean.isFollowed());
            // addOptionalButton(mCombinationBean.isFollowed());
            // if (mCombinationBean.isFollowed()) {
            // addFollowSuccess();
            // } else {
            // delFollowSuccess();
            // }
            // try {
            // int index = mDataList.indexOf(object);
            // mDataList.get(index).setFollowed();
            // } catch (Exception e) {
            // e.printStackTrace();
            // }

        }
    };

}
