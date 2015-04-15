/**
 * @Title NewMainActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.ui.fragment.MainInfoFragment;
import com.dkhs.portfolio.ui.fragment.MainMarketFragment;
import com.dkhs.portfolio.ui.fragment.MainOptionalFragment;
import com.dkhs.portfolio.ui.fragment.MenuItemFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.fragment.UserFragment;
import com.dkhs.portfolio.utils.JsonUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @ClassName NewMainActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version 2.0
 */
public class NewMainActivity extends ModelAcitivity {

    private MenuItemFragment mMenuFragment;
    private Fragment mContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setTheme(android.R.style.Theme_Light_NoTitleBar);
        // PortfolioApplication.getInstance().addActivity(this);
        hideHead();
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_new_main);


        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mMenuFragment = new MenuItemFragment();
            Bundle bunlde = new Bundle();
            // bunlde.putInt("content", mIndex);
            mMenuFragment.setArguments(bunlde);
            t.replace(R.id.bottom_layout, mMenuFragment);
            fragmentA = new MainOptionalFragment();
            mContentFragment = fragmentA;
            t.replace(R.id.content_layout, mContentFragment);
            t.commit();

        } else {
            mMenuFragment = (MenuItemFragment) this.getSupportFragmentManager().findFragmentById(R.id.bottom_layout);
            mContentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content_layout);

        }
        fragmentB = new MainMarketFragment();
        fragmentC = new MainInfoFragment();
        fragmentD = new UserFragment();


        initRM();


    }

    /**
     * 设置消息通知的token
     */
    private void initRM() {

        LogUtils.e("initRM");
        UserEntity user = null;
        try {
            user = DbUtils.create(PortfolioApplication.getInstance())
                    .findFirst(UserEntity.class);

            if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {





                engine.getToken(user.getId()+"",user.getUsername(),user.getAvatar_xs(), new BasicHttpListener() {
                    @Override
                    public void onSuccess(String result) {



                        LogUtils.e(JsonUtil.format(result));


                    }

                            @Override
                            public void onFailure(int errCode, String errMsg) {
                                super.onFailure(errCode, errMsg);

                                LogUtils.e("onFailure "+errMsg);
                            }
                        }

                );
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


        String token = "e6AOw1Bpi/giPRFUdRHg68gDsJ/MCRXv0xD+lhrR5MvlzCXOyzUahpqsb+SYzpfB0a353iJKEyQ=";

        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Log.d("Connect:", "Login successfully.");
                    BusProvider.getInstance().post(new NewMessageEvent());
                    RongIM.getInstance().setReceiveMessageListener(listener);


                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    Log.d("Connect:", "Login failed.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showContentIndex(int index) {
        switch (index) {
            case MenuItemFragment.TABINDEX_1: {
                displayFragmentA();
                // Intent intent = new Intent(this, MainActivity.class);
                // startActivity(intent);
            }
                break;
            case MenuItemFragment.TABINDEX_2: {
                displayFragmentB();
            }
                break;
            case MenuItemFragment.TABINDEX_3: {
                displayFragmentC();

            }
                break;
            case MenuItemFragment.TABINDEX_4: {
                displayFragmentD();
            }
                break;

            default:
                break;
        }
    }

    private Fragment fragmentA;
    private Fragment fragmentB;
    private Fragment fragmentC;
    private Fragment fragmentD;

    protected void displayFragmentA() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null == fragmentA) {
            fragmentA = new MainOptionalFragment();
        }
        if (fragmentA.isAdded()) { // if the fragment is already in container
            ft.show(fragmentA);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentA, "A");
        }
        if (null != fragmentB && fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (null != fragmentC && fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (null != fragmentD && fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentB() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null == fragmentB) {
            fragmentB = new MainMarketFragment();
        }
        if (null != fragmentB && fragmentB.isAdded()) { // if the fragment is already in container
            ft.show(fragmentB);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentB, "B");
        }
        if (null != fragmentA && fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        if (null != fragmentC && fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (null != fragmentD && fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentC() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null == fragmentC) {
            fragmentC = new MainInfoFragment();
        }
        if (null != fragmentC && fragmentC.isAdded()) { // if the fragment is already in container
            ft.show(fragmentC);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentC, "C");
        }
        if (null != fragmentB && fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (null != fragmentA && fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        if (null != fragmentD && fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentD() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null == fragmentD) {
            fragmentD = new UserFragment();
        }
        if (null != fragmentD && fragmentD.isAdded()) { // if the fragment is already in container
            ft.show(fragmentD);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentD, "D");
        }
        if (null != fragmentB && fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (null != fragmentC && fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (null != fragmentA && fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        ft.commit();
    }
   final  RongIM.OnReceiveMessageListener listener = new RongIM.OnReceiveMessageListener() {
        @Override
        public void onReceived(RongIMClient.Message message, int left) {
            // 输出消息类型。
            Log.d("Receive:---","收到");
            NewMainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post(new NewMessageEvent());
                }
            });

        }
    };
}
