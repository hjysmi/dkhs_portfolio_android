package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.SearchFriendFragment;
import com.dkhs.portfolio.ui.fragment.SortFriendFragment;

/**
 * Created by zjz on 2015/8/26.
 */
public class SelectFriendActivity extends ModelAcitivity {


    private EditText etSearchKey;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_select_friend);
        setTitle(R.string.title_pickup_user);
        initViews();
    }

    private void initViews() {

        replaceSortFriendFragment();
        etSearchKey = (EditText) findViewById(R.id.filter_edit);
        etSearchKey.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    replaceSortFriendFragment();
                } else {
                    replaceSearchFragment(s.toString());
                }
            }
        });


    }

    private static final String TAG_SORTLIST = "sortlist";
    private static final String TAG_SEARCH = "search";

    private void replaceSortFriendFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment sortFragment = getSupportFragmentManager().findFragmentByTag(TAG_SORTLIST);
        if (null == sortFragment) {
            sortFragment = new SortFriendFragment();
            transaction.add(R.id.fl_friend, sortFragment, TAG_SORTLIST);
        } else {
            transaction.show(sortFragment);
        }

        if (getSupportFragmentManager().findFragmentByTag(TAG_SEARCH) != null) {
            transaction.hide(getSupportFragmentManager().findFragmentByTag(TAG_SEARCH));
        }

        transaction.commitAllowingStateLoss();

    }


    private void replaceSearchFragment(String key) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFriendFragment searchFragment = (SearchFriendFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
        if (null == searchFragment) {
            searchFragment = new SearchFriendFragment();
            transaction.add(R.id.fl_friend, searchFragment, TAG_SEARCH);
        } else {
            transaction.show(searchFragment);
        }

        searchFragment.setSearchKey(key);

        if (getSupportFragmentManager().findFragmentByTag(TAG_SORTLIST) != null) {
            transaction.hide(getSupportFragmentManager().findFragmentByTag(TAG_SORTLIST));
        }

        transaction.commitAllowingStateLoss();

    }


}
