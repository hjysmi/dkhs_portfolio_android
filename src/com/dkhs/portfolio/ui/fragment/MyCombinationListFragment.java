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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;
import com.dkhs.portfolio.ui.adapter.RVMyCombinationAdapter;
import com.dkhs.portfolio.ui.adapter.RVMyCombinationAdapter.OnItemClickListener;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version 1.0
 */
public class MyCombinationListFragment extends RefreshLoadMoreListFragment {

    private String mOrderType;
    // private CombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;

    private MyCombinationActivity combinationActivity;

    private static final long mCombinationRequestTime = 1000 * 30;
    private Timer mCombinationTimer;

    private RVMyCombinationAdapter rvConbinationAdatper;

    public static MyCombinationListFragment getFragment() {
        MyCombinationListFragment fragment = new MyCombinationListFragment();
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
        if (null != rvConbinationAdatper) {

            rvConbinationAdatper.addItem();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();

            }
        });

        rvConbinationAdatper = new RVMyCombinationAdapter(getActivity(), mDataList);
        recyclerView.setAdapter(rvConbinationAdatper);
        rvConbinationAdatper.SetOnItemClickListener(rvMyCombinationItemListener);
    }

    // @Override
    // ListAdapter getListAdapter() {
    // if (mAdapter == null) {
    // mAdapter = new CombinationAdapter(getActivity(), mDataList);
    // mAdapter.setDeleteButtonClickListener(this);
    // }
    // return mAdapter;
    // }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
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
        mSwipeLayout.setRefreshing(false);
        if (null != object.getResults()) {
            if (!UIUtils.roundAble(object.getStatu())) {
                if (mCombinationTimer != null) {
                    mCombinationTimer.cancel();
                    mCombinationTimer = null;
                }
            }
            // if (isRefresh) {
            mDataList.clear();
            isRefresh = false;
            // }

            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            rvConbinationAdatper.notifyDataSetChanged();
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
        // UserCombinationEngineImpl.loadAllData(this);

    }

    public void setListDelStatus(boolean isDel) {
        rvConbinationAdatper.setDelStatus(isDel);
        // if (isDel && null != mListView) {
        // mListView.setOnItemClickListener(null);
        // }
    }

    public void removeSelectCombinations() {
        // mListView.setOnItemClickListener(this);
        // List<CombinationBean> selectList = rvConbinationAdatper.getDelPosition();
        // final List<CombinationBean> delList = new ArrayList<CombinationBean>();
        // StringBuilder sbIds = new StringBuilder();
        // for (CombinationBean delStock : selectList) {
        // // int i = index;
        // // CombinationBean delStock = mDataList.get(i);
        // delList.add(delStock);
        // sbIds.append(delStock.getId());
        // sbIds.append(",");
        // }
        // if (delList.size() > 0) {
        // // new MyCombinationEngineImpl().deleteCombination(delList.get(0).getId(), new BasicHttpListener() {
        // new MyCombinationEngineImpl().deleteCombination(sbIds.toString(), new BasicHttpListener() {
        //
        // @Override
        // public void onSuccess(String result) {
        // mAdapter.getDelPosition().clear();
        // mDataList.removeAll(delList);
        // combinationActivity.upateDelViewStatus();
        //
        // mAdapter.setDelStatus(false);
        // mAdapter.notifyDataSetChanged();
        // }
        //
        // @Override
        // public void onFailure(int errCode, String errMsg) {
        // super.onFailure(errCode, errMsg);
        // Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
        // }
        // });
        // }
    }

    private void showLongClickDialog(final String conId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        builder.setItems(R.array.long_click_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    // queryFromCreateDay();
                    // } else {
                    // 置于页首

                    setCombinationTop(conId);
                } else if (which == 1) {
                    rvConbinationAdatper.setDelStatus(true);
                    combinationActivity.setButtonCancel();
                }

            }
        }).setNegativeButton(R.string.cancel, null).setCancelable(false).show();

    }

    private void setCombinationTop(String conId) {
        new MyCombinationEngineImpl().setCombinationTOp(conId, setTopListener);
    }

    BasicHttpListener setTopListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            refresh();
        }

    };

    private void showDelDialog(final CombinationBean mCombination) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        builder.setMessage(R.string.dialog_message_delete_combination);
        // builder.setTitle(R.string.tips);

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                new MyCombinationEngineImpl().deleteCombination(mCombination.getId() + "", new ParseHttpListener() {

                    @Override
                    public void onSuccess(String result) {
                        // mCombinationAdapter.getDelPosition().clear();
                        mDataList.remove(mCombination);
                        rvConbinationAdatper.notifyDataSetChanged();
                        // rvConbinationAdatper.notifyItemRemoved(position)
                        // mAdapter.notifyDataSetChanged();
                        combinationActivity.setButtonFinish();
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
                        refresh();
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

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_combination_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    OnItemClickListener rvMyCombinationItemListener = new OnItemClickListener() {

        @Override
        public void onLongItemClick(View view, int position) {
            showLongClickDialog(mDataList.get(position).getId());

        }

        @Override
        public void onItemClick(View view, int position) {
            startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

        }

        @Override
        public void onClickDeleteButton(int position) {
            CombinationBean combiantinBean = mDataList.get(position);
            showDelDialog(combiantinBean);

        }
    };

}
