package com.dkhs.portfolio.ui.selectfriend.actions;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.FriendDataLocalEngine;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.widget.sortlist.PinyinComparator;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FriendSourceEngine {

    public static final String FRIEND_NET_DATA = "friend_net_data";
    public static final String FRIEND_SEARCH_DATA = "friend_search_data";


    public static final String KEY_FRIENDLIST = "key_friendlist";

    private static final int MSG_SEARCH = 600;
    private static final int MSG_REFRESH = 200;

    private static FriendSourceEngine instance;
    final Dispatcher dispatcher;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<SortUserEntity> mSortList;

    private FriendDataLocalEngine mLocalEngine;


    FriendSourceEngine(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        pinyinComparator = new PinyinComparator();
        mSortList = new ArrayList<>();
        mLocalEngine = new FriendDataLocalEngine();
    }

    public static FriendSourceEngine get() {
        if (instance == null) {
            instance = new FriendSourceEngine(Dispatcher.get());
        }
        return instance;
    }


    public void loadData() {

        new UserEngineImpl().getFriendList(String.valueOf(UserEngineImpl.getUserEntity().getId()), new ParseHttpListener<MoreDataBean<SortUserEntity>>() {
                    @Override
                    protected MoreDataBean<SortUserEntity> parseDateTask(String jsonData) {

                        MoreDataBean<SortUserEntity> moreBean = (MoreDataBean<SortUserEntity>) DataParse.parseObjectJson(new TypeToken<MoreDataBean<SortUserEntity>>() {
                        }.getType(), jsonData);

                        if (null != moreBean) {
                            List<SortUserEntity> sortList = moreBean.getResults();
                            if (null != sortList) {

                                for (SortUserEntity userEntity : sortList) {
                                    //汉字转换成拼音
                                    String pinyin = userEntity.getChi_spell();
                                    if (TextUtils.isEmpty(pinyin)) {
                                        userEntity.setSortLetters("#");
                                    } else {

                                        String sortString = pinyin.substring(0, 1).toUpperCase();

                                        // 正则表达式，判断首字母是否是英文字母
                                        if (sortString.matches("[A-Z]")) {
                                            userEntity.setSortLetters(sortString.toUpperCase());
                                        } else {
                                            userEntity.setSortLetters("#");
                                        }
                                    }
                                }

                                saveData(sortList);


                                sortList.addAll(mLocalEngine.searchLastestFriend());


                                Collections.sort(moreBean.getResults(), pinyinComparator);
                            }
                        }
                        // 根据a-z进行排序源数据
                        return moreBean;
                    }

                    @Override
                    protected void afterParseData(MoreDataBean<SortUserEntity> object) {
                        if (null != object) {

                            notifyDataRefresh(object.getResults());

                        }

                    }


                    @Override
                    public void onFailure(int errCode, String errMsg) {
                        super.onFailure(errCode, errMsg);
                        loadLocalData();
                    }
                }
        );


    }

    private void loadLocalData() {
        Message msg = new Message();
        List<SortUserEntity> sortList = mLocalEngine.getAllFriendList();
        sortList.addAll(mLocalEngine.searchLastestFriend());

        Collections.sort(sortList, pinyinComparator);
        msg.obj = sortList;
        msg.what = MSG_REFRESH;
        handler.sendMessage(msg);
    }

    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH) {

                List<SortUserEntity> list = (List<SortUserEntity>) msg.obj;
                notifyDataRefresh(list);
            } else if (msg.what == MSG_SEARCH) {
                List<SortUserEntity> list = (List<SortUserEntity>) msg.obj;
                notifyDataSearch(list);
            }
            return false;
        }
    });

    private void notifyDataRefresh(List<SortUserEntity> userList) {

        mSortList.clear();
        mSortList.addAll(userList);
        dispatcher.dispatch(
                FriendSourceEngine.FRIEND_NET_DATA,
                FriendSourceEngine.KEY_FRIENDLIST, mSortList
        );
    }

    private void notifyDataSearch(List<SortUserEntity> userList) {

        dispatcher.dispatch(
                FriendSourceEngine.FRIEND_SEARCH_DATA,
                FriendSourceEngine.KEY_FRIENDLIST, userList
        );
    }


    private void saveData(List<SortUserEntity> userList) {
        mLocalEngine.saveAllFriend(userList);
    }

    public void saveSelectFriend(SortUserEntity userEntity) {
        mLocalEngine.saveSelectFriend(userEntity);
    }


    public void searchFriendByKey(String key) {
        Message msg = new Message();
        List<SortUserEntity> sortList = mLocalEngine.searchFriendByKey(key);
        msg.obj = sortList;
        msg.what = MSG_SEARCH;
        handler.sendMessage(msg);
    }

}
