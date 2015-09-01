package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendSourceEngine;
import com.dkhs.portfolio.ui.selectfriend.store.FriendStore;
import com.dkhs.portfolio.ui.widget.sortlist.SideBar;
import com.dkhs.portfolio.ui.widget.sortlist.SortFriendAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/8/31.
 */
public class SortFriendFragment extends BaseFragment {
    @ViewInject(R.id.lv_sort_friend)
    private ListView sortListView;


    @ViewInject(R.id.sidrbar)
    private SideBar sideBar;
    @ViewInject(R.id.tv_center_index)
    private TextView tvCenterIndex;
    private SortFriendAdapter mFriendAdatper;


    private List<SortUserEntity> mSortDateList;

    private FriendStore todoStore;
    private FriendSourceEngine actionsCreator;


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_sort_friend;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        Dispatcher.get().register(todoStore);
        Dispatcher.get().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Dispatcher.get().unregister(todoStore);
        Dispatcher.get().unregister(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDependencies();
        mSortDateList = new ArrayList<SortUserEntity>();
    }


    private void initDependencies() {
        todoStore = FriendStore.get();
        actionsCreator = FriendSourceEngine.get();
    }


    private void initViews() {
        sideBar.setTextView(tvCenterIndex);


        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendAdatper.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                SortUserEntity item = (SortUserEntity) mFriendAdatper.getItem(position);
                setSelectBack(item);
            }
        });

        mFriendAdatper = new SortFriendAdapter(getActivity(), mSortDateList);
        sortListView.setAdapter(mFriendAdatper);
        getFriendData();
    }


    private void setSelectBack(SortUserEntity user) {
        Intent intent = new Intent();
        intent.putExtra("select_friend", user.getUsername());
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
        actionsCreator.saveSelectFriend(user);
    }


    private void getFriendData() {
        actionsCreator.loadData();

    }


    private void updateUI() {
        mFriendAdatper.updateListView(todoStore.getSortLists());
    }

    @Subscribe
    public void onTodoStoreChange(FriendStore.FriendChangeEvent event) {
        updateUI();
    }

}
