/**
 * @Title FundsOrderFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.CombinationUserActivity;
import com.dkhs.portfolio.ui.FloatingActionMenu;
import com.dkhs.portfolio.ui.adapter.UserCombinationAdapter;
import com.lidroid.xutils.http.HttpHandler;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午4:03:33
 */
public class UserCombinationListFragment extends LoadMoreNoRefreshListFragment  {

    private String mOrderType;
    private UserCombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;
    private String mUserName;
    private String mUserId;
    private View headerView;

    private View footView;

    private float animPercent;


    private HttpHandler mHttpHandler;


    public static UserCombinationListFragment getFragment( String userId) {

        UserCombinationListFragment fragment = new UserCombinationListFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mUserId = bundle.getString("userId");
        }
    }


    public ListView getListView() {
        return mListView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int headerHeight = getResources().getDimensionPixelOffset(R.dimen.header_height);
        headerView = new View(getActivity());
        footView = new View(getActivity());
        super.onViewCreated(view, savedInstanceState);
    }


    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new UserCombinationAdapter(getActivity(), mDataList);
        }
        return mAdapter;
    }

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);


        if (null != object.getResults()) {

            // mDataList = object.getResults();
            mDataList.clear();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        }



    }




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
    public void loadData() {
        getLoadEngine().loadData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        loadData();
    }


    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0 || position > mDataList.size()) {
                    return;
                }

                if (((CombinationUserActivity) getActivity()).mUserName != null) {
                    CombinationBean cBean = mDataList.get(position - 1);
                    UserEntity user = new UserEntity();
                    user.setId(Integer.parseInt(mUserId));
                    user.setUsername(((CombinationUserActivity) getActivity()).mUserName);
                    cBean.setUser(user);
                    startActivity(CombinationDetailActivity.newIntent(getActivity(), cBean));
//                getActivity().startActivity(NewCombinationDetailActivity.newIntent(getActivity(), cBean, false, null));
                }
            }

        };
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_user_combination_list);



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {

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

    public void onLoadMore() {
        if (null != getLoadEngine()) {
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                return;
            }
            setHttpHandler(getLoadEngine().loadMore());
        }
    }


    public interface OnFragmentInteractionListener {
        public void onScrollChanged(float percent);

    }

    public HttpHandler getHttpHandler() {
        return mHttpHandler;
    }

    public void setHttpHandler(HttpHandler mHttpHandler) {
        if (null != this.mHttpHandler) {
            this.mHttpHandler.cancel();
        }
        this.mHttpHandler = mHttpHandler;
    }


}
