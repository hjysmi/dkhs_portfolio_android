package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.baidu.mobstat.StatService;
import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.PeopleEngineImpl;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.ItemView.FollowerItemHandler;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UnFollowEvent;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsFragment
 * @date 2015/4/23.13:27
 * @Description (查看ta 关注的人和关注他的人 复合Fragment)
 */
public class FriendsOrFollowersFragment extends LoadMoreNoRefreshListFragment {

    private List<PeopleBean> dataList = new ArrayList<PeopleBean>();
    private PeopleEngineImpl peopleEngine = null;
    private PeopleEngineImpl.TYPE type;
    private BaseAdapter adapter;
    private String userId;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent intent = getActivity().getIntent();
        String getTypeStr = intent.getStringExtra(FriendsOrFollowersActivity.KEY);
        userId = intent.getStringExtra(FriendsOrFollowersActivity.USER_ID);
        if (getTypeStr.equals(FriendsOrFollowersActivity.FRIENDS)) {
            type = PeopleEngineImpl.TYPE.FRIENDS;
        } else if (getTypeStr.equals(FriendsOrFollowersActivity.FOLLOWER)) {
            type = PeopleEngineImpl.TYPE.FOLLOWERS;
        }
        BusProvider.getInstance().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (type == PeopleEngineImpl.TYPE.FRIENDS) {
            StatService.onPageStart(getActivity(), UIUtils.getResString(getActivity(),R.string.statistics_friends));
            MobclickAgent.onPageStart(UIUtils.getResString(getActivity(), R.string.statistics_friends));
        }else if(type == PeopleEngineImpl.TYPE.FOLLOWERS){
            StatService.onPageStart(getActivity(), UIUtils.getResString(getActivity(),R.string.statistics_followers));
            MobclickAgent.onPageStart(UIUtils.getResString(getActivity(), R.string.statistics_followers));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (type == PeopleEngineImpl.TYPE.FRIENDS) {
            StatService.onPageEnd(getActivity(), UIUtils.getResString(getActivity(), R.string.statistics_friends));
            MobclickAgent.onPageEnd(UIUtils.getResString(getActivity(), R.string.statistics_friends));
        }else if(type == PeopleEngineImpl.TYPE.FOLLOWERS){
            StatService.onPageEnd(getActivity(), UIUtils.getResString(getActivity(), R.string.statistics_followers));
            MobclickAgent.onPageEnd(UIUtils.getResString(getActivity(), R.string.statistics_followers));
        }
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        loadData();
        super.onStart();
    }


    @Override
    public void loadData() {
        startLoadData();
        dataList.clear();
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
        setHttpHandler(getLoadEngine().loadData());
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == adapter) {
//            adapter = new FollowerItemHandler(getActivity(), dataList);
            adapter = new DKBaseAdapter(getActivity(), dataList).buildSingleItemView(new FollowerItemHandler(getActivity()));
        }
        return adapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);

        if (!isAdded()) {
            return;
        }
        endLoadData();
        dataList.addAll(object.getResults());
        adapter.notifyDataSetChanged();
        if (dataList.size() == 0) {

            switch (type) {
                case FOLLOWERS:
                    setEmptyText(R.string.nodata_follower);
                    break;
                case FRIENDS:
                    setEmptyText(R.string.nodata_friend);
                    break;
            }

        }

    }

    @Override
    LoadMoreDataEngine getLoadEngine() {

        if (null == peopleEngine) {
            peopleEngine = new PeopleEngineImpl(this, type, userId);
        }
        return peopleEngine;
    }

    //    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        };
    }


    @Override
    AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                startActivity(UserHomePageActivity.getIntent(getActivity(), dataList.get(position).getUsername(),
                        dataList.get(position).getId() + ""));
            }
        };
    }

    private void startLoadData() {
        if (getActivity() instanceof FriendsOrFollowersActivity) {
            ((FriendsOrFollowersActivity) getActivity()).rotateRefreshButton();
        }
    }

    private void endLoadData() {
        if (getActivity() instanceof FriendsOrFollowersActivity) {
            ((FriendsOrFollowersActivity) getActivity()).stopRefreshAnimation();
        }
    }

    @Override
    public void loadFail() {
        endLoadData();

    }


    @Subscribe
    public void updateList(UnFollowEvent follow) {

        switch (type) {
            case FOLLOWERS:
                break;
            case FRIENDS:
                for (int i = 0; i < dataList.size(); i++) {
                    PeopleBean item = dataList.get(i);

                    if (item.getId() == follow.getId()) {
                        dataList.remove(i);
                        adapter.notifyDataSetChanged();
                        if (dataList.size() == 0) {

                            switch (type) {
                                case FOLLOWERS:
                                    setEmptyText(R.string.nodata_follower);
                                    break;
                                case FRIENDS:
                                    setEmptyText(R.string.nodata_friend);
                                    break;
                            }

                        }

                        break;
                    }

                }
                break;
        }
    }
}