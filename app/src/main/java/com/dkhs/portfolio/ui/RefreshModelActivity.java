/**
 * @Title RefreshModelActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-30 上午10:10:12
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * @ClassName RefreshModelActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-30 上午10:10:12
 * @version 1.0
 */
public class RefreshModelActivity extends ModelAcitivity {

    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        btnRefresh = getSecondRightButton();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);
        // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);

    }

    protected static final int MSG_WHAT_BEFORE_REQUEST = 99;
    protected static final int MSG_WHAT_AFTER_REQUEST = 97;
    Handler requestUiHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_BEFORE_REQUEST: {
                    rotateRefreshButton();
                }

                    break;
                case MSG_WHAT_AFTER_REQUEST: {
                    stopRefreshAnimation();
                }

                    break;

                default:
                    break;
            }
        };
    };

    private void rotateRefreshButton() {
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing), null,
                null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
    }

    private void stopRefreshAnimation() {
        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh), null,
                null, null);
    }

    public void setRefreshButtonListener(OnClickListener refreshListener) {
        btnRefresh.setOnClickListener(refreshListener);
    }

    public void startAnimaRefresh() {
        requestUiHandler.sendEmptyMessage(MSG_WHAT_BEFORE_REQUEST);
    }

    public void endAnimaRefresh() {
        requestUiHandler.sendEmptyMessage(MSG_WHAT_AFTER_REQUEST);
    }

}
