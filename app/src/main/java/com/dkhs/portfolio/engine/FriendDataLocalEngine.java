package com.dkhs.portfolio.engine;

import android.util.Log;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.LasterUserEntity;
import com.dkhs.portfolio.bean.SortUserEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zjz on 2015/9/1.
 */
public class FriendDataLocalEngine {


    public List<SortUserEntity> searchLastestFriend() {

        DbUtils dbUtils = AppConfig.getDBUtils();
        List<SortUserEntity> friendList = new ArrayList<SortUserEntity>();
        try {

            List<LasterUserEntity> lastFriendList = dbUtils
                    .findAll(Selector.from(LasterUserEntity.class).orderBy("saveTime", true)
                                    .limit(20)
                    );

            if (null != lastFriendList && !lastFriendList.isEmpty()) {
                for (LasterUserEntity userEntity : lastFriendList) {
                    userEntity.setSortLetters("*");
                    friendList.add(userEntity);
                }
                LogUtils.d(" searchLastestFriend size:" + friendList.size());
            } else {

                LogUtils.d(" searchLastestFriend is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return friendList;

    }


    public void saveSelectFriend(final SortUserEntity userEntity) {
        new Thread() {
            public void run() {
                DbUtils db = AppConfig.getDBUtils();
                try {
                    LasterUserEntity lastUser = new LasterUserEntity(userEntity);
                    lastUser.setSaveTime(System.currentTimeMillis() / 1000);
                    db.saveOrUpdate(lastUser);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }


    public void saveAllFriend(List<SortUserEntity> friends) {
        Log.d(this.getClass().getSimpleName(), " saveAllFriend");
        DbUtils db = AppConfig.getDBUtils();
        try {
            db.saveOrUpdateAll(friends);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public List<SortUserEntity> searchFriendByKey(String key) {
        key = key.trim();
        DbUtils dbUtils = AppConfig.getDBUtils();
        List<SortUserEntity> searchUserList = new ArrayList<SortUserEntity>();
        try {
            searchUserList = dbUtils
                    .findAll(Selector
                            .from(SortUserEntity.class)
                            .where(WhereBuilder.b("username", "LIKE", "%" + key + "%")
                                    .or("chi_spell", "LIKE", "%" + key + "%")
                                    .or("chi_spell_all", "LIKE", "%" + key + "%")));

            if (null != searchUserList) {
                LogUtils.d(" searchFriendByKey size:" + searchUserList.size());
            } else {
                LogUtils.d(" searchFriendByKey is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return searchUserList;
    }


    public List<SortUserEntity> getAllFriendList() {
        DbUtils db = AppConfig.getDBUtils();
        List<SortUserEntity> list = Collections.EMPTY_LIST;
        try {
            list = db.findAll(SortUserEntity.class);

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找
        return list;
    }

}
