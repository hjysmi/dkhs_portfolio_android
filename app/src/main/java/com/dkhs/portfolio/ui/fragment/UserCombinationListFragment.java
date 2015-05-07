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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.CombinationBean.CombinationUser;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.adapter.UserCombinationAdapter;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午4:03:33
 */
public class UserCombinationListFragment extends LoadMoreListFragment implements OnScrollListener {

    private String mOrderType;
    private UserCombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;
    private String mUserName;
    private String mUserId;
    private View headerView;

    private View footView;
    private float animPercent;

    /**
     * 头部高度
     */
    private int headerHeight;

    public static UserCombinationListFragment getFragment(String username, String userId) {

        UserCombinationListFragment fragment = new UserCombinationListFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mUserName = bundle.getString("username");
            mUserId = bundle.getString("userId");
        }
    }

    public ListView getListView() {
        return mListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        headerHeight = getResources().getDimensionPixelOffset(R.dimen.header_height);
        headerView = new View(getActivity());
        footView = new View(getActivity());
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
        getListView().setOnScrollListener(this);
        getListView().setSmoothScrollbarEnabled(true);
        getListView().addHeaderView(headerView);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new UserCombinationAdapter(getActivity(), mDataList);
        }
        return mAdapter;
    }

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);

        if (null != object.getResults()) {

            // mDataList = object.getResults();
            mDataList.clear();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        }

        if (object.getCurrentPage() == 1) {
            addListViewFootView();
        }

    }

    public void addListViewFootView() {


        if (null != footView.getParent()) {
            getListView().removeFooterView(footView);
        }
        int totalHeight = 0;
        totalHeight = getResources().getDimensionPixelOffset(R.dimen.combination_item_height) * (getListAdapter().getCount() - 1);
        int footHeight;

        if (totalHeight < (getListView().getHeight())) {
            footHeight = (getListView().getHeight()) - totalHeight  - getResources().getDimensionPixelOffset(R.dimen.header_can_scroll_distance);
        } else {
            footHeight =getResources().getDimensionPixelOffset(R.dimen.combination_item_height);
        }

        footView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, footHeight));
        getListView().addFooterView(footView);


    }

    @Override
    LoadMoreDataEngine getLoadEngine() {

        if (null == dataEngine) {
            dataEngine = new UserCombinationEngineImpl(this, mUserId);
        }
        return dataEngine;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadData() {
        getLoadEngine().loadData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0 || position > mDataList.size()) {
                    return;
                }

                CombinationBean cBean = mDataList.get(position - 1);
                CombinationUser user = new CombinationBean.CombinationUser();
                user.setId(mUserId);
                user.setUsername(mUserName);
                cBean.setUser(user);

                getActivity().startActivity(OrderFundDetailActivity.getIntent(getActivity(), cBean, false, null));
            }
        };
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_user_combination_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    OnRefreshListener setOnRefreshListener() {
        // TODO Auto-generated method stub
        return new OnRefreshListener() {

            @Override
            public void onRefresh() {
                getLoadEngine().loadData();
            }
        };
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {

            if (animPercent != 1 && animPercent != 0) {

                if (animPercent > 0.4f) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }

            }
        }
    }


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 0:
                    getListView().smoothScrollBy(16, 2);
                    break;
                case 1:
                    getListView().smoothScrollBy(-16, 2);
                    break;
            }

            if (animPercent < 1 && animPercent > 0) {
                handler.sendEmptyMessage(msg.what);
            }
        }
    };

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        animPercent = getValues(-headerView.getTop());


        if (firstVisibleItem > 1) {
            animPercent = 1;
        }
        if (mListener != null   ) {
            mListener.onScrollChanged(animPercent);
        }

    }

    public float getValues(int l) {
        float value = l * 1.0f / getResources().getDimensionPixelOffset(R.dimen.header_can_scroll_distance);
        value = Math.max(value, 0);
        value = Math.min(value, 1);
        return value;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private OnFragmentInteractionListener mListener;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onScrollChanged(float percent);

    }
}
