package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.engine.Action1;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WebActivity
 * @Description TODO(邀请好友界面)
 * @date 2015-5-18 上午10:26:35
 */
public class InviteFriendsActivity extends ModelAcitivity {


    @ViewInject(R.id.invitingBtn)
    private Button invitingBtn;

    private ShareBean mShareBean;
    @ViewInject(R.id.iv_invite)
    private ImageView mInviteIV;
    @ViewInject(R.id.loadView)
    private View loadView;


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
        loadView.setVisibility(View.GONE);
        getDataForNet(false);

        TextView rightButton = getRightButton();

        rightButton.setText(R.string.history);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteFriendsActivity.this, InviteHistoryActivity.class));
            }
        });

        getInviteFriendsInfo();

    }

    private void getInviteFriendsInfo() {

            AdEngineImpl.getInvite(new Action1<AdBean>() {
                @Override
                public void call(AdBean adBean) {
                    if (adBean != null) {
                        updateInviteFriendsInfo(adBean);
                    }
                }
            });


    }

    private void updateInviteFriendsInfo(AdBean o) {

        if(o !=null ){

            if(o.getAds().size() > 0){
                AdBean.AdsEntity entity=o.getAds().get(0);


                ImageLoaderUtils.setImage(entity.getImage(), mInviteIV, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        loadView.setVisibility(View.GONE);
                        invitingBtn.setVisibility(View.VISIBLE);
                    }
                });
            }

        }


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


    private ShareBean shareBean;

    private void invitingFriendAction(ShareBean object) {
        shareBean=object;
            ImageLoaderUtils.loadImage(object.getImg(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    shareAction(null);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    ImageLoader loader = ImageLoader.getInstance();
                    shareAction(loader.getDiskCache().get(s).getPath());
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

    }

    private void shareAction(String path) {

        if(shareBean ==null ){
            return;
        }
        Context context = this;
        final OnekeyShare oks = new OnekeyShare();
//          oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        oks.setTitleUrl(shareBean.getUrl());
        oks.setUrl(shareBean.getUrl());
        oks.setTitle(shareBean.getTitle());
        oks.setText(shareBean.getContent());
        if(path !=null) {
            oks.setImagePath(path);
        }else {
            oks.setImageUrl(shareBean.getImg());
        }
//            oks.setFilePath(SHARE_IMAGE);
        oks.setSilent(false);
        oks.setShareFromQQAuthSupport(false);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        oks.setSuccessText(getString(R.string.invite_success));
        oks.setCancelText(getString(R.string.invite_cancel));
//        oks.setErrorText(getString(R.string.invite_err));
        oks.show(context);

    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_InviteFriends;
    }

}
