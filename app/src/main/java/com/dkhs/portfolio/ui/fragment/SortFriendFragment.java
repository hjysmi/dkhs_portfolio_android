package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendSourceEngine;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendViewAction;
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
public class SortFriendFragment extends BaseFragment implements FriendViewAction {
    @ViewInject(R.id.lv_sort_friend)
    private ListView sortListView;


    @ViewInject(R.id.sidrbar)
    private SideBar sideBar;
    @ViewInject(R.id.tv_center_index)
    private TextView tvCenterIndex;
    private SortFriendAdapter mFriendAdatper;


    private List<UserEntity> mSortDateList;

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
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDependencies();
        friendList = new ArrayList<UserEntity>();
        mSortDateList = new ArrayList<UserEntity>();
    }


    private void initDependencies() {
        todoStore = FriendStore.get();
        actionsCreator = FriendSourceEngine.get();
    }

    @Override
    public void onResume() {
        super.onResume();
        Dispatcher.get().register(todoStore);
    }

    @Override
    public void onPause() {
        super.onPause();
        Dispatcher.get().unregister(todoStore);
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
                UserEntity item = (UserEntity) mFriendAdatper.getItem(position);
                setSelectBack(item.getUsername());
            }
        });

//        mSortDateList = filledData(getResources().getStringArray(R.array.friend_date));


        mFriendAdatper = new SortFriendAdapter(getActivity(), mSortDateList);
        sortListView.setAdapter(mFriendAdatper);
        getFriendData();
    }


    private void setSelectBack(String username) {
        Intent intent = new Intent();
        intent.putExtra("select_friend", username);
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
    }


    private List<UserEntity> friendList;

    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    private void getFriendData() {

        loadData();


    }


    //添加最近5个联系人
//        for (int i = 0; i < 5; i++) {
//            SortModel sortModel = new SortModel();
//            sortModel.setName(date[i]);
//
//            sortModel.setSortLetters("*");
//
//            mSortList.add(sortModel);
//        }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */

    private void filterData(String filterStr) {
//        List<SortModel> filterDateList = new ArrayList<SortModel>();
//
//        if (TextUtils.isEmpty(filterStr)) {
//            filterDateList = mSortDateList;
//        } else {
//            filterDateList.clear();
//            for (SortModel sortModel : mSortDateList) {
//                String name = sortModel.getName();
//                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
//                    filterDateList.add(sortModel);
//                }
//            }
//        }
//
//        // 根据a-z进行排序
//        Collections.sort(filterDateList, pinyinComparator);
//        mFriendAdatper.updateListView(filterDateList);
    }

    @Override
    public void loadData() {
        actionsCreator.loadData();
    }


    @Override
    public void refresh() {

    }

    private void updateUI() {
        mFriendAdatper.updateListView(todoStore.getTodos());
    }

    @Subscribe
    public void onTodoStoreChange(FriendStore.StoreChangeEvent event) {
        updateUI();
    }

}
