package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.sharesdk.onekeyshare.OnekeyShare;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_friends);
        ViewUtils.inject(this);
        setTitle(R.string.invite_friends);
        invitingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdEngineImpl.getInvitingInfo(new SimpleParseHttpListener() {
                    @Override
                    public Class getClassType() {
                        return ShareBean.class;
                    }

                    @Override
                    protected void afterParseData(Object object) {

                        if(object!=null ){
                            invitingFriendAction((ShareBean)object);
                        }

                    }
                });
            }
        });


    }


    private void invitingFriendAction(ShareBean object) {


            /**
             *
             * "content": "领取免费流量，下载谁牛app，输入邀请码17350926,即可领取100M免费流量",
             "url": "https://www.dkhs.com/portfolio/wap/?invite_code=17350926",
             "code": "17350926",
             "img": "https://www.dkhs.com/static/portfolio/img/shuiniuwap/favicon.png",
             "title": "谁牛－免费流量跟踪牛股"
             */
            Context context = this;
            final OnekeyShare oks = new OnekeyShare();
            oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
            oks.setTitleUrl(object.getUrl());
            oks.setUrl(object.getUrl());
            oks.setTitle(object.getTitle());
            oks.setText(object.getContent());
            oks.setImageUrl(object.getImg());
//            oks.setFilePath(SHARE_IMAGE);
            oks.setSilent(false);

            oks.setShareFromQQAuthSupport(false);
            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();

            oks.show(context);


    }

}
