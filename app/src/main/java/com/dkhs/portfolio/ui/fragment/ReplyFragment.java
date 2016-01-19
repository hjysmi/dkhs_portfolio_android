package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommentHandler;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.eventbus.DeleteCommentEvent;
import com.dkhs.portfolio.ui.listener.CommentItemClick;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/27.17:43
 */
public class ReplyFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int current_page = 0;
    private int total_page = 0;
    private int total_count;
    private int CUR_TYPE;
    private int TYPE_LODAMORE = 1;
    private List<CommentBean> results = new ArrayList<>();

    private static final String USER_ID = "user_id";
    private static final String CONTENT_TYPE = "content_type";
    private String userId;
    private String contentType;
    private boolean isCurrentUser;
    //    private static final String REPLY_TYPE = "reply_type";
//    public static final int TYPE_MINE = 1;
//    public static final int TYPE_OTHERS = 2;
    private int statusType;

    private CommentItemClick mCommentClick;

    /**
     * @param userId 帖子id
     * @return ReplyFragemnt
     */
    public static ReplyFragment getIntent(String userId,String contentType) {

        ReplyFragment fragment = new ReplyFragment();

        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        bundle.putString(CONTENT_TYPE,contentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        if (null != GlobalParams.LOGIN_USER) {
            mCommentClick = new CommentItemClick(GlobalParams.LOGIN_USER.getId() + "", getActivity());
        } else {
            mCommentClick = new CommentItemClick("", getActivity());
        }
    }

    private PullToRefreshListView lvReply;
    private DKBaseAdapter adapter;
    private ImageView ivPraise;
    private BitmapUtils bitmapUtils;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView tvEmpty;
    private View mProgressView;

    private void initView(View view) {
        mProgressView = view.findViewById(android.R.id.progress);
        tvEmpty = (TextView) view.findViewById(android.R.id.empty);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        lvReply = (PullToRefreshListView) view.findViewById(R.id.lv_reply);
        lvReply.setCanRefresh(false);
        lvReply.setOnLoadListener(new PullToRefreshListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                CUR_TYPE = TYPE_LODAMORE;
                if (current_page < total_page && current_page != 0 && total_page != 0) {
                    StatusEngineImpl.getReplys(userId,contentType, current_page + 1, 0, replyListener);
                }
            }
        });
        lvReply.setOnItemClickListener(this);

        bitmapUtils = new BitmapUtils(getActivity());
        bitmapUtils.configDefaultLoadingImage(R.drawable.default_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_head);


    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            userId = bundle.getString(USER_ID);
            contentType = bundle.getString(CONTENT_TYPE);
            if (null != GlobalParams.LOGIN_USER) {
                isCurrentUser = String.valueOf(GlobalParams.LOGIN_USER.getId()).equals(userId);
            }
//            if (isCurrentUser) {
//                setTitle(R.string.mine_reply);
//            } else {
//                setTitle(R.string.others_reply);
//            }


        }
        showProgress();
        refreshData();
    }

    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void dismissProgress(){
        if(mProgressView.getVisibility() == View.VISIBLE){
            mProgressView.setVisibility(View.GONE);
        }
    }
    private void refreshData() {
//        mSwipeLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeLayout.setRefreshing(true);
//            }
//        });
        current_page = 0;
        if (!TextUtils.isEmpty(userId)) {

            StatusEngineImpl.getReplys(userId,contentType, current_page, 0, replyListener);
        } else {
            userId = "1";
            StatusEngineImpl.getReplys(userId,contentType, current_page, 0, replyListener);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_praise:
                ivPraise.setBackgroundResource(R.drawable.praised);
                ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);//设置动画持续时间
                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(1);
                animation.setFillAfter(false);
                ivPraise.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivPraise.setBackgroundResource(R.drawable.praise);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommentBean commentBean = results.get(position + 1);
        mCommentClick.clickFromMyReply(commentBean);

    }


    @Override
    public int setContentLayoutId() {
        return R.layout.activity_reply;
    }

    @Subscribe
    public void deleteSuccessUpdate(DeleteCommentEvent event) {
        if (null != event) {
            refreshData();
        }
    }


    private ParseHttpListener<MoreDataBean<CommentBean>> replyListener = new ParseHttpListener<MoreDataBean<CommentBean>>() {
        @Override
        protected MoreDataBean<CommentBean> parseDateTask(String jsonData) {
            MoreDataBean<CommentBean> moreDataBean = (MoreDataBean<CommentBean>) DataParse.parseObjectJson(new TypeToken<MoreDataBean<CommentBean>>() {
            }.getType(), jsonData);
            return moreDataBean;
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            mSwipeLayout.setRefreshing(false);
            dismissProgress();
        }

        @Override
        protected void afterParseData(MoreDataBean<CommentBean> moreDataBean) {
            dismissProgress();
            mSwipeLayout.setRefreshing(false);
            if (moreDataBean != null && moreDataBean.getResults() != null) {
                current_page = moreDataBean.getCurrentPage();
                total_count = moreDataBean.getTotalCount();
                total_page = moreDataBean.getTotalPage();
                if (CUR_TYPE == TYPE_LODAMORE) {
                    lvReply.onLoadMoreComplete();
                }
                if (current_page != 0 && current_page == total_page) {
                    lvReply.setCanLoadMore(false);
                    lvReply.setAutoLoadMore(false);
                } else if (current_page < total_page) {
                    lvReply.setCanLoadMore(true);
                }
//                if (total_count > 0) {
                if (current_page == 1) {
                    results.clear();
                    results.addAll(moreDataBean.getResults());
                } else {
                    results.addAll(moreDataBean.getResults());
                }
                if (adapter == null) {
//                    adapter = new MyReplyAdapter(ReplyActivity.this, results);
                    CommentHandler itemHandler = new CommentHandler(getActivity(), true, true);
                    itemHandler.setReplyComment(true);
                    adapter = new DKBaseAdapter(getActivity(), results).buildSingleItemView(itemHandler);
                    lvReply.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
//                }
            }
            updateEmptyView();

        }
    };


    private void updateEmptyView() {
        if (null == results || results.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            if(contentType.equals(String.valueOf(TopicsDetailActivity.TYPE_REWARD))){
                tvEmpty.setText(R.string.no_activity);
            }else{
                tvEmpty.setText(R.string.no_comment);
            }
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

}
