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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.ChampionBean.CombinationUser;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter;
import com.dkhs.portfolio.ui.adapter.FundsOrderAdapter;
import com.dkhs.portfolio.ui.adapter.UserCombinationAdapter;
import com.dkhs.portfolio.ui.adapter.CombinationAdapter.IDelButtonListener;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.ui.fragment.MainFragment.RequestCombinationTask;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version 1.0
 */
public class MyCombinationListFragment extends LoadMoreListFragment implements OnItemClickListener, IDelButtonListener {

    private String mOrderType;
    private CombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;
    // private String mUserName;
    // private String mUserId;
    private MyCombinationActivity combinationActivity;
    private ListView mListView;
    // 30s
    private static final long mCombinationRequestTime = 1000 * 30;
    private Timer mCombinationTimer;

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
        if (null != mAdapter) {

            mAdapter.addItem();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new CombinationAdapter(getActivity(), mDataList);
            mAdapter.setDeleteButtonClickListener(this);
        }
        return mAdapter;
    }

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
        if (null != object.getResults()) {
            if (!UIUtils.roundAble(object.getStatu())) {
                if (mCombinationTimer != null) {
                    mCombinationTimer.cancel();
                    mCombinationTimer = null;
                }
            }
            if (isRefresh) {
                mDataList.clear();
                isRefresh = false;
            }

            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
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
    OnItemClickListener getItemClickListener() {

        return this;
    }

    @Override
    public void setListViewInit(ListView listview) {
        mListView = listview;
        mListView.setDivider(null);
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showLongClickDialog(mDataList.get(position - 1).getId());
                return true;
            }
        });
    };

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // refresh();

    }

    public void refresh() {
        isRefresh = true;

        // mDataList.clear();
        ((UserCombinationEngineImpl) getLoadEngine()).loadAllData();
        // UserCombinationEngineImpl.loadAllData(this);

    }

    public void setListDelStatus(boolean isDel) {
        mAdapter.setDelStatus(isDel);
        if (isDel && null != mListView) {
            mListView.setOnItemClickListener(null);
        }
    }

    public void removeSelectCombinations() {
        mListView.setOnItemClickListener(this);
        List<CombinationBean> selectList = mAdapter.getDelPosition();
        final List<CombinationBean> delList = new ArrayList<CombinationBean>();
        StringBuilder sbIds = new StringBuilder();
        for (CombinationBean delStock : selectList) {
            // int i = index;
            // CombinationBean delStock = mDataList.get(i);
            delList.add(delStock);
            sbIds.append(delStock.getId());
            sbIds.append(",");
        }
        if (delList.size() > 0) {
            // new MyCombinationEngineImpl().deleteCombination(delList.get(0).getId(), new BasicHttpListener() {
            new MyCombinationEngineImpl().deleteCombination(sbIds.toString(), new BasicHttpListener() {

                @Override
                public void onSuccess(String result) {
                    mAdapter.getDelPosition().clear();
                    mDataList.removeAll(delList);
                    combinationActivity.upateDelViewStatus();

                    mAdapter.setDelStatus(false);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int errCode, String errMsg) {
                    super.onFailure(errCode, errMsg);
                    Toast.makeText(PortfolioApplication.getInstance(), "删除组合失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(CombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

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
                    mAdapter.setDelStatus(true);
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

                new MyCombinationEngineImpl().deleteCombination(mCombination.getId() + "", new BasicHttpListener() {

                    @Override
                    public void onSuccess(String result) {
                        // mCombinationAdapter.getDelPosition().clear();
                        mDataList.remove(mCombination);
                        mAdapter.notifyDataSetChanged();
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

                });
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

    @Override
    public void clickDeleteButton(int position) {
        CombinationBean combiantinBean = mDataList.get(position);
        // Toast.makeText(this, "Is del :" + combiantinBean.getName(), Toast.LENGTH_SHORT).show();
        showDelDialog(combiantinBean);

    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_combination_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

}
