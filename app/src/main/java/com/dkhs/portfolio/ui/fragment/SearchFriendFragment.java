package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ListView;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.adapter.SearchFriendAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_search_friend;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchList = new ArrayList<>();
        adapterSearch = new SearchFriendAdapter(getActivity(), mSearchList);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setSearchKey(String searchKey) {
        this.mSearchKey = searchKey;
        Log.d(this.getClass().getSimpleName(), "Search key:" + searchKey);
    }
}
