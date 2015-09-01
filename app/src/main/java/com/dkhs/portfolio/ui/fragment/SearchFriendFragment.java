package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ListView;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.adapter.SearchFriendAdapter;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendSourceEngine;
import com.dkhs.portfolio.ui.selectfriend.store.FriendStore;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/8/31.
 */
public class SearchFriendFragment extends BaseFragment {

    private String mSearchKey;
    @ViewInject(R.id.lv_friend)
    private ListView lvSearch;
    private SearchFriendAdapter adapterSearch;

    private List<UserEntity> mSearchList;
    //    private FriendSourceEngine actionsCreator;
    private FriendStore todoStore;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_search_friend;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchList = new ArrayList<>();

//        actionsCreator = FriendSourceEngine.get();
        todoStore = FriendStore.get();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                SortUserEntity item = (SortUserEntity) adapterSearch.getItem(position);
                setSelectBack(item);
            }
        });

        adapterSearch = new SearchFriendAdapter(getActivity(), mSearchList);
        lvSearch.setAdapter(adapterSearch);
        if (!TextUtils.isEmpty(this.mSearchKey)) {
            setSearchKey(this.mSearchKey);
        }
    }


    private void setSelectBack(SortUserEntity user) {
        Intent intent = new Intent();
        intent.putExtra("select_friend", user.getUsername());
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
        FriendSourceEngine.get().saveSelectFriend(user);
    }

    @Override
    public void onResume() {
        super.onResume();
        Dispatcher.get().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        Dispatcher.get().unregister(this);
    }

    public void setSearchKey(String searchKey) {
        this.mSearchKey = searchKey;
        FriendSourceEngine.get().searchFriendByKey(searchKey);
        Log.d(this.getClass().getSimpleName(), "Search key:" + searchKey);
    }

    private void updateUI() {
        adapterSearch.updateData(todoStore.getSearchLists());
    }

    @Subscribe
    public void onTodoStoreChange(FriendStore.SearchChangeEvent event) {
        updateUI();
    }
}
