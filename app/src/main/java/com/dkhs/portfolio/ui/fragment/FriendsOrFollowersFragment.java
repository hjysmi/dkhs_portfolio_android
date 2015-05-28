package com.dkhs.portfolio.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.User;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.PeopleEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.CombinationUserActivity;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.adapter.FriendsOrFollowerAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UnFollowEvent;
import com.squareup.otto.Subscribe;

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
        userId= intent.getStringExtra(FriendsOrFollowersActivity.USER_ID);
        if (getTypeStr.equals(FriendsOrFollowersActivity.FRIENDS)) {
            type = PeopleEngineImpl.TYPE.FRIENDS;
        } else if (getTypeStr.equals(FriendsOrFollowersActivity.FOLLOWER)) {
            type = PeopleEngineImpl.TYPE.FOLLOWERS;
        }
        BusProvider.getInstance().register(this);

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
    public void loadData() {

        startLoadData();
        dataList.clear();
        if (null != adapter) {
            adapter .notifyDataSetChanged();
        }
        setHttpHandler(getLoadEngine().loadData());
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == adapter) {
            adapter = new FriendsOrFollowerAdapter(getActivity(), dataList);
        }
        return adapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        endLoadData();
        dataList.addAll(object.getResults());
        adapter.notifyDataSetChanged();
        if(dataList.size() == 0){

            switch (type){
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
            peopleEngine = new PeopleEngineImpl(this, type,userId);
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

                boolean isMyInfo;


                if (UserEngineImpl.getUserEntity() != null && dataList.get(position).getId() == UserEngineImpl.getUserEntity().getId()) {
                    isMyInfo = true;
                } else {
                    isMyInfo = false;
                }
                startActivity(CombinationUserActivity.getIntent(getActivity(), dataList.get(position).getUsername(),
                        dataList.get(position).getId() + "", isMyInfo));
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
//        mSwipeLayout.setRefreshing(false);
    }


    @Subscribe
    public void updateList( UnFollowEvent follow){

        switch (type) {
            case FOLLOWERS:
                break;
            case FRIENDS:
                for (int i = 0; i < dataList.size(); i++) {
                    PeopleBean item = dataList.get(i);

                    if (item.getId() == follow.getId()) {
                        dataList.remove(i);
                        adapter.notifyDataSetChanged();
                        if(dataList.size() == 0){

                            switch (type){
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