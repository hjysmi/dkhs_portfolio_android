package com.dkhs.portfolio.ui.selectfriend.store;

import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.EventAction;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendSourceEngine;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/9/1.
 */
public class FriendStore extends Store {


    private static FriendStore instance;


    private List<SortUserEntity> mSortDateList;
    private List<SortUserEntity> mSearchDataList;

    protected FriendStore(Dispatcher dispatcher) {
        super(dispatcher);
        mSortDateList = new ArrayList<SortUserEntity>();
        mSearchDataList = new ArrayList<SortUserEntity>();
    }

    public static FriendStore get() {
        if (instance == null) {
            instance = new FriendStore(Dispatcher.get());
        }
        return instance;
    }


    public List<SortUserEntity> getSortLists() {
        return mSortDateList;
    }

    public List<SortUserEntity> getSearchLists() {
        return mSearchDataList;
    }

    @Override
    @Subscribe
    public void onAction(EventAction action) {

        switch (action.getType()) {

            case FriendSourceEngine.FRIEND_NET_DATA: {

                List<SortUserEntity> userEntities = (List<SortUserEntity>) action.getData().get(FriendSourceEngine.KEY_FRIENDLIST);
                setNetData(userEntities);
                postStoreChange(action.getType());
                break;
            }
            case FriendSourceEngine.FRIEND_SEARCH_DATA: {

                List<SortUserEntity> userEntities = (List<SortUserEntity>) action.getData().get(FriendSourceEngine.KEY_FRIENDLIST);
                setSearachData(userEntities);
                postStoreChange(action.getType());
                break;
            }
        }
    }


    private void setNetData(List<SortUserEntity> userEntities) {

        mSortDateList.clear();
        mSortDateList.addAll(userEntities);
    }

    private void setSearachData(List<SortUserEntity> userEntities) {

        mSearchDataList.clear();
        mSearchDataList.addAll(userEntities);
    }


    @Override
    StoreChangeEvent changeEvent(String type) {
        if (FriendSourceEngine.FRIEND_NET_DATA.equals(type)) {
            return new FriendChangeEvent();
        }
        if (FriendSourceEngine.FRIEND_SEARCH_DATA.equals(type)) {
            return new SearchChangeEvent();
        }
        return null;
    }

    public class FriendChangeEvent implements StoreChangeEvent {
    }

    public class SearchChangeEvent implements StoreChangeEvent {
    }


}
