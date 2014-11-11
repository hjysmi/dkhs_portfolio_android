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
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.ChampionBean.CombinationUser;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.FundsOrderAdapter;
import com.dkhs.portfolio.ui.adapter.UserCombinationAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
public class UserCombinationListFragment extends LoadMoreListFragment {

    private String mOrderType;
    private UserCombinationAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private UserCombinationEngineImpl dataEngine;
    private String mUserName;
    private String mUserId;

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
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Bundle bundle = getArguments();

        if (null != bundle) {
            mUserName = bundle.getString("username");
            mUserId = bundle.getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
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
        if (null != object.getResults()) {

            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == dataEngine) {
            dataEngine = new UserCombinationEngineImpl(this);
        }
        return dataEngine;
    }
    
    /**  
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {
       getLoadEngine().loadData();
    }

    @Override
    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CombinationBean cBean = mDataList.get(position);
                CombinationUser user = new ChampionBean().new CombinationUser();
                user.setId(mUserId);
                user.setUsername(mUserName);
                cBean.setCreateUser(user);

                getActivity().startActivity(OrderFundDetailActivity.getIntent(getActivity(), cBean, false));
            }
        };
    }

}