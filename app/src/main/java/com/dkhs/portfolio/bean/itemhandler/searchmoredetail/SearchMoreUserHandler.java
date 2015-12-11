package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreUserHandler extends SimpleItemHandler<UserEntity> {

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_user_manager_detail;
    }

    @Override
    public void onBindView(final ViewHolder vh, final UserEntity user, int position) {
        final View itemView = vh.getConvertView();
        System.out.println("user=" + user.toString());
        vh.getTextView(R.id.tv_name).setText(user.getUsername());
        vh.getTextView(R.id.tv_follow).setText("关注: " + user.getFollowed_by_count());
        vh.getTextView(R.id.tv_fans).setText("粉丝: " + user.getFriends_count());
        ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        WaterMarkUtil.calWaterMarkImage(vh.getImageView(R.id.iv_water_mark), user.verified, user.verified_type == 0 ? WaterMarkUtil.TYPE_RED : WaterMarkUtil.TYPE_BLUE);
        final CheckBox cb_select_stock = vh.getCheckBox(R.id.cb_select_stock);
        if (user.isMe_follow()) {
            //我已关注
            cb_select_stock.setChecked(true);
        }
        cb_select_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //已关注
                    followAction(itemView, user);
                }else{
                    //没关注
                    unFollowAction(itemView,user);
                }

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView.getContext(), UserHomePageActivity.getIntent(itemView.getContext(), user.getUsername(), user.getId() + ""));
            }
        });
    }

    private void unFollowAction(View itemView, final UserEntity user) {
        final Context context = itemView.getContext();
        PromptManager.getAlertDialog(context).setTitle(R.string.tips).setMessage(context.getResources().getString(R.string.unfollow_alert_content))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unfollowListener.setLoadingDialog(context);
                        new UserEngineImpl().unfollow(user.getId() + "", unfollowListener);
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
    }

    private void followAction(View itemView, UserEntity user) {
        if (!UIUtils.iStartLoginActivity(itemView.getContext())) {
            followListener.setLoadingDialog(itemView.getContext());
            new UserEngineImpl().follow(user.getId() + "", followListener);
        }
    }

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
                PromptManager.showFollowToast();
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
                PromptManager.showDelFollowToast();

            }
        }
    };


}
