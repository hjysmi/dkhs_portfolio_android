package com.dkhs.portfolio.ui.fragment;

import android.util.Log;

import com.dkhs.portfolio.R;

/**
 * Created by zjz on 2015/8/31.
 */
public class SearchFriendFragment extends BaseFragment {

    private String mSearchKey;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_search_friend;
    }


    public void setSearchKey(String searchKey) {
        this.mSearchKey = searchKey;
        Log.d(this.getClass().getSimpleName(), "Search key:" + searchKey);
    }
}
