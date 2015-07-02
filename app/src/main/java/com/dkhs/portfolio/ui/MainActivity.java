/**
 * @Title NewMainActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.MainInfoFragment;
import com.dkhs.portfolio.ui.fragment.MainMarketFragment;
import com.dkhs.portfolio.ui.fragment.MainOptionalFragment;
import com.dkhs.portfolio.ui.fragment.MenuItemFragment;
import com.dkhs.portfolio.ui.fragment.ShakesFragment;
import com.dkhs.portfolio.ui.fragment.UserFragment;
import com.dkhs.portfolio.ui.fragment.VisiableLoadFragment;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.ui.messagecenter.MessageReceive;
import com.dkhs.portfolio.ui.widget.UpdateDialog;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

import io.rong.imlib.model.Message;

/**
 * @author zjz
 * @version 2.0
 * @ClassName NewMainActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 上午10:26:35
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private MessageHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 模拟堆栈管理activity
        PortfolioApplication.getInstance().addActivity(this);
        // setTheme(android.R.style.Theme_Light_NoTitleBar);
        // PortfolioApplication.getInstance().addActivity(this);
        handler = new MessageHandler(this);
//        hideHead();
//        setSwipeBackEnable(false);
        setContentView(R.layout.activity_new_main);
        handIntent(getIntent());
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            MenuItemFragment mMenuFragment = new MenuItemFragment();
            Bundle bunlde = new Bundle();
            // bunlde.putInt("content", mIndex);
            mMenuFragment.setArguments(bunlde);
            t.replace(R.id.bottom_layout, mMenuFragment);

            t.commit();
            displayFragmentA();

        } else {
//            mMenuFragment = (MenuItemFragment) this.getSupportFragmentManager().findFragmentById(R.id.bottom_layout);
//            mContentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content_layout);

        }

        UserEngineImpl mUserEngineImpl = new UserEngineImpl();
        mUserEngineImpl.getAppVersion("portfolio_android", userInfoListener);


    }

    @Override
    protected void onResume() {
        MessageManager.getInstance().connect();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handIntent(intent);
        super.onNewIntent(intent);
    }

    private void handIntent(Intent intent) {

        if (intent == null) {
            return;
        }


        if (null != intent.getParcelableExtra(MessageReceive.KEY_MESSAGE)) {
            Message message = intent.getParcelableExtra(MessageReceive.KEY_MESSAGE);
            handler.handleMessage(message);
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
        fragmentA = getSupportFragmentManager().findFragmentByTag("A");
        if (null == fragmentA) {
            fragmentA = new MainOptionalFragment();
        }
        hideAllFragment();
        if (fragmentA.isAdded()) { // if the fragment is already in container
            ft.show(fragmentA);
            if (fragmentA instanceof VisiableLoadFragment) {
                ((VisiableLoadFragment) fragmentA).onViewShow();
            }

        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentA, "A");
        }

        ft.commit();
    }


    protected void displayFragmentB() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentB = getSupportFragmentManager().findFragmentByTag("B");
        if (null == fragmentB) {
            fragmentB = new MainMarketFragment();
        }
        hideAllFragment();
        if (null != fragmentB && fragmentB.isAdded()) { // if the fragment is already in container
            ft.show(fragmentB);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentB, "B");
        }
        if (fragmentB instanceof VisiableLoadFragment) {
            ((VisiableLoadFragment) fragmentB).onViewShow();
        }

        ft.commit();
    }

    protected void displayFragmentC() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentC = getSupportFragmentManager().findFragmentByTag("C");
        if (null == fragmentC) {
            fragmentC = new MainInfoFragment();
        }
        hideAllFragment();
        if (null != fragmentC && fragmentC.isAdded()) { // if the fragment is already in container
            ft.show(fragmentC);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentC, "C");
        }

        ft.commit();
    }

    protected void displayFragmentD() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentD = getSupportFragmentManager().findFragmentByTag("D");
        if (null == fragmentD) {
            fragmentD = new UserFragment();
        }
        hideAllFragment();
        if (null != fragmentD && fragmentD.isAdded()) { // if the fragment is already in container
            ft.show(fragmentD);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentD, "D");
        }

        ft.commit();
    }


    private void hideAllFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (null != fragmentA && fragmentA.isAdded()) {
            ft.hide(fragmentA);
            if (fragmentA instanceof VisiableLoadFragment) {
                ((VisiableLoadFragment) fragmentA).onViewHide();
            }
        }
        if (null != fragmentB && fragmentB.isAdded()) {
            ft.hide(fragmentB);
            if (fragmentB instanceof VisiableLoadFragment) {
                ((VisiableLoadFragment) fragmentB).onViewHide();
            }
        }
        if (null != fragmentC && fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (null != fragmentD && fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_click_once_more), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            }

        }

        GlobalParams.clearUserInfo();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }



    ParseHttpListener userInfoListener = new ParseHttpListener<AppBean>() {

        @Override
        protected AppBean parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(AppBean.class, jsonData);
        }

        @Override
        protected void afterParseData(AppBean object) {
            if (null != object) {
                try {
                    final AppBean bean = object;
                    PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                    String version = info.versionName;

                    if (object.isNewVersion(version)) {

                        if (!object.getVersion().equals(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_VERSIONY))) {
                            UpdateDialog alert = new UpdateDialog(mContext);
                            alert.showByAppBean(object);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
