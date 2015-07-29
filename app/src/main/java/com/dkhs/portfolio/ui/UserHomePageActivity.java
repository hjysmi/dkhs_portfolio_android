/**
 * @Title UserCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.adapter.AutoRVAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.CombinationsBean;
import com.dkhs.portfolio.bean.CommendBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.MoreFootBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.UserCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.UserTopicsEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.CombinationUserAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UnFollowEvent;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zjz
 * @version 1.0
 * @ClassName UserCombinationActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-6 下午4:40:09
 */
public class UserHomePageActivity extends ModelAcitivity  {

    private final int MENU_FOLLOW_OR_UNFOLLOWE = 0;
    private String mUserId;
    public String mUserName;
    private boolean isMyInfo;
    private UserEntity mUserEntity;
    public List<CombinationBean> mCombinationBeans;
    public List<TopicsBean> mTopicsBeans;
    private List<CommendBean> mCommendBeans;


    private RecyclerView mRV;


    private AutoRVAdapter mAutoRVAdapter;
    private List mData=new ArrayList();

    public FloatingActionMenu localFloatingActionMenu;
    private UserEngineImpl userEngine;

    public static Intent getIntent(Context context, String username, String userId) {
        Intent intent = new Intent(context, UserHomePageActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("username", username);
        return intent;
    }
    private void handleExtras(Bundle extras) {
        mUserId = extras.getString("user_id");
        mUserName = extras.getString("username");
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
        setContentView(R.layout.activity_user_combination);
        mRV= (RecyclerView) findViewById(R.id.rv);
        getTitleView().setBackgroundColor(getResources().getColor(R.color.user_combination_head_bg));
        Bundle extras = getIntent().getExtras();
        userEngine = new UserEngineImpl();
        if (extras != null) {
            handleExtras(extras);
                isMyInfo = UserEntity.currentUser(mUserId);
            if (isMyInfo) {
                TextView rightBtn = getRightButton();
                rightBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_edit_selector), null,
                        null, null);
                rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startSettingActivity();
                    }
                });
            }
        }
        mRV.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAutoRVAdapter=new CombinationUserAdapter(mContext,mData);
        mRV.setAdapter(mAutoRVAdapter);
        initViews();
        initData();
    }

    private void startSettingActivity() {
                startActivity(SettingActivity.getEditUserInfoIntent(this));
    }
    private void initViews() {
        localFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_view);
        if (isMyInfo) {
            setTitle("我的主页");
            localFloatingActionMenu.setVisibility(View.GONE);

        } else {
            setTitle("Ta的主页");
            localFloatingActionMenu.setVisibility(View.VISIBLE);
        }
        localFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {

                switch (paramInt) {
                    case MENU_FOLLOW_OR_UNFOLLOWE:
                        if (null == mUserEntity) {
                            return false;
                        }
                        if (mUserEntity.isMe_follow()) {
                            unFollowAction();
                        } else {
                            followAction();
                        }
                        break;
                }
                return false;
            }
        });

//        replaceCombinationListView();

          new UserCombinationEngineImpl(new LoadMoreDataEngine.ILoadDataBackListener(){

              @Override
              public void loadFinish(MoreDataBean object) {
                  mCombinationBeans=object.getResults();
              }

              @Override
              public void loadFail() {

              }
          }, mUserId).loadData();



    }

    public void getTopcisList(UserEntity userEntity){
        new UserTopicsEngineImpl(new LoadMoreDataEngine.ILoadDataBackListener() {
            @Override
            public void loadFinish(MoreDataBean object) {
                mTopicsBeans =object.getResults();
                updateUI();
            }
            @Override
            public void loadFail() {

            }
        },userEntity.getUsername()+"",userEntity.getId()+"","2").loadData();
    }

    private void updateUI() {

        mData.clear();
        mData.add(mUserEntity);
        MoreBean moreBean=new MoreBean();
        if(isMyInfo){
            moreBean.title = "我的組合";
        }else {
            moreBean.title = "TA的組合";
        }
        moreBean.index=0;
        moreBean.userEntity=mUserEntity;

        mData.add(moreBean);
        if(mCombinationBeans != null){

            if(mCombinationBeans.size()>0) {
                CombinationsBean combinationsBean=new CombinationsBean();
                combinationsBean.userId=mUserEntity.getId()+"";
                combinationsBean.userName=mUserEntity.getUsername()+"";
                combinationsBean.combinationBeanList=mCombinationBeans;

                mData.add(combinationsBean);
            }else{
                NoDataBean noDataBean=new NoDataBean();
                noDataBean.noData="暫無組合";

                mData.add(noDataBean);

            }

        }else{
            mData.add(new LoadingBean());
        }
        MoreBean moreBean2=new MoreBean();
        if(isMyInfo) {
            moreBean2.title = "我的主貼";
        }else {
            moreBean2.title = "TA的主貼";
        }
        moreBean2.index=1;
        moreBean2.userEntity=mUserEntity;
        mData.add(moreBean2);

        if(mTopicsBeans!= null){
            if(mTopicsBeans.size()>0) {

                mData.addAll(mTopicsBeans);
                MoreFootBean moreFootBean=new MoreFootBean();
                mData.add(moreFootBean);
            }else{
                NoDataBean noDataBean=new NoDataBean();
                noDataBean.noData="暫無主貼";
                mData.add(noDataBean);

            }
        }
        mAutoRVAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMyInfo) {
            userInfoListener.setLoadingDialog(mContext);
            userEngine.getBaseUserInfo(mUserId, userInfoListener);
        }
    }





    private void initData() {

        if (!isMyInfo) {
            userInfoListener.setLoadingDialog(mContext);
            userEngine.getBaseUserInfo(mUserId, userInfoListener);
        }
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {
//                updateUserView(object);

                mUserEntity =object;

                getTopcisList(mUserEntity);
            }
        }
    };
    ParseHttpListener unfollowListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {
                updateUserFolllowInfo(object);
                PromptManager.showDelFollowToast();

            }
        }
    };
    ParseHttpListener followListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            try {
                JSONArray json = new JSONArray(jsonData);
                return DataParse.parseObjectJson(UserEntity.class, json.get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {
                //todo api 返回无me_follow 字段,所以这边手动设置为true
                object.setMe_follow(true);
                updateUserFolllowInfo(object);
                PromptManager.showFollowToast();
            }

        }
    };



    private void updateUserFolllowInfo(UserEntity object) {

        mUserEntity = object;
        localFloatingActionMenu.removeAllItems();
        if (object.isMe_follow()) {

            localFloatingActionMenu.addItem(0, R.string.unfollowing, R.drawable.btn_del_item_selector);

        } else {
            localFloatingActionMenu.addItem(0, R.string.following, R.drawable.ic_add);

        }
    }
    private void unFollowAction() {
        PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(getResources().getString(R.string.unfollow_alert_content))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unfollowListener.setLoadingDialog(mContext);
                        new UserEngineImpl().unfollow(mUserEntity.getId() + "", unfollowListener);
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
    }
    private void followAction() {
        if (!UIUtils.iStartLoginActivity(this)) {
            followListener.setLoadingDialog(mContext);
            new UserEngineImpl().follow(mUserEntity.getId() + "", followListener);
        }
    }

    @Override
    public void finish() {
        if (null != mUserEntity && !mUserEntity.isMe_follow() && UserEngineImpl.getUserEntity() != null && mUserEntity.getId() != UserEngineImpl.getUserEntity().getId()) {
            UnFollowEvent unFollowEvent = new UnFollowEvent();
            unFollowEvent.setId(mUserEntity.getId());
            BusProvider.getInstance().post(unFollowEvent);
        }
        super.finish();
    }




}