/**
 * @Title MyCombinationFragmnet.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 上午9:00:48
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.widget.BoundListView;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MyCombinationFragmnet
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-3-30 上午9:00:48
 */
public class MyCombinationFragmnet extends VisiableLoadFragment implements ILoadDataBackListener {
    // private List<ApplicationInfo> mAppList;
    private CombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;

    // public SwipeRefreshLayout mSwipeLayout;
    private static final String TAG = MyCombinationFragmnet.class.getSimpleName();

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_mycombination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataEngine = new UserCombinationEngineImpl(this, "");

    }

    public TextView tvEmptyText;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmptyText = (TextView) view.findViewById(R.id.add_data);
        tvEmptyText.setText(R.string.click_creat_combina);
        tvEmptyText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewCombination();

            }
        });
        BoundListView mListView = (BoundListView) view.findViewById(R.id.swipemenu_listView);

        mAdapter = new CombinationAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(tvEmptyText);


        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));
                startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

            }
        });
    }

    @Override
    public void requestData() {
        refresh();
    }


    class CombinationAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public CombinationBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_new_combination, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();

            final CombinationBean comBean = mDataList.get(position);
            holder.tvTitle.setText(comBean.getName());

            float currenValue = comBean.getChng_pct_day();
            holder.tvCurrent.setTextColor(ColorTemplate.getUpOrDrownCSL(currenValue));
            holder.tvCurrent.setText(StringFromatUtils.get2PointPercentPlus(currenValue));

            float addValue = comBean.getCumulative();
            holder.tvAddup.setTextColor(ColorTemplate.getUpOrDrownCSL(addValue));
            holder.tvAddup.setText(StringFromatUtils.get2PointPercentPlus(addValue));

            return convertView;
        }

        class ViewHolder {
            public TextView tvTitle;
            public TextView tvCurrent;
            public TextView tvAddup;

            public ViewHolder(View row) {
                tvTitle = (TextView) row.findViewById(R.id.tv_combin_title);
                tvCurrent = (TextView) row.findViewById(R.id.tv_mycob_curren_value);
                tvAddup = (TextView) row.findViewById(R.id.tv_mycob_add_value);

                // iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                // tv_name = (TextView) view.findViewById(R.id.tv_name);
                row.setTag(this);
            }
        }
    }


    private static final long mCombinationRequestTime = 1000 * 30;
    private Timer mCombinationTimer;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onViewHide() {
        super.onViewHide();
        if (mCombinationTimer != null) {
            mCombinationTimer.cancel();
            mCombinationTimer = null;
        }
    }

    @Override
    public void onViewShow() {
        super.onViewShow();
        if (mCombinationTimer == null) {
            mCombinationTimer = new Timer(true);
            mCombinationTimer.schedule(new RequestCombinationTask(), 20, mCombinationRequestTime);
        }
    }

    public class RequestCombinationTask extends TimerTask {

        @Override
        public void run() {

            Log.e(TAG, "RequestCombinationTask run");
            refresh();

        }
    }

    WeakHandler uiHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 777:
                    startLoadData();
                    break;
                case 888:
                    endLoadData();
                    break;

                default:
                    break;
            }
            return false;
        }
    });


    public void refresh() {

        Log.e(TAG, "refresh");
        uiHandler.sendEmptyMessage(777);
        isRefresh = true;

        dataEngine.loadAllData();

    }

    private boolean isRefresh;

    /**
     * @param object
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFinish(MoreDataBean object) {
        // mSwipeLayout.setRefreshing(false);
        uiHandler.sendEmptyMessage(888);
        if (null != object.getResults()) {
            if (!UIUtils.roundAble(object.getStatu())) {
                if (mCombinationTimer != null) {
                    mCombinationTimer.cancel();
                    mCombinationTimer = null;
                }
            }
            mDataList.clear();
            isRefresh = false;
            mDataList.addAll(object.getResults());

            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {
        isRefresh = false;
    }

    private void startLoadData() {
        if (getActivity() instanceof MyCombinationActivity) {
            ((MyCombinationActivity) getActivity()).rotateRefreshButton();
        }
    }

    private void endLoadData() {
        if (getActivity() instanceof MyCombinationActivity) {
            ((MyCombinationActivity) getActivity()).stopRefreshAnimation();
        }
    }

    public void createNewCombination() {
        if (mDataList.size() >= 20) {
            PromptManager.showShortToast(R.string.more_combination_tip);
        } else {
            startActivity(PositionAdjustActivity.newIntent(getActivity(), null));
        }

    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_my_combination;
    }

}
