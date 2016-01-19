package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.bean.itemhandler.LikePeopleHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsDetailHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommentHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.LoadingHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.RewardAdoptedHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.RewardReplyBarHandler;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RewardDetailRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.TopicStateEvent;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.dkhs.portfolio.ui.fragment.TopicDetailFragment;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.ui.widget.TopicsDetailListView;
import com.dkhs.portfolio.ui.widget.TopicsDetailScrollView;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mingle.autolist.AutoData;
import com.mingle.autolist.AutoList;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.File;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 主贴详情,悬赏详情
 */
public class TopicsDetailActivity extends ModelAcitivity implements SwitchLikeStateHandler.StatusChangeI, TopicDetailFragment.OnFragmentInteractionListener, LoadMoreDataEngine.ILoadDataBackListener {
    /**
     * 话题类型
     */
    public static final int TYPE_TOPIC = 0;
    /**
     * 新闻类型
     */
    public static final int TYPE_NEWS = 10;
    /**
     * 公告类型
     */
    public static final int TYPE_ANNOUNCE = 20;

    /**
     * 研报类型
     */
    public static final int TYPE_REPORT = 30;

    /**
     * 悬赏类型
     */
    public static final int TYPE_REWARD = 40;

    /**
     * 专题理财
     */
    public static final int TYPE_SPECIAL = 50;

    public static final int MENU_COMMEND = 0;
    public static final int MENU_LIKE = 1;
    public static final int MENU_SHARE = 2;
    public static final int MENU_MORE = 3;
    public static final int MENU_MORE_GO_HOME = 4;
    public static final int MENU_MORE_STATUS_REPORT = 5;
    public static final int MENU_MORE_STATUS_DELETE = 6;

    private Boolean mScrollToComment;
    @ViewInject(R.id.ignoreTV)
    private TextView ignoreTV;
    private TopicsBean mTopicsBean;
    private AutoList<Object> mDataList = new AutoList<>().applyAction(CommentBean.class);
    private TopicsCommendEngineImpl mTopicsCommendEngine = null;
    private DKBaseAdapter mAdapter;
    @ViewInject(R.id.srl)
    private SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.lv)
    private TopicsDetailListView mTopicsDetailListView;
    @ViewInject(R.id.tsv)
    private TopicsDetailScrollView topicsDetailScrollView;
    /*    @ViewInject(R.id.adopt_reply_rl)
        private RelativeLayout mAdoptRl;*/
    TopicsCommendEngineImpl.SortType mSortType;
    CommentHandler mHandler;


    private TopicsDetailHandler mRewardDetailHandler = new TopicsDetailHandler(this);
    private RewardReplyBarHandler mRewardReplyBarHandler = new RewardReplyBarHandler(this);

    private RewardAdoptedHandler mRewardAdoptedHandler;

    public static void startActivity(Context context, TopicsBean topicsBean) {
        startActivity(context, topicsBean, false);
    }

    public static void startActivity(Context context, int id) {
        TopicsBean topicsBean = new TopicsBean();
        topicsBean.id = id;
        startActivity(context, topicsBean, false);
    }

    public static void startActivity(Context context, String id) {

        if (id.matches("\\d+"))
            TopicsDetailActivity.startActivity(context, Integer.parseInt(id));
    }

    public static void startActivity(Context context, TopicsBean topicsBean, boolean scrollToComment) {
        Intent intent = new Intent(context, TopicsDetailActivity.class);
        intent.putExtra("topicsBean", Parcels.wrap(topicsBean));
        //在子类的fragment中有使用到
        intent.putExtra("scrollToComment", scrollToComment);
        context.startActivity(intent);
    }

    @ViewInject(R.id.floating_action_view)
    public FloatingActionMenu mFloatingActionMenu;
    private TopicsBean mItemTopicsBean;
    private SwitchLikeStateHandler mSwitchLikeStateHandler;

    private boolean isMyTopics = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
            mItemTopicsBean = mTopicsBean;
            mScrollToComment = getIntent().getBooleanExtra("scrollToComment", false);
            setContentView(R.layout.activity_topics_detail);

//            Toolbar toolbar= (Toolbar) findViewById(R.id.tool);
//            setSupportActionBar(toolbar);
            ViewUtils.inject(this);
            mSwipeLayout.setColorSchemeResources(R.color.theme_blue);


            initData();
//            setTopicsDetail();
            ignoreTV.setText(mTopicsBean.text);
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(mTopicsBean);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            loadData();
            mFloatingActionMenu.attachToListViewTop(mTopicsDetailListView, null, null);
            BusProvider.getInstance().register(this);
            mTopicsDetailListView.setFocusable(false);
        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void setRewardAdopted() {
        if (mTopicsBean.reward_state == 2) {
            StatusEngineImpl.getAdoptedReply(String.valueOf(mTopicsBean.getId()), new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return CommentBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
                    if (object == null)
                        return;
                    findViewById(R.id.adopt_reply_rl).setVisibility(View.VISIBLE);
                    CommentBean comment = (CommentBean) object;
                    if (mRewardAdoptedHandler == null) {
                        mRewardAdoptedHandler = new RewardAdoptedHandler(TopicsDetailActivity.this, true, true);
                    }
                    mRewardAdoptedHandler.onBindView(ViewHolder.newInstant(findViewById(R.id.adopt_reply_rl)), comment, 0);
                }

                @Override
                protected Object parseDateTask(String jsonData) {
                    MoreDataBean<CommentBean> moreBean = null;
                    if (!TextUtils.isEmpty(jsonData)) {

                        try {
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CommentBean>>() {
                            }.getType());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (moreBean != null && moreBean.getResults() != null && moreBean.getResults().size() > 0) {
                        return moreBean.getResults().get(0);
                    }
                    return null;
                }

                @Override
                public void onFailure(int errCode, String errMsg) {
                    super.onFailure(errCode, errMsg);
                }
            });
        }
    }

    @Subscribe
    public void refresh(TopicsDetailRefreshEvent topicsDetailRefreshEvent) {
        loadData(topicsDetailRefreshEvent.sortType);
        mSortType = topicsDetailRefreshEvent.sortType;
    }

    /**
     * 点击采纳回答成功后更新界面
     *
     * @param event
     */
    @Subscribe
    public void refresh(RewardDetailRefreshEvent event) {
        mTopicsBean.reward_state = 2;
        mHandler.setRewardState(mTopicsBean.reward_state);
        setTopicsDetail();
        initFloatMenu();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        setRewardAdopted();
        BusProvider.getInstance().post(new TopicStateEvent(mTopicsBean.id, TopicStateEvent.REWARDED));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mTopicsDetailListView.setFocusableInTouchMode(true);
            mTopicsDetailListView.setFocusable(true);
        }
    }

    public void loadData(TopicsCommendEngineImpl.SortType sortType) {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData(sortType);
    }

    public void loadData() {

        mSwipeLayout.setRefreshing(true);
        BaseInfoEngine.getTopicsDetail(mTopicsBean.id + "", new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return TopicsBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                mTopicsBean = (TopicsBean) object;
                mHandler.setRewardUserId(mTopicsBean.getUser().getId());
                mHandler.setRewardState(mTopicsBean.reward_state);
                onFragmentInteraction(mTopicsBean);
                setTopicsDetail();
                if (mScrollToComment) {

                    topicsDetailScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollToComment = false;
                            topicsDetailScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            setRewardAdopted();
                        }
                    }, 0);
                } else {
                    setRewardAdopted();

                }
                mSwipeLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                mSwipeLayout.setRefreshing(false);
            }


        });
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData();

    }


    private void setTopicsDetail() {
        mRewardDetailHandler.onBindView(ViewHolder.newInstant(findViewById(R.id.topicDetailRl)), mTopicsBean, 0);
        setReplyBar();
    }

    private void setReplyBar() {
        if (mTopicsBean.content_type == TYPE_REWARD) {
            mRewardReplyBarHandler.setBarType(RewardReplyBarHandler.REWARD_BAR);
        } else {
            mRewardReplyBarHandler.setBarType(RewardReplyBarHandler.TOPIC_BAE);
        }
        mRewardReplyBarHandler.onBindView(ViewHolder.newInstant(findViewById(R.id.ll_reply_bar)), mTopicsBean, 0);
    }


    TopicsCommendEngineImpl getLoadEngine() {
        if (mTopicsCommendEngine == null) {
            mTopicsCommendEngine = new TopicsCommendEngineImpl(this, mTopicsBean.id + "");
        }
        return mTopicsCommendEngine;
    }


    /**
     * iniView initData
     */
    public void initData() {
        initFloatMenu();
        mDataList.setup(this);

        mTopicsDetailListView.setOnLoadMoreListener(new TopicsDetailListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                    return;
                }
                mTopicsDetailListView.toggleFooter(true);
                getLoadEngine().loadMore();
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mHandler = new CommentHandler(this, true, true, false);
        mAdapter = new DKBaseAdapter(this, mDataList)
                .buildMultiItemView(TopicsBean.class, new TopicsDetailHandler(this))
                .buildMultiItemView(CommentBean.class, mHandler)
                .buildMultiItemView(NoDataBean.class, new NoDataHandler())
                .buildMultiItemView(LoadingBean.class, new LoadingHandler())
                .buildMultiItemView(UserEntity.class, new LikePeopleHandler(this))
                .buildMultiItemView(PeopleBean.class, new LikePeopleHandler(this));

        mTopicsDetailListView.setAdapter(mAdapter);
        mDataList.setAdapter(mAdapter);
        mDataList.add(new LoadingBean());
        mDataList.setActionHandler(new AutoList.ActionHandler<AutoData>() {
            @Override
            public boolean beforeHandleAction(AutoData a) {

                if (a instanceof CommentBean) {
                    mTopicsBean.comments_count = mDataList.size();
                    setReplyBar();
                    switch (a.action) {

                        case Add:
                            if (mDataList.size() > 0 && (mDataList.get(0) instanceof NoDataBean)) {
                                mDataList.remove(0);
                            }


                    }
                    if (mSortType == TopicsCommendEngineImpl.SortType.like) {//防止当前为赞tab时autoList中再添加commentBean数据
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void afterHandleAction(AutoData a) {
                if (a instanceof CommentBean) {
                    mTopicsBean.comments_count = mDataList.size();
                    setReplyBar();
                    switch (a.action) {
                        case Delete:
                            if (mDataList.size() == 0) {
                                mDataList.add(new NoDataBean());
                            }
                    }
                }

            }
        });
        mFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {
                switch (paramInt) {
                    case MENU_COMMEND:

                        if (UIUtils.iStartLoginActivity(TopicsDetailActivity.this)) {
                            break;
                        }
                        if (null != mTopicsBean && null != mTopicsBean.user) {
                            if (mTopicsBean.content_type == TYPE_REWARD) {
                                startActivity(PostTopicActivity.getIntent(TopicsDetailActivity.this,
                                        PostTopicActivity.TYPE_COMMENT_REWARD, mTopicsBean.id + "", mTopicsBean.user.getUsername()));
                            } else {
                                startActivity(PostTopicActivity.getIntent(TopicsDetailActivity.this,
                                        PostTopicActivity.TYPE_COMMENT_TOPIC, mTopicsBean.id + "", mTopicsBean.user.getUsername()));
                            }
                        }
                        break;
                    case MENU_LIKE:

                        mSwitchLikeStateHandler.toggleLikeState();
                        break;
                    case MENU_SHARE:
                        if (mTopicsBean != null && mTopicsBean.user != null) {
                            share();
                        }
                        break;
                    case MENU_MORE_STATUS_REPORT:
                        if (!UIUtils.iStartLoginActivity(TopicsDetailActivity.this)) {

                            if (null != mTopicsBean && null != mTopicsBean.user) {
                                TopicsDetailActivity.this.startActivity(StatusReportActivity.getIntent(TopicsDetailActivity.this, mTopicsBean.id + "", mTopicsBean.user.getUsername(), mTopicsBean.text, mTopicsBean.content_type));
                            }
                        }
                        break;
                    case MENU_MORE_GO_HOME:
                        if (mTopicsBean == null || mTopicsBean.content_type != 50) {
                            MainActivity.gotoTopicsHome(TopicsDetailActivity.this);
                        }
                        ((Activity) TopicsDetailActivity.this).finish();
                        break;
                    case MENU_MORE_STATUS_DELETE:
                        if (mTopicsBean.content_type == TYPE_REWARD) {
                            showColseRewardDialog();
                        } else {
                            showDelTopicDialog();
                        }

                        break;
                }
                return false;
            }


        });
    }

    private void showDelTopicDialog() {
        PromptManager.getAlertDialog(TopicsDetailActivity.this).setMessage(getString(R.string.delete_topics)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatusEngineImpl.delete(mTopicsBean.id + "", new ParseHttpListener() {
                    @Override
                    protected Object parseDateTask(String jsonData) {

                        return null;
                    }

                    @Override
                    protected void afterParseData(Object object) {


                        if (mTopicsBean != null) {
//                                            RemoveTopicsEvent removeTopicsListEvent = new RemoveTopicsEvent(mTopicsBean);
//                                            BusProvider.getInstance().post(removeTopicsListEvent);
                            mTopicsBean.appleAction(this, AutoData.Action.Delete).post();
                        }
                        ((Activity) TopicsDetailActivity.this).finish();
                    }
                });
            }
        }).setNegativeButton(R.string.cancel, null).show();
    }

    private void showColseRewardDialog() {
        PromptManager.getAlertDialog(TopicsDetailActivity.this).setMessage(getString(R.string.msg_close_reward)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatusEngineImpl.closeReward(mTopicsBean.id + "", new ParseHttpListener() {
                    @Override
                    protected Object parseDateTask(String jsonData) {

                        return null;
                    }

                    @Override
                    protected void afterParseData(Object object) {
                        PromptManager.showToast(R.string.close_reward_hint);
                        if (mTopicsBean != null) {
                            mTopicsBean.reward_state = 1;
                            mHandler.setRewardState(mTopicsBean.reward_state);
                            mAdapter.notifyDataSetChanged();
                            setTopicsDetail();
                            initFloatMenu();
                            BusProvider.getInstance().post(new TopicStateEvent(mTopicsBean.id, TopicStateEvent.CLOSED));
                        }
                    }
                }.setLoadingDialog(TopicsDetailActivity.this, false));
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, null).show();
    }

    private void share() {
        ShareBean shareBean = new ShareBean();
        if (mTopicsBean.text != null) {

            if (ignoreTV.getText().toString().length() > 30) {
                shareBean.setContent(ignoreTV.getText().toString().substring(0, 30) + "...");
            } else {
                shareBean.setContent(ignoreTV.getText().toString());
            }
        }
        shareBean.setUrl(DKHSClient.getHeadUrl() + "/statuses/" + mTopicsBean.getId());
        if (mTopicsBean.content_type == 0) {
            shareBean.setTitle(String.format("分享 %s 的话题", mTopicsBean.user.getUsername()));
        } else {
            shareBean.setTitle(String.format("分享 %s 的悬赏", mTopicsBean.user.getUsername()));
        }
        String img = null;
        shareBean.setResId(R.drawable.ic_launcher);
        if (mTopicsBean.medias != null && mTopicsBean.medias.size() > 0) {

            String imaUrl = mTopicsBean.medias.get(0).getImage_md();
            String imgPath = ImageLoader.getInstance().getDiskCache().get(imaUrl).getPath();

            if (new File(imgPath).exists()) {
                img = ImageLoader.getInstance().getDiskCache().get(imaUrl).getPath();
                shareBean.setImg(imaUrl);
            }
        }
        shareAction(shareBean, img);
    }

    private void initFloatMenu() {

        if (mTopicsBean.user != null) {
            isMyTopics = UserEntity.currentUser(mTopicsBean.user.getId() + "");
        }

        mFloatingActionMenu.removeAllItems();
        if (mTopicsBean.content_type == TYPE_REWARD) {
            mFloatingActionMenu.addItem(MENU_COMMEND, R.string.answer, R.drawable.ic_comment_detail);
        } else {
            mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_comment_detail);
        }

        if (mTopicsBean.like) {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.praised);
        } else {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_praise_detail);
        }
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_fm_share);
        String[] choices = getResources().getStringArray(R.array.topics_menu_overflow);

        if (!isMyTopics) {
            mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                    .addItem(MENU_MORE_GO_HOME, "回到首页").
                    addItem(MENU_MORE_STATUS_REPORT, "举报");
        } else {
            if (mTopicsBean.content_type == 0) {

                mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                        .addItem(MENU_MORE_GO_HOME, "回到首页").
                        addItem(MENU_MORE_STATUS_DELETE, "删除");
            } else if (mTopicsBean.content_type == TYPE_REWARD) {
                if (mTopicsBean != null && mTopicsBean.reward_state == 0) {
                    mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                            .addItem(MENU_MORE_GO_HOME, "回到首页").
                            addItem(MENU_MORE_STATUS_DELETE, "关闭悬赏");
                } else {
                    mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                            .addItem(MENU_MORE_GO_HOME, "回到首页")
                    ;
                }
            }

        }
    }


    @Override
    public void likePre() {
        initFloatMenu();
        like();
    }

    @Override
    public void unLikePre() {

        initFloatMenu();
        unLike();

    }

    @Override
    public void onFragmentInteraction(TopicsBean topicsBean) {
        mTopicsBean = topicsBean;
        ignoreTV.setText(mTopicsBean.text);
        switch (topicsBean.content_type) {
            case TYPE_TOPIC:
                setTitle("话题正文");
                break;
            case TYPE_NEWS:
                setTitle("新闻正文");
                break;
            case TYPE_ANNOUNCE:
                setTitle("公告正文");
                break;
            case TYPE_REPORT:
                setTitle("研报正文");
                break;
            case TYPE_REWARD:
                setTitle("悬赏正文");
                break;
            case TYPE_SPECIAL:
                setTitle(topicsBean.recommend_title);
                break;
            default:
                break;
        }

        mSwitchLikeStateHandler.setLikeBean(mTopicsBean);
        initFloatMenu();
    }

    @Override
    public void finish() {
        if (mItemTopicsBean != null) {
            //更新列表状态
            mItemTopicsBean.attitudes_count = mTopicsBean.attitudes_count;
            mItemTopicsBean.like = mTopicsBean.like;
            mItemTopicsBean.comments_count = mTopicsBean.comments_count;
            mItemTopicsBean.appleAction(this, AutoData.Action.Update).post();
        }
        super.finish();
    }

    private void shareAction(ShareBean shareBean, String path) {

        if (shareBean == null) {
            return;
        }
        Context context = this;
        final OnekeyShare oks = new OnekeyShare();
//          oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        oks.setTitleUrl(shareBean.getUrl());
        oks.setUrl(shareBean.getUrl());
        oks.setTitle(shareBean.getTitle());
        oks.setText(Html.fromHtml(shareBean.getContent()).toString());
        if (path != null) {
            oks.setImagePath(path);
        } else if (!TextUtils.isEmpty(shareBean.getImg())) {
            oks.setImageUrl(shareBean.getImg());
        } else if (shareBean.getResId() != 0) {
            oks.setBitMap(BitmapFactory.decodeResource(getResources(), shareBean.getResId()));
        }
        oks.setSilent(false);
        oks.setShareFromQQAuthSupport(false);
        oks.setDialogMode();
        oks.show(context);

    }

    @Override
    public void loadFinish(MoreDataBean object) {

        mSwipeLayout.setRefreshing(false);
        mTopicsDetailListView.toggleFooter(false);
        if (mTopicsCommendEngine.getCurrentpage() == 1) {
            mDataList.clear();
            if (object.getResults().size() == 0) {
                NoDataBean noDataBean = new NoDataBean();
                if (mTopicsCommendEngine.isLikes()) {
                    noDataBean.noData = "暂无人点赞";
                } else {
                    if (mTopicsBean.content_type == TYPE_REWARD) {
                        noDataBean.noData = "暂无回答";
                    } else {
                        noDataBean.noData = "暂无评论";
                    }
                }
                mDataList.add(noDataBean);
            }
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void loadFail() {

        mSwipeLayout.setRefreshing(false);

    }


    public void addLikePeople(UserEntity userEntity) {
        if (mSortType != TopicsCommendEngineImpl.SortType.like) {
            return;
        }
        if (mDataList.size() > 0) {
            if (mDataList.get(0) instanceof NoDataBean) {
                mDataList.remove(0);
            }

            boolean had = false;
            for (Object userEntity1 : mDataList) {
                if (userEntity1 instanceof UserEntity) {
                    if (((UserEntity) userEntity1).getId() == userEntity.getId()) {
                        had = true;
                        break;
                    }
                }
            }
            if (!had) {
                mDataList.add(0, userEntity);
            }
        }


    }

    public void removeLikePeople(UserEntity userEntity) {
        if (mSortType != TopicsCommendEngineImpl.SortType.like) {

            return;
        }


        for (Object o : mDataList) {
            if (o instanceof UserEntity) {
                if (((UserEntity) o).getId() == userEntity.getId()) {
                    mDataList.remove(o);
                    if (mDataList.size() == 0) {
                        NoDataBean noDataBean = new NoDataBean();
                        noDataBean.noData = "暂无人点赞";
                        mDataList.add(0, noDataBean);
                    }
                    break;
                }
            }
        }


    }


    public void like() {

        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count += 1;
            addLikePeople(UserEngineImpl.getUserEntity());
            setReplyBar();
            mAdapter.notifyDataSetChanged();
        }

    }

    public void unLike() {
        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count -= 1;
            removeLikePeople(UserEngineImpl.getUserEntity());
            setReplyBar();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getPageStatisticsStringId() {
        if (mTopicsBean == null) {
            return 0;
        }
        if (mTopicsBean.content_type == 40) {
            return R.string.statistics_reward_detail;
        } else if (mTopicsBean.content_type == 0) {
            return R.string.statistics_topic_detail;
        } else if (mTopicsBean.content_type == 50) {
            return R.string.statistics_financial_management;
        }
        return 0;
    }
}
