package com.dkhs.portfolio.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.FlowPackageActivity;
import com.dkhs.portfolio.ui.InviteFriendsActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.MyDraftActivity;
import com.dkhs.portfolio.ui.ReplyActivity;
import com.dkhs.portfolio.ui.UserTopicsActivity;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.widget.FlexibleDividerDecoration;

/**
 * Created by zjz on 2015/7/22.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> implements
        FlexibleDividerDecoration.SizeProvider {


    private String[] titleTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.user_info_title);
    private int[] iconRes;

    private LayoutInflater mLayoutInflater;
    private Context mContext;

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_user_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(titleTexts[position]);
        holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes[position],
                0, 0, 0);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition(position);
            }
        });
        if (position == titleTexts.length - 1) {
            holder.tvTip.setVisibility(View.VISIBLE);
        } else {
            holder.tvTip.setVisibility(View.GONE);
        }
        if (position == 0) {

            if (mUnreadCount > 0) {
                holder.tvUnReadCount.setVisibility(View.VISIBLE);
                holder.tvUnReadCount.setText(mUnreadCount + "");
            } else {
                holder.tvUnReadCount.setVisibility(View.GONE);
            }

        } else {
            holder.tvUnReadCount.setVisibility(View.GONE);
        }
    }


    private int mUnreadCount;

    public void setUnreadCount(int count) {
        this.mUnreadCount = count;
        notifyItemChanged(0);
    }

    private void clickPosition(int position) {
        if (UIUtils.iStartLoginActivity(mContext)) {
            return;
        }
        switch (position) {

            case 0: //消息中心


                MessageManager.getInstance().startConversationList(mContext);

                break;
            case 1: //我的组合

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, MyCombinationActivity.class));


                break;

            case 2://我的话题

                UserEntity userEntity = UserEngineImpl.getUserEntity();
                UserTopicsActivity.starActivity(mContext, userEntity.getId() + "", userEntity.getUsername());
                break;
            case 3://我的回复
                UIUtils.startAnimationActivity((Activity) mContext, ReplyActivity.getIntent(mContext, GlobalParams.LOGIN_USER.getId() + ""));

                break;
            case 4://我的草稿
                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, MyDraftActivity.class));

                break;


            case 5://邀请好友

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, InviteFriendsActivity.class));


                break;


            case 6://流量兑换

                UIUtils.startAnimationActivity((Activity) mContext, new Intent(mContext, FlowPackageActivity.class));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return titleTexts.length;
    }

    @Override
    public int dividerSize(int position, RecyclerView parent) {
        switch (position) {
            case 0:
            case 1:
            case 4:
            case 6:
                return parent.getResources().getDimensionPixelOffset(R.dimen.combin_horSpacing);

            default:
                return -1;
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvUnReadCount;
        TextView tvTip;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_infotitle);
            tvUnReadCount = (TextView) view.findViewById(R.id.tv_unread_count);
            tvTip = (TextView) view.findViewById(R.id.tv_info_tip);
        }

    }
}
