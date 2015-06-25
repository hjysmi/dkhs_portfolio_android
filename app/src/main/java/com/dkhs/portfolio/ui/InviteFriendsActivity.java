package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WebActivity
 * @Description TODO(邀请好友界面)
 * @date 2015-5-18 上午10:26:35
 */
public class InviteFriendsActivity extends ModelAcitivity {


    private String title;
    private String uri;
    @ViewInject(R.id.invitingBtn)
    private Button invitingBtn;

    private ShareBean mShareBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_friends);
        ViewUtils.inject(this);
        setTitle(R.string.invite_friends);
        invitingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                invitingFriendAction();

            }
        });

        getDataForNet(false);

        Button rightButton = getRightButton();

        rightButton.setText(R.string.history);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteFriendsActivity.this, InviteHistoryActivity.class));
            }
        });

    }

    private void invitingFriendAction() {

        if(mShareBean != null){
            invitingFriendAction(mShareBean);
        }else{
            getDataForNet(true);
        }
    }

    private void getDataForNet(final boolean response) {
        AdEngineImpl.getInvitingInfo(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return ShareBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                if (object != null) {
                    mShareBean= (ShareBean) object;
                    if(response) {
                        invitingFriendAction(mShareBean);
                    }
                }
            }
        }.setLoadingDialog(InviteFriendsActivity.this));
    }


    private void invitingFriendAction(ShareBean object) {

        Context context = this;
        final OnekeyShare oks = new OnekeyShare();
//          oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        oks.setTitleUrl(object.getUrl());
        oks.setUrl(object.getUrl());
        oks.setTitle(object.getTitle());
        oks.setText(object.getContent()+object.getUrl());
        oks.setImageUrl(object.getImg());
//            oks.setFilePath(SHARE_IMAGE);
        oks.setSilent(false);
        oks.setShareFromQQAuthSupport(false);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        oks.setSuccessText(getString(R.string.invite_success));
        oks.setCancelText(getString(R.string.invite_cancel));
        oks.setErrorText(getString(R.string.invite_err));
        oks.show(context);


    }

}
