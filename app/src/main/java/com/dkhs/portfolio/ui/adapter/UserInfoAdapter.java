package com.dkhs.portfolio.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.FlowPackageActivity;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.MyDraftActivity;
import com.dkhs.portfolio.ui.MyPurseActivity;
import com.dkhs.portfolio.ui.MyRewardActivity;
import com.dkhs.portfolio.ui.MyTopicActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.widget.FlexibleDividerDecoration;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zjz on 2015/7/22.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FlexibleDividerDecoration.SizeProvider {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final int INDEX_MESSAGE = 0;
    private static final int INDEX_MY_COMBINATION = 1;
    private static final int INDEX_PURSE = 2;
    private static final int INDEX_COINS = 3;
    private static final int INDEX_REWARD = 4;
    private static final int INDEX_USER_ENTITY = 5;
    private static final int INDEX_DRAFT = 6;

    private String[] titleTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.user_info_title);
    private int[] iconRes;

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private String mInviteCode;
    private int mUnreadCount;

    public UserInfoAdapter(Context context) {
        super();
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        initIconResource();
    }

    private void initIconResource() {
        TypedArray ar = mContext.getResources().obtainTypedArray(R.array.ic_info_ids);
        int len = ar.length();
        iconRes = new int[len];
        for (int i = 0; i < len; i++) iconRes[i] = ar.getResourceId(i, 0);
        ar.recycle();
        if (PortfolioApplication.hasUserLogin()) {
            getInviteCode();
        }

    }

    private void getInviteCode() {
        AdEngineImpl.getInvitingInfo(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return ShareBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                if (object != null) {
                    mInviteCode = ((ShareBean) object).getCode();
                    Log.d("UserInfoAdatper", mInviteCode + " mInviteCode");
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM) {
            view = mLayoutInflater.inflate(R.layout.item_user_info, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            view = mLayoutInflater.inflate(R.layout.layout_userinfo_header, parent, false);
            return new HeadViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            final int itemPosition = position - 1;
            itemHolder.tvTitle.setText(titleTexts[itemPosition]);
            itemHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes[itemPosition],
                    0, 0, 0);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition(itemPosition);
                }
            });
            if (itemPosition == 0) {

                if (mUnreadCount > 0) {
                    itemHolder.tvUnReadCount.setVisibility(View.VISIBLE);
                    itemHolder.tvUnReadCount.setText(mUnreadCount + "");
                } else {
                    itemHolder.tvUnReadCount.setVisibility(View.GONE);
                }

            } else {
                itemHolder.tvUnReadCount.setVisibility(View.GONE);
            }
        } else if (holder instanceof HeadViewHolder) {
            HeadViewHolder headHolder = (HeadViewHolder) holder;
            headHolder.updateView();
        }


    }

    public void setUnreadCount(int count) {
        this.mUnreadCount = count;
        notifyItemChanged(1);
    }

    private void clickPosition(int position) {
        if (UIUtils.iStartLoginActivity(mContext)) {
            return;
        }
        switch (position) {

            case INDEX_MESSAGE: //消息中心


                MessageManager.getInstance().startConversationList(mContext);

                break;
            case INDEX_MY_COMBINATION: //我的组合

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, MyCombinationActivity.class));


                break;

            case INDEX_USER_ENTITY://我的话题

                UserEntity userEntity = UserEngineImpl.getUserEntity();
                MyTopicActivity.starActivity(mContext, userEntity.getId() + "", userEntity.getUsername());
                break;
//            case 4://我的回复
//                UIUtils.startAnimationActivity((Activity) mContext, ReplyActivity.getIntent(mContext, GlobalParams.LOGIN_USER.getId() + ""));
//
//                break;
            case INDEX_DRAFT://我的草稿
                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, MyDraftActivity.class));

                break;


   /*         case 6://邀请好友

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, InviteFriendsActivity.class));


                break;*/


            case INDEX_COINS://流量兑换

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, FlowPackageActivity.class));

                break;
            case INDEX_PURSE:
                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, MyPurseActivity.class));
                break;
            case INDEX_REWARD:
                UserEntity entity = UserEngineImpl.getUserEntity();
                MyRewardActivity.starActivity(mContext, entity.getId() + "", entity.getUsername());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return titleTexts.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public int dividerSize(int position, RecyclerView parent) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 4:
            case 7:
                return parent.getResources().getDimensionPixelOffset(R.dimen.combin_horSpacing);

            default:
                return -1;
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvUnReadCount;
        TextView tvTip;

        public ItemViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_infotitle);
            tvUnReadCount = (TextView) view.findViewById(R.id.tv_unread_count);
            tvTip = (TextView) view.findViewById(R.id.tv_info_tip);
        }

    }

    static class HeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @ViewInject(R.id.ll_login_layout)
        private View viewLogin;
        @ViewInject(R.id.ll_userinfo_layout)
        private View viewUserInfo;
        @ViewInject(R.id.setting_image_head)
        private ImageView settingImageHead;
        @ViewInject(R.id.setting_text_account_text)
        private TextView settingTextAccountText;
        @ViewInject(R.id.setting_text_name_text)
        private TextView settingTextNameText;
//
//    @ViewInject(R.id.tv_unread_count)
//    private TextView unreadCountTV;

        @ViewInject(R.id.tv_followers)
        private TextView tvFollowers;

        @ViewInject(R.id.tv_following)
        private TextView tvFollowing;
        ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

            @Override
            protected UserEntity parseDateTask(String jsonData) {

                return DataParse.parseObjectJson(UserEntity.class, jsonData);
            }

            @Override
            protected void afterParseData(UserEntity object) {
                if (null != object) {
                    updateUserFollowInfo(object);
                }

            }
        };
        private View mView;

        public HeadViewHolder(View view) {
            super(view);
            this.mView = view;
            ViewUtils.inject(this, view); // 注入view和事件
        }

        @OnClick({R.id.btn_login, R.id.setting_layout_icon, R.id.user_myfunds_layout, R.id.ll_following, R.id.ll_followers})
        public void onClick(View v) {
            int id = v.getId();

            switch (id) {
                case R.id.btn_login:
                    UIUtils.iStartLoginActivity(mView.getContext());
                    break;
                case R.id.setting_layout_icon:
                    startUserInfoActivity();
                    break;
                case R.id.user_myfunds_layout:
                    if (!UIUtils.iStartLoginActivity(mView.getContext())) {
                        UIUtils.startAnimationActivity((Activity) mView.getContext(), new Intent(mView.getContext(), MyCombinationActivity.class));
                    }
                    break;

                case R.id.ll_following:
                    Intent followIntent = new Intent(mView.getContext(), FriendsOrFollowersActivity.class);
                    followIntent.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FRIENDS);
                    followIntent.putExtra(FriendsOrFollowersActivity.USER_ID, UserEngineImpl.getUserEntity().getId() + "");
                    UIUtils.startAnimationActivity((Activity) mView.getContext(), followIntent);
                    break;
                case R.id.ll_followers:
                    Intent intent1 = new Intent(mView.getContext(), FriendsOrFollowersActivity.class);
                    intent1.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FOLLOWER);
                    intent1.putExtra(FriendsOrFollowersActivity.USER_ID, UserEngineImpl.getUserEntity().getId() + "");
                    UIUtils.startAnimationActivity((Activity) mView.getContext(), intent1);
                    break;

            }


        }

        private void startUserInfoActivity() {
            Intent intent = UserHomePageActivity.getIntent(mView.getContext(), UserEngineImpl.getUserEntity().getUsername(),
                    UserEngineImpl.getUserEntity().getId() + "");
            UIUtils.startAnimationActivity((Activity) mView.getContext(), intent);
        }

        public void updateView() {
            if (PortfolioApplication.hasUserLogin()) {
                viewLogin.setVisibility(View.GONE);
                viewUserInfo.setVisibility(View.VISIBLE);
                String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
                if (!TextUtils.isEmpty(account)) {
                    settingTextAccountText.setText(account);
                }
                settingTextNameText.setText(PortfolioPreferenceManager
                        .getStringValue(PortfolioPreferenceManager.KEY_USERNAME));

                String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
                if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                    BitmapUtils bitmapUtils = new BitmapUtils(mView.getContext());
                    bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
                    bitmapUtils.display(settingImageHead, url);

                } else {
                    Bitmap b = BitmapFactory.decodeResource(mView.getResources(), R.drawable.ic_user_head);
                    b = UIUtils.toRoundBitmap(b);
                    settingImageHead.setImageBitmap(b);

                }

                UserEntity userEntity = UserEngineImpl.getUserEntity();
                new UserEngineImpl().getBaseUserInfo(userEntity.getId() + "", userInfoListener);
            } else {
                viewLogin.setVisibility(View.VISIBLE);
                viewUserInfo.setVisibility(View.GONE);
            }


        }

        private void updateUserFollowInfo(UserEntity object) {
            tvFollowers.setText(StringFromatUtils.handleNumber(object.getFollowed_by_count()));
            tvFollowing.setText(StringFromatUtils.handleNumber(object.getFriends_count()));
            //当使用第三方账号第一次登录时，PortfolioPreferenceManager.KEY_USER_HEADER_URL未能保存数据需要通过服务端数据获取用户头像
            String url = object.getAvatar_md();
            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                BitmapUtils bitmapUtils = new BitmapUtils(mView.getContext());
                bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
                bitmapUtils.display(settingImageHead, url);
            }
        }

    }

}
