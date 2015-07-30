package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.fragment.TopicDetailFragment;
import com.dkhs.portfolio.utils.SwitchLikeStateHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.parceler.Parcels;

/**
 * 主贴详情
 */
public class TopicsDetailActivity extends ModelAcitivity implements SwitchLikeStateHandler.StatusChangeI {


    public static final int MENU_COMMEND = 0;
    public static final int MENU_LIKE = 1;
    public static final int MENU_SHARE = 2;
    public static final int MENU_MORE = 3;
    public static final int MENU_MORE_GO_HOME = 4;
    public static final int MENU_MORE_STATUS_REPORT = 5;
    private TopicDetailFragment mTopicDetailFragment;


    public static void startActivity(Context context, TopicsBean topicsBean) {

        Intent intent = new Intent(context, TopicsDetailActivity.class);
        intent.putExtra("topicsBean", Parcels.wrap(topicsBean));
        context.startActivity(intent);
    }

    @ViewInject(R.id.floating_action_view)
    private FloatingActionMenu mFloatingActionMenu;
    private TopicsBean mTopicsBean;
    private SwitchLikeStateHandler mSwitchLikeStateHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideHead();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
            setContentView(R.layout.activity_topics_detail);
            setTitle(R.string.title_activity_topics_detail);
            ViewUtils.inject(this);
            mTopicDetailFragment = new TopicDetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, mTopicDetailFragment).commitAllowingStateLoss();
            initData();
            mSwitchLikeStateHandler=new SwitchLikeStateHandler(mTopicsBean);
            mSwitchLikeStateHandler.setStatusChangeI(this);
        }

    }

    /**
     * iniView initData
     */
    public void initData() {
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_coment);
        mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_unlike);
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_share_back);
        String[] choices=getResources().getStringArray(R.array.topics_menu_overflow);
        mFloatingActionMenu.addMoreItem(MENU_MORE,getString( R.string.more), R.drawable.ic_fm_more)
                .addItem(MENU_MORE_GO_HOME, choices[0]).
                addItem(MENU_MORE_STATUS_REPORT,choices[1]);
        mFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {
                switch (paramInt) {
                    case MENU_COMMEND:
                        startActivity(PostTopicActivity.getIntent(mContext,PostTopicActivity.TYPE_RETWEET,mTopicsBean.id+"",mTopicsBean.user.getUsername()));
                        break;
                    case MENU_LIKE:
                        mSwitchLikeStateHandler.toggleLikeState();
                        break;
                    case MENU_SHARE:
                        break;

                    case MENU_MORE_STATUS_REPORT:
                        mContext.startActivity(new Intent(mContext, StatusReportActivity.class));

                        break;
                    case MENU_MORE_GO_HOME:

                        ((Activity)mContext).finish();
                        break;
                }
                return false;
            }
        });
    }




    @Override
    public void likePre() {
        mFloatingActionMenu.removeAllItems();
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_coment);
        mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_liked);
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_share_back);
        String[] choices=getResources().getStringArray(R.array.topics_menu_overflow);
        mFloatingActionMenu.addMoreItem(MENU_MORE,getString( R.string.more), R.drawable.ic_fm_more)
                .addItem(MENU_MORE_GO_HOME, choices[0]).
                addItem(MENU_MORE_STATUS_REPORT,choices[1]);
        mTopicDetailFragment.like();

    }

    @Override
    public void unLikePre() {
        mFloatingActionMenu.removeAllItems();
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_coment);
        mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_unlike);
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_share_back);
        String[] choices=getResources().getStringArray(R.array.topics_menu_overflow);
        mFloatingActionMenu.addMoreItem(MENU_MORE,getString( R.string.more), R.drawable.ic_fm_more)
                .addItem(MENU_MORE_GO_HOME, choices[0]).
                addItem(MENU_MORE_STATUS_REPORT,choices[1]);
        mTopicDetailFragment.unLike();

    }
}
