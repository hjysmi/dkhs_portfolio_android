package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RemoveTopicsEvent;
import com.dkhs.portfolio.ui.eventbus.UpdateTopicsListEvent;
import com.dkhs.portfolio.ui.fragment.TopicDetailFragment;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.parceler.Parcels;

import java.io.File;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 主贴详情
 */
public class TopicsDetailActivity extends ModelAcitivity implements SwitchLikeStateHandler.StatusChangeI, TopicDetailFragment.OnFragmentInteractionListener {


    public static final int MENU_COMMEND = 0;
    public static final int MENU_LIKE = 1;
    public static final int MENU_SHARE = 2;
    public static final int MENU_MORE = 3;
    public static final int MENU_MORE_GO_HOME = 4;
    public static final int MENU_MORE_STATUS_REPORT = 5;
    public static final int MENU_MORE_STATUS_DELETE = 6;
    private TopicDetailFragment mTopicDetailFragment;
    private Boolean mScrollToComment;
    @ViewInject(R.id.ignoreTV)
    private TextView ignoreTV;

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
    private TopicsBean mTopicsBean;
    private SwitchLikeStateHandler mSwitchLikeStateHandler;

    private boolean isMyTopics = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideHead();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));

            mScrollToComment = getIntent().getBooleanExtra("scrollToComment", false);
            setContentView(R.layout.activity_topics_detail);
//            setTitle(R.string.title_activity_topics_detail);
            ViewUtils.inject(this);
            mTopicDetailFragment = new TopicDetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, mTopicDetailFragment).commitAllowingStateLoss();
            initData();
            ignoreTV.setText(mTopicsBean.text);
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(mTopicsBean);
            mSwitchLikeStateHandler.setStatusChangeI(this);

        }

    }

    /**
     * iniView initData
     */
    public void initData() {
        initFloatMenu();
        mFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {
                switch (paramInt) {
                    case MENU_COMMEND:

                        if (UIUtils.iStartLoginActivity(mContext)) {
                            break;
                        }
                        startActivity(PostTopicActivity.getIntent(mContext,
                                PostTopicActivity.TYPE_COMMENT, mTopicsBean.id + "", mTopicsBean.user.getUsername()));
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
                        mContext.startActivity(StatusReportActivity.getIntent(mContext, mTopicsBean.id + "", mTopicsBean.user.getUsername(), mTopicsBean.text));
                        break;
                    case MENU_MORE_GO_HOME:

                        MainActivity.gotoTopicsHome(mContext);
                        ((Activity) mContext).finish();
                        break;
                    case MENU_MORE_STATUS_DELETE:

                        PromptManager.getAlertDialog(mContext).setMessage(getString(R.string.delete_topics)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
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
                                            RemoveTopicsEvent removeTopicsListEvent = new RemoveTopicsEvent(mTopicsBean);
                                            BusProvider.getInstance().post(removeTopicsListEvent);
                                        }
                                        ((Activity) mContext).finish();
                                    }
                                });
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
        shareBean.setTitle(String.format("分享 %s 的话题", mTopicsBean.user.getUsername()));
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
        mFloatingActionMenu.addItem(MENU_COMMEND, R.string.comment, R.drawable.ic_coment);
        if (mTopicsBean.like) {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_like);
        } else {
            mFloatingActionMenu.addItem(MENU_LIKE, R.string.like, R.drawable.ic_unlike);
        }
        mFloatingActionMenu.addItem(MENU_SHARE, R.string.share, R.drawable.ic_fm_share);
        String[] choices = getResources().getStringArray(R.array.topics_menu_overflow);

        if (!isMyTopics) {
            mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                    .addItem(MENU_MORE_GO_HOME, "回到首页").
                    addItem(MENU_MORE_STATUS_REPORT, "举报");
        } else {
            mFloatingActionMenu.addMoreItem(MENU_MORE, getString(R.string.more), R.drawable.ic_fm_more)
                    .addItem(MENU_MORE_GO_HOME, "回到首页").
                    addItem(MENU_MORE_STATUS_DELETE, "删除");
        }
    }


    @Override
    public void likePre() {
        initFloatMenu();
        mTopicDetailFragment.like();
    }

    @Override
    public void unLikePre() {

        initFloatMenu();
        mTopicDetailFragment.unLike();

    }

    @Override
    public void onFragmentInteraction(TopicsBean topicsBean) {
        mTopicsBean = topicsBean;
        ignoreTV.setText(mTopicsBean.text);

//        公告正文
//                研报正文
//        话题正文
//                新闻正文
//        CONTENT_TYPE = (
//                (0, '话题'),
//        (10, '新闻'),
//        (20, '公告'),
//        (30, '研报'),
//        )
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
            default:
                break;
        }

        mSwitchLikeStateHandler.setLikeBean(mTopicsBean);
        initFloatMenu();
    }

    @Override
    public void finish() {
        if (mTopicsBean != null) {
            //更新列表状态
            UpdateTopicsListEvent updateTopicsListEvent = new UpdateTopicsListEvent(mTopicsBean);
            BusProvider.getInstance().post(updateTopicsListEvent);
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
}
