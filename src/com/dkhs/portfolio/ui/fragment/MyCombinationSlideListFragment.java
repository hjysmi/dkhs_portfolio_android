/**
 * @Title FundsOrderFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.MyCombinationAdapter;
import com.dkhs.portfolio.ui.widget.SlideListView;
import com.dkhs.portfolio.ui.widget.SlideListView.MessageItem;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version 1.0
 */
public class MyCombinationSlideListFragment extends RefreshLoadMoreSlideListFragment {

    private String mOrderType;
    // private CombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private List<SlideListView.MessageItem<CombinationBean>> mMessageList = new ArrayList<SlideListView.MessageItem<CombinationBean>>();
    private UserCombinationEngineImpl dataEngine;

    private MyCombinationActivity combinationActivity;

    private static final long mCombinationRequestTime = 1000 * 300;
    private Timer mCombinationTimer;

    // private RVMyCombinationAdapter rvConbinationAdatper;
    private MyCombinationAdapter mCombinationAdapter;

    public static MyCombinationSlideListFragment getFragment() {
        MyCombinationSlideListFragment fragment = new MyCombinationSlideListFragment();
        Bundle args = new Bundle();
        // args.putString("username", username);
        // args.putString("userId", userId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Bundle bundle = getArguments();
        combinationActivity = (MyCombinationActivity) getActivity();
        if (null != bundle) {
            // mUserName = bundle.getString("username");
            // mUserId = bundle.getString("userId");
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        // mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
        //
        // @Override
        // public void onRefresh() {
        // refresh();
        //
        // }
        // });

        // rvConbinationAdatper = new RVMyCombinationAdapter(getActivity(), mDataList);
        mCombinationAdapter = new MyCombinationAdapter(getActivity(), mMessageList);
        slideListView.setAdapter(mCombinationAdapter);
        slideListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mCombinationAdapter.getmLastSlideViewWithStatusOn() != null) {
                    // mCombinationAdapter.getmLastSlideViewWithStatusOn().shrink();
                    // return;
                    mCombinationAdapter.notifyDataSetChanged();
                    return;
                }

                startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

            }
        });
        tvEmptyText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewCombination();

            }
        });
        // rvConbinationAdatper.SetOnItemClickListener(rvMyCombinationItemListener);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mCombinationTimer == null) {
            mCombinationTimer = new Timer(true);
            mCombinationTimer.schedule(new RequestCombinationTask(), 200, mCombinationRequestTime);
        }
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mCombinationTimer != null) {
            mCombinationTimer.cancel();
            mCombinationTimer = null;
        }
    }

    public class RequestCombinationTask extends TimerTask {

        @Override
        public void run() {
            refresh();

        }
    }

    private boolean isRefresh;

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        if (null != object.getResults()) {
            if (!UIUtils.roundAble(object.getStatu())) {
                if (mCombinationTimer != null) {
                    mCombinationTimer.cancel();
                    mCombinationTimer = null;
                }
            }
            mDataList.clear();
            mMessageList.clear();
            isRefresh = false;
            mDataList.addAll(object.getResults());

            if (!mDataList.isEmpty()) {

                for (CombinationBean bean : mDataList) {
                    MessageItem item = slideListView.new MessageItem<CombinationBean>();
                    item.data = bean;
                    mMessageList.add(item);
                }
            }

            mCombinationAdapter.notifyDataSetChanged();
        }

    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == dataEngine) {
            dataEngine = new UserCombinationEngineImpl(this, "");
        }
        return dataEngine;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    public void refresh() {
        isRefresh = true;

        ((UserCombinationEngineImpl) getLoadEngine()).loadAllData();

    }

    public void setListDelStatus(boolean isDel) {
    }

    // private void showLongClickDialog(final String conId) {
    //
    // AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
    // android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
    //
    // builder.setItems(R.array.long_click_type, new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog, int which) {
    //
    // if (which == 0) {
    // // queryFromCreateDay();
    // // } else {
    // // 置于页首
    //
    // setCombinationTop(conId);
    // } else if (which == 1) {
    // // rvConbinationAdatper.setDelStatus(true);
    // // combinationActivity.setButtonCancel();
    // }
    //
    // }
    // }).setNegativeButton(R.string.cancel, null).setCancelable(false).show();
    //
    // }

    private void setCombinationTop(String conId) {
        new MyCombinationEngineImpl().setCombinationTOp(conId, setTopListener);
    }

    BasicHttpListener setTopListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            refresh();
        }

    };

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_combination_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    // OnItemClickListener rvMyCombinationItemListener = new OnItemClickListener() {
    //
    // @Override
    // public void onLongItemClick(View view, int position) {
    // showLongClickDialog(mDataList.get(position).getId());
    //
    // }
    //
    // @Override
    // public void onItemClick(View view, int position) {
    // startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));
    //
    // }
    //
    // @Override
    // public void onClickDeleteButton(int position) {
    // CombinationBean combiantinBean = mDataList.get(position);
    // showDelDialog(combiantinBean);
    //
    // }
    // };

}
