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
import com.dkhs.portfolio.bean.CommentBean;
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
import com.dkhs.portfolio.engine.UserTopicsCommentEngineImpl;
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
public class UserHomePageActivity extends ModelAcitivity {

    private final int MENU_FOLLOW_OR_UNFOLLOWE = 0;
    private String mUserId;
    public String mUserName;
    private boolean isMyInfo;
    private UserEntity mUserEntity;
    public List<CombinationBean> mCombinationBeans;
    public List<TopicsBean> mTopicsBeans;
    private List<CommentBean> mCommentBeans;


    private RecyclerView mRV;

    private int mCommentAmount=-1;
    private int mCombinationAmount=-1;
    private int mTopicsAcount=-1;


    private AutoRVAdapter mAutoRVAdapter;
    private List mData = new ArrayList();

    public FloatingActionMenu mLocalFloatingActionMenu;
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
        mRV = (RecyclerView) findViewById(R.id.rv);
        mLocalFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_view);
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
        mRV.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAutoRVAdapter = new CombinationUserAdapter(mContext, mData);
        mRV.setAdapter(mAutoRVAdapter);
        mLocalFloatingActionMenu.attachToRecyclerView(mRV);
        initViews();
        initData();
    }

    private void startSettingActivity() {
        startActivity(SettingActivity.getEditUserInfoIntent(this));
    }

    private void initViews() {
        mLocalFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_view);
        if (isMyInfo) {
            setTitle(getString(R.string.title_my_home_page));
            mLocalFloatingActionMenu.setVisibility(View.GONE);

        } else {
            setTitle(getString(R.string.title_ta_home_page));
            mLocalFloatingActionMenu.setVisibility(View.VISIBLE);
        }
        mLocalFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
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

        new UserCombinationEngineImpl(new LoadMoreDataEngine.ILoadDataBackListener() {

            @Override
            public void loadFinish(MoreDataBean object) {

                mCombinationBeans=object.getResults();

                mCombinationAmount=object.getTotalCount();

                updateUI();
            }

            @Override
            public void loadFail() {

            }
        }, mUserId).loadData();

    }

    public void getTopicsList(UserEntity userEntity) {
        new UserTopicsCommentEngineImpl(new LoadMoreDataEngine.ILoadDataBackListener() {
            @Override
            public void loadFinish(MoreDataBean object) {

                mTopicsBeans=new ArrayList<>();
                mTopicsAcount=object.getTotalCount();
                if(object.getResults() != null) {
                    int addAmount;
                    if (object.getResults().size() > 5) {
                        addAmount = 5;
                    } else {
                        addAmount = object.getResults().size();
                    }
                    for (int i = 0; i < addAmount; i++) {
                        TopicsBean topicsBean = (TopicsBean) object.getResults().get(i);

                        if (addAmount - 1 == i) {
                            topicsBean.compact = false;
                        } else {
                            topicsBean.compact = true;
                        }
                        mTopicsBeans.add(topicsBean);
                    }
                }
                updateUI();
            }

            @Override
            public void loadFail() {

            }
        }, mUserId, UserTopicsCommentEngineImpl.StatusType.Topics).loadData();
        new UserTopicsCommentEngineImpl(new LoadMoreDataEngine.ILoadDataBackListener() {
            @Override
            public void loadFinish(MoreDataBean object) {

                mCommentBeans=new ArrayList<>();
                mCommentAmount=object.getTotalCount();

                int  addAmount;
                if(object.getResults().size()>5){
                    addAmount=5;
                }else{
                    addAmount=object.getResults().size();
                }
                for (int i = 0; i < addAmount; i++) {
                    CommentBean commentBean=   (CommentBean) object.getResults().get(i);

                    if(addAmount-1 == i ){
                        commentBean.compact=false;
                    }else{
                        commentBean.compact=true;
                    }
                    mCommentBeans.add(commentBean);
                }
                updateUI();
            }

            @Override
            public void loadFail() {

            }
        }, mUserId, UserTopicsCommentEngineImpl.StatusType.Comment).loadData();
    }

    private void updateUI() {
        if(mUserEntity == null){
            return;
        }
        mData.clear();
        mData.add(mUserEntity);
        if(!isMyInfo) {
            updateUserFolllowInfo(mUserEntity);
        }
        MoreBean moreBean = new MoreBean();
        if (isMyInfo) {

            if(mCombinationAmount != -1){
                moreBean.title =getString(R.string.my_combination)+"("+mCombinationAmount+")";
            }else {
                moreBean.title = getString(R.string.my_combination);
            }


        } else {
            if(mCombinationAmount != -1){
                moreBean.title =getString(R.string.ta_combination)+"("+mCombinationAmount+")";
            }else {
                moreBean.title = getString(R.string.ta_combination);
            }
        }
        moreBean.index = 0;
        moreBean.userEntity = mUserEntity;

        mData.add(moreBean);
        if (mCombinationBeans != null) {
            if (mCombinationBeans.size() > 0) {
                CombinationsBean combinationsBean = new CombinationsBean();
                combinationsBean.userId = mUserEntity.getId() + "";
                combinationsBean.userName = mUserEntity.getUsername() + "";
                combinationsBean.combinationBeanList = mCombinationBeans;
                mData.add(combinationsBean);
            } else {
                NoDataBean noDataBean = new NoDataBean();
                noDataBean.noData = getString(R.string.no_combination);

                mData.add(noDataBean);

            }

        } else {
            mData.add(new LoadingBean());
        }
        MoreBean moreBean2 = new MoreBean();
        if (isMyInfo) {
            if(mTopicsAcount != -1){
                moreBean2.title =getString(R.string.my_bbs_topic)+"("+mTopicsAcount+")";
            }else{
                moreBean2.title = getString(R.string.my_bbs_topic);
            }

        } else {
            if(mTopicsAcount != -1){
                moreBean2.title =getString(R.string.ta_bbs_topic)+"("+mTopicsAcount+")";
            }else{
                moreBean2.title = getString(R.string.ta_bbs_topic);
            }
        }

        moreBean2.userEntity = mUserEntity;


        if (mTopicsBeans != null) {
            if (mTopicsBeans.size() > 5) {
                moreBean2.index = 1;
                mData.add(moreBean2);
                mData.addAll(mTopicsBeans);
                MoreFootBean moreFootBean = new MoreFootBean();
                moreFootBean.index=1;
                moreFootBean.userEntity=mUserEntity;
                mData.add(moreFootBean);
            } else {
                moreBean2.index = 0;
                mData.add(moreBean2);
                NoDataBean noDataBean = new NoDataBean();
                noDataBean.noData = getString(R.string.no_bbs_topic);
                mData.add(noDataBean);
            }
        }
        MoreBean moreBean3 = new MoreBean();
        if (isMyInfo) {
            if(mCommentAmount != -1) {
                moreBean3.title = getString(R.string.my_comment)+"("+mCommentAmount+")";
            }else{
                moreBean3.title = getString(R.string.my_comment);
            }
        } else {
            if(mCommentAmount != -1) {
                moreBean3.title = getString(R.string.ta_comment)+"("+mCommentAmount+")";
            }else{
                moreBean3.title = getString(R.string.ta_comment);
            }
        }

        moreBean3.userEntity = mUserEntity;


        if (mCommentBeans != null) {
            if (mCommentBeans.size() > 5) {
                moreBean3.index = 2;
                mData.add(moreBean3);
                mData.addAll(mCommentBeans);
                MoreFootBean moreFootBean = new MoreFootBean();
                moreFootBean.index=2;
                moreFootBean.userEntity=mUserEntity;
                mData.add(moreFootBean);
            } else {
                moreBean3.index = 0;
                mData.add(moreBean3);
                NoDataBean noDataBean = new NoDataBean();
                noDataBean.noData =  getString(R.string.no_comment);
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
                mUserEntity = object;
                getTopicsList(mUserEntity);
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
        mLocalFloatingActionMenu.removeAllItems();
        if (object.isMe_follow()) {

            mLocalFloatingActionMenu.addItem(0, R.string.unfollowing, R.drawable.btn_del_item_selector);

        } else {
            mLocalFloatingActionMenu.addItem(0, R.string.following, R.drawable.ic_add);

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
