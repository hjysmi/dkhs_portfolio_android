package com.dkhs.portfolio.ui.selectfriend.actions;

import android.util.Log;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.Dispatcher;
import com.dkhs.portfolio.ui.widget.sortlist.PinyinComparator;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;


public class FriendSourceEngine {
    public static final String TODO_CREATE = "todo-create";
    public static final String FRIEND_NET_DATA = "friend_net_data";


    public static final String KEY_TEXT = "key-text";
    public static final String KEY_ID = "key-id";
    public static final String KEY_FRIENDLIST = "key_friendlist";


    private static FriendSourceEngine instance;
    final Dispatcher dispatcher;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    FriendSourceEngine(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        pinyinComparator = new PinyinComparator();
    }

    public static FriendSourceEngine get() {
        if (instance == null) {
            instance = new FriendSourceEngine(Dispatcher.get());
        }
        return instance;
    }


    public void create(String text) {


    }


    public void loadData() {

        new UserEngineImpl().getFriendList(String.valueOf(UserEngineImpl.getUserEntity().getId()), new ParseHttpListener<MoreDataBean<UserEntity>>() {
                    @Override
                    protected MoreDataBean<UserEntity> parseDateTask(String jsonData) {

                        MoreDataBean<UserEntity> moreBean = (MoreDataBean<UserEntity>) DataParse.parseObjectJson(new TypeToken<MoreDataBean<UserEntity>>() {
                        }.getType(), jsonData);

                        if (null != moreBean) {
                            if (null != moreBean.getResults()) {

                                for (UserEntity userEntity : moreBean.getResults()) {
                                    //汉字转换成拼音
                                    String pinyin = userEntity.getChi_spell();
                                    String sortString = pinyin.substring(0, 1).toUpperCase();

                                    // 正则表达式，判断首字母是否是英文字母
                                    if (sortString.matches("[A-Z]")) {
                                        userEntity.setSortLetters(sortString.toUpperCase());
                                    } else {
                                        userEntity.setSortLetters("#");
                                    }

                                }


                                Collections.sort(moreBean.getResults(), pinyinComparator);
                            }
                        }
                        // 根据a-z进行排序源数据
                        return moreBean;
                    }

                    @Override
                    protected void afterParseData(MoreDataBean<UserEntity> object) {
                        if (null != object) {

                            dispatcher.dispatch(
                                    FriendSourceEngine.FRIEND_NET_DATA,
                                    FriendSourceEngine.KEY_FRIENDLIST, object.getResults()
                            );


                        }
                        Log.d("afterParseData", " size:" + object.getResults().size());
                    }


                }
        );


    }


}
