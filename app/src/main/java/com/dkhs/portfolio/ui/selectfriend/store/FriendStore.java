package com.dkhs.portfolio.ui.selectfriend.store;

import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.selectfriend.actions.EventAction;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendSourceEngine;
import com.dkhs.portfolio.ui.selectfriend.actions.FriendViewAction;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/9/1.
 */
public class FriendStore extends Store {


    private static FriendStore instance;

    private FriendViewAction viewAction;

    private List<UserEntity> mSortDateList;

    protected FriendStore(Dispatcher dispatcher) {
        super(dispatcher);
        mSortDateList = new ArrayList<UserEntity>();
    }

    public static FriendStore get() {
        if (instance == null) {
            instance = new FriendStore(Dispatcher.get());
        }
        return instance;
    }


    public List<UserEntity> getTodos() {
        return mSortDateList;
    }

    @Override
    @Subscribe
    public void onAction(EventAction action) {
        long id;
        switch (action.getType()) {
            case FriendSourceEngine.TODO_CREATE:
                String text = ((String) action.getData().get(FriendSourceEngine.KEY_TEXT));
                create(text);
                postStoreChange();
                break;
            case FriendSourceEngine.FRIEND_NET_DATA:
                List<UserEntity> userEntities = (List<UserEntity>) action.getData().get(FriendSourceEngine.KEY_FRIENDLIST);
//                create(text);
                getNetData(userEntities);
                postStoreChange();
                break;
        }
    }

    private void create(String text) {
        long id = System.currentTimeMillis();
//        Todo todo = new Todo(id, text);
//        addElement(todo);
//        Collections.sort(todos);
    }

    private void getNetData(List<UserEntity> userEntities) {
        mSortDateList.clear();
        mSortDateList.addAll(userEntities);
    }


    @Override
    StoreChangeEvent changeEvent() {
        return new FriendChangeEvent();
    }

    public class FriendChangeEvent implements StoreChangeEvent {
    }


    public void loadData() {

    }

}
