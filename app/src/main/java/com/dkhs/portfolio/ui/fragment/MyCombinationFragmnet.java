/**
 * @Title MyCombinationFragmnet.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 上午9:00:48
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
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

    @Override
    public int setContentLayoutId() {
        return R.layout.layout_mycombination_listview;
    }

    /**
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        dataEngine = new UserCombinationEngineImpl(this, "");

    }

    public TextView tvEmptyText;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        // mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        // mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

        // @Override
        // public void onRefresh() {
        // // TODO Auto-generated method
        //
        // refresh();
        // }
        // });
        // mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        // mAppList = getPackageManager().getInstalledApplications(0);
        tvEmptyText = (TextView) view.findViewById(R.id.add_data);
        tvEmptyText.setText(R.string.click_creat_combina);
        tvEmptyText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewCombination();

            }
        });
        SwipeMenuListView mListView = (SwipeMenuListView) view.findViewById(R.id.swipemenu_listView);
        mAdapter = new CombinationAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(tvEmptyText);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(MyCombinationFragmnet.this.getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.red_delete)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        // open(item);
                        delCombination(position);
                        // PromptManager.showToast("删除");
                        break;
                    case 1:
                        // delete
                        // delete(item);
                        // mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), position + " long click", 0).show();
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

    }

    private void delCombination(int position) {
        final CombinationBean item = mDataList.get(position);
        // CombinationBean mCombination = (CombinationBean) item.data;
        if (PortfolioApplication.hasUserLogin()) {

            showDelDialog(item);
        }
    }

    public void showDelDialog(final CombinationBean mCombination) {

        MAlertDialog builder = PromptManager.getAlertDialog(getActivity());
        builder.setMessage(R.string.dialog_message_delete_combination);
        // builder.setTitle(R.string.tips);
        // final CombinationBean mCombination = (CombinationBean) item.data;
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                new MyCombinationEngineImpl().deleteCombination(mCombination.getId() + "", new ParseHttpListener() {

                    @Override
                    public void onSuccess(String result) {
                        // mCombinationAdapter.getDelPosition().clear();
                        super.onSuccess(result);
                        mDataList.remove(mCombination);
                        mAdapter.notifyDataSetChanged();
                        // rvConbinationAdatper.notifyDataSetChanged();
                        // rvConbinationAdatper.notifyItemRemoved(position)
                        // mAdapter.notifyDataSetChanged();
                        // combinationActivity.setButtonFinish();
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

                }.setLoadingDialog(getActivity(), "", false));
                dialog.dismiss();
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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

}
