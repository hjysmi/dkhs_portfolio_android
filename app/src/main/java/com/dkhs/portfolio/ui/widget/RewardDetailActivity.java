package com.dkhs.portfolio.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
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
import com.dkhs.portfolio.bean.itemhandler.RewardAnswerHandler;
import com.dkhs.portfolio.bean.itemhandler.RewardDetailHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.LoadingHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.RewardReplyBarHandler;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.FloatingActionMenu;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.ModelAcitivity;
import com.dkhs.portfolio.ui.PostRewardActivity;
import com.dkhs.portfolio.ui.StatusReportActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.dkhs.portfolio.ui.fragment.TopicDetailFragment;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
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
 * 主贴详情
 */
public class RewardDetailActivity extends ModelAcitivity implements SwitchLikeStateHandler.StatusChangeI, TopicDetailFragment.OnFragmentInteractionListener, LoadMoreDataEngine.ILoadDataBackListener {


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
    private RewardDetailListView mRewardDetailListView;
    @ViewInject(R.id.tsv)
    private RewardDetailScrollView mRewardDetailScrollView;
/*    @ViewInject(R.id.adopt_reply_rl)
    private RelativeLayout mAdoptRl;*/
    TopicsCommendEngineImpl.SortType mSortType;
    RewardAnswerHandler mHandler;


    private RewardDetailHandler mRewardDetailHandler = new RewardDetailHandler(this);
    private RewardReplyBarHandler mRewardReplyBarHandler = new RewardReplyBarHandler(this);

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
            RewardDetailActivity.startActivity(context, Integer.parseInt(id));
    }

    public static void startActivity(Context context, TopicsBean topicsBean, boolean scrollToComment) {
        Intent intent = new Intent(context, RewardDetailActivity.class);
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
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
            mItemTopicsBean = mTopicsBean;
            mScrollToComment = getIntent().getBooleanExtra("scrollToComment", false);
            setContentView(R.layout.activity_reward_detail);

//            Toolbar toolbar= (Toolbar) findViewById(R.id.tool);
//            setSupportActionBar(toolbar);
            ViewUtils.inject(this);
            mSwipeLayout.setColorSchemeResources(R.color.theme_blue);


            initData();
            setTopicsDetail();
            setRewardAdopted();
            ignoreTV.setText(mTopicsBean.text);
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(mTopicsBean);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            loadData();
            mFloatingActionMenu.attachToListViewTop(mRewardDetailListView, null, null);
            BusProvider.getInstance().register(this);
            mRewardDetailListView.setFocusable(false);
        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void setRewardAdopted(){
        if(mTopicsBean.reward_state == 2){
            StatusEngineImpl.getAdoptedReply(String.valueOf(mTopicsBean.getId()),new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return CommentBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
//                    mAdoptRl.setVisibility(View.VISIBLE);
                    CommentBean comment = (CommentBean)object;
//                    mAdoptHandler.onBindView(ViewHolder.newInstant(mAdoptRl),comment,0);
//                    setTopicsDetail();
                }

                @Override
                protected Object parseDateTask(String jsonData) {
                    return super.parseDateTask(jsonData);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mRewardDetailListView.setFocusableInTouchMode(true);
            mRewardDetailListView.setFocusable(true);
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
                mSwipeLayout.setRefreshing(false);
                mTopicsBean = (TopicsBean) object;
                mHandler.setRewardUserId(mTopicsBean.getUser().getId());
                mHandler.setRewardState(mTopicsBean.reward_state);
                onFragmentInteraction(mTopicsBean);
                setTopicsDetail();
                if (mScrollToComment) {

                    mRewardDetailScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollToComment = false;
                            mRewardDetailScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 500);
                }

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

        mRewardDetailListView.setOnLoadMoreListener(new RewardDetailListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                    return;
                }
                mRewardDetailListView.toggleFooter(true);
                getLoadEngine().loadMore();
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mHandler = new RewardAnswerHandler(this, true, true);
        mAdapter = new DKBaseAdapter(this, mDataList)
                .buildMultiItemView(TopicsBean.class, new RewardDetailHandler(this))
                .buildMultiItemView(CommentBean.class, mHandler)
                .buildMultiItemView(NoDataBean.class, new NoDataHandler())
                .buildMultiItemView(LoadingBean.class, new LoadingHandler())
                .buildMultiItemView(UserEntity.class, new LikePeopleHandler(this))
                .buildMultiItemView(PeopleBean.class, new LikePeopleHandler(this));

        mRewardDetailListView.setAdapter(mAdapter);
        mDataList.setAdapter(mAdapter);
        mDataList.add(new LoadingBean());
        mDataList.setActionHandler(new AutoList.ActionHandler<AutoData>() {
            @Override
            public boolean beforeHandleAction(AutoData a) {

                if (a instanceof CommentBean) {
                    mTopicsBean.comments_count = mDataList.size();
                    setTopicsDetail();
                    switch (a.action) {

                        case Add:
                            if (mDataList.size() > 0 && (mDataList.get(0) instanceof NoDataBean)) {
                                mDataList.remove(0);
                            }


                    }
                }

                return false;
            }

            @Override
            public void afterHandleAction(AutoData a) {

                if (a instanceof CommentBean) {
                    mTopicsBean.comments_count = mDataList.size();
                    setTopicsDetail();
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

                        if (UIUtils.iStartLoginActivity(RewardDetailActivity.this)) {
                            break;
                        }
                        if (null != mTopicsBean && null != mTopicsBean.user) {

                            startActivity(PostRewardActivity.getIntent(RewardDetailActivity.this,
                                    PostRewardActivity.TYPE_COMMENT, mTopicsBean.id + "", mTopicsBean.user.getUsername()));
                        }
                        break;
                    case MENU_LIKE:

                        mSwitchLikeStateHandler.toggleLikeState();
                        break;
                    case MENU_SHARE:
                        if (mTopicsBean != null) {
                            share();
                        }
                        break;
                    case MENU_MORE_STATUS_REPORT:
                        if (!UIUtils.iStartLoginActivity(RewardDetailActivity.this)) {

                            if (null != mTopicsBean && null != mTopicsBean.user) {

                                RewardDetailActivity.this.startActivity(StatusReportActivity.getIntent(RewardDetailActivity.this, mTopicsBean.id + "", mTopicsBean.user.getUsername(), mTopicsBean.text));
                            }
                        }
                        break;
                    case MENU_MORE_GO_HOME:

                        MainActivity.gotoTopicsHome(RewardDetailActivity.this);
                        ((Activity) RewardDetailActivity.this).finish();
                        break;
                    case MENU_MORE_STATUS_DELETE:

                        PromptManager.getAlertDialog(RewardDetailActivity.this).setMessage(getString(R.string.msg_close_reward)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StatusEngineImpl.closeReward(mTopicsBean.id + "", new ParseHttpListener() {
                                    @Override
                                    protected Object parseDateTask(String jsonData) {

                                        return null;
                                    }

                                    @Override
                                    protected void afterParseData(Object object) {
                                        if (mTopicsBean != null) {
                                            mTopicsBean.reward_state = 1;
                                            mHandler.setRewardState(mTopicsBean.reward_state);
                                            mAdapter.notifyDataSetChanged();
                                            initFloatMenu();
                                        }

                                    }
                                });
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();

                        break;
                }
                return false;
            }
        });
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
        shareBean.setTitle(String.format("分享 %s 的悬赏", mTopicsBean.user.getUsername()));
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
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.answer, R.drawable.ic_coment);
        if (mTopicsBean.like) {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.praised);
        } else {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.praise);
        }
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_fm_share);
        String[] choices = getResources().getStringArray(R.array.topics_menu_overflow);

        if (!isMyTopics) {
            mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                    .addItem(MENU_MORE_GO_HOME, "回到首页").
                    addItem(MENU_MORE_STATUS_REPORT, "举报");
        } else {
            if(mTopicsBean != null && mTopicsBean.reward_state == 0){
                mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                        .addItem(MENU_MORE_GO_HOME, "回到首页").
                        addItem(MENU_MORE_STATUS_DELETE, "关闭悬赏");
            }else{
                mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                        .addItem(MENU_MORE_GO_HOME, "回到首页")
                        ;
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
            case 0:
                setTitle("话题正文");
                break;
            case 10:
                setTitle("新闻正文");
                break;
            case 20:
                setTitle("公告正文");
                break;
            case 30:
                setTitle("研报正文");
                break;
            case 40:
                setTitle("悬赏正文");
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
        mRewardDetailListView.toggleFooter(false);
        if (mTopicsCommendEngine.getCurrentpage() == 1) {
            mDataList.clear();
            if (object.getResults().size() == 0) {
                NoDataBean noDataBean = new NoDataBean();
                if (mTopicsCommendEngine.isLikes()) {
                    noDataBean.noData = "暂无人点赞";
                } else {
                    noDataBean.noData = "暂无评论";
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
        setTopicsDetail();

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
        setTopicsDetail();

    }


    public void like() {

        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count += 1;
            addLikePeople(UserEngineImpl.getUserEntity());
            mAdapter.notifyDataSetChanged();

        }

    }

    public void unLike() {
        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count -= 1;
            removeLikePeople(UserEngineImpl.getUserEntity());
            mAdapter.notifyDataSetChanged();
        }
    }

}
