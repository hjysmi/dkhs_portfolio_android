package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.DKHSTextView;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PhoneInfo;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by zhangcm on 2015/7/27.17:43
 */
public class ReplyActivity extends ModelAcitivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int current_page = 0;
    private int total_page = 0;
    private int total_count;
    private int CUR_TYPE;
    private int TYPE_LODAMORE = 1;
    private List<CommentBean> results;

    private static final String USER_ID = "user_id";
    private String userId;
    private boolean isCurrentUser;
    //    private static final String REPLY_TYPE = "reply_type";
//    public static final int TYPE_MINE = 1;
//    public static final int TYPE_OTHERS = 2;
    private int statusType;


    /**
     * @param context
     * @param userId  帖子id
     * @return
     */
    public static Intent getIntent(Context context, String userId) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra(USER_ID, userId);
//        intent.putExtra(REPLY_TYPE, replyType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_reply);


        initView();
        initData();
    }


    private PullToRefreshListView lvReply;
    private MyReplyAdapter adapter;
    private ImageView ivPraise;
    private BitmapUtils bitmapUtils;

    private void initView() {
        lvReply = (PullToRefreshListView) findViewById(R.id.lv_reply);
        lvReply.setCanRefresh(false);
        lvReply.setOnLoadListener(new PullToRefreshListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                CUR_TYPE = TYPE_LODAMORE;
                if (current_page < total_page && current_page != 0 && total_page != 0) {
                    StatusEngineImpl.getReplys(userId, current_page + 1, 0, listener);
                }
            }
        });
        lvReply.setOnItemClickListener(this);

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultLoadingImage(R.drawable.default_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_head);


    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userId = bundle.getString(USER_ID);
            if (null != GlobalParams.LOGIN_USER) {
                isCurrentUser = String.valueOf(GlobalParams.LOGIN_USER.getId()).equals(userId);
            }
            if (isCurrentUser) {
                setTitle(R.string.mine_reply);
            } else {
                setTitle(R.string.others_reply);
            }
            if (!TextUtils.isEmpty(userId)) {
                StatusEngineImpl.getReplys(userId, current_page, 0, listener);
            }

        } else {
            userId = "1";
            StatusEngineImpl.getReplys(userId, current_page, 0, listener);
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
        if (isCurrentUser) {//当前用户
            showMineReplyDialog(commentBean);
        } else { // TA的回复
            showOtherReplyDialog(commentBean);
        }
    }

    private MAlertDialog mDialog;

    private void showOtherReplyDialog(final CommentBean commentBean) {
        if (null == mDialog) {

            MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
            String[] choice = getResources().getStringArray(R.array.choices_other_reply);
            mDialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0://回复评论
                            //
                            break;
                        case 1://查看主贴
                            break;
                        case 2://复制
                            PhoneInfo.copyToClipboard(commentBean.getText(), mContext);
                            PromptManager.showToast(R.string.msg_copy_content_success);
                            break;
                        case 3://举报
                            ReplyActivity.this.startActivity(StatusReportActivity.getIntent(mContext, commentBean.getId(), commentBean.getUser().getUsername(), commentBean.getText()));
                            break;
                    }
                    dialog.dismiss();
                }
            });
        }
        mDialog.show();
    }

    private void showMineReplyDialog(final CommentBean commentBean) {
        if (null == mDialog) {
            MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
            String[] choice = getResources().getStringArray(R.array.choices_mine_reply);
            mDialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0://查看主贴
                            //
                            break;
                        case 1://复制内容
                            PhoneInfo.copyToClipboard(commentBean.getText(), mContext);
                            PromptManager.showToast(R.string.msg_copy_content_success);
                            break;
                        case 2://删除回复
                            break;
                    }
                    dialog.dismiss();
                }
            });
        }
        mDialog.show();
    }

    private class MyReplyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (results != null) {
                return results.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_reply, null);
                holder = new ViewHolder();
                holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
                holder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
                holder.tvText = (DKHSTextView) convertView.findViewById(R.id.tv_text);
                holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
                convertView.setTag(holder);
            }
            CommentBean comment = results.get(position);
            holder = (ViewHolder) convertView.getTag();
            UserEntity user = comment.getUser();
            if (!TextUtils.isEmpty(user.getAvatar_sm())) {
                bitmapUtils.display(holder.ivHead, comment.getUser().getAvatar_sm());
            }
            holder.tvUserName.setText(user.getUsername());
            holder.ivPraise.setTag(position);
            holder.tvText.setText(comment.getText());
            holder.ivPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //TODO 点赞处理
                    int position = (int) v.getTag();
                    v.setBackgroundResource(R.drawable.praised);
                    ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);//设置动画持续时间
                    animation.setRepeatMode(Animation.REVERSE);
                    animation.setRepeatCount(1);
                    animation.setFillAfter(false);
                    v.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            v.setBackgroundResource(R.drawable.praise);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    //todo 提交点赞


                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView ivPraise;
            ImageView ivHead;
            TextView tvUserName;
            TextView tvTime;
            TextView tvPraiseCount;
            DKHSTextView tvText;
        }
    }

    private ParseHttpListener<MoreDataBean<CommentBean>> listener = new ParseHttpListener<MoreDataBean<CommentBean>>() {
        @Override
        protected MoreDataBean<CommentBean> parseDateTask(String jsonData) {
            MoreDataBean<CommentBean> moreDataBean = (MoreDataBean<CommentBean>) DataParse.parseObjectJson(new TypeToken<MoreDataBean<CommentBean>>() {
            }.getType(), jsonData);
            return moreDataBean;
        }

        @Override
        protected void afterParseData(MoreDataBean<CommentBean> moreDataBean) {
            if (moreDataBean != null && moreDataBean.getResults() != null && moreDataBean.getResults().size() > 0) {
                current_page = moreDataBean.getCurrentPage();
                total_count = moreDataBean.getTotalCount();
                total_page = moreDataBean.getTotalPage();
                if (CUR_TYPE == TYPE_LODAMORE) {
                    lvReply.onLoadMoreComplete();
                }
                if (current_page != 0 && current_page == total_page) {
                    lvReply.setCanLoadMore(false);
                } else if (current_page < total_page) {
                    lvReply.setCanLoadMore(true);
                }
                if (total_count > 0) {
                    if (current_page == 1) {
                        results = moreDataBean.getResults();
                    } else {
                        results.addAll(moreDataBean.getResults());
                    }
                    if (adapter == null) {
                        adapter = new MyReplyAdapter();
                        lvReply.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

}
