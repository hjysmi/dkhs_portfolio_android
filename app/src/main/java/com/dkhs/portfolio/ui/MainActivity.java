/**
 * @Title NewMainActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.AppUpdateEngine;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.fragment.HomePageFragment;
import com.dkhs.portfolio.ui.fragment.MainMarketFragment;
import com.dkhs.portfolio.ui.fragment.MainOptionalFragment;
import com.dkhs.portfolio.ui.fragment.MainRewardFragment;
import com.dkhs.portfolio.ui.fragment.MenuItemFragment;
import com.dkhs.portfolio.ui.fragment.UserFragment;
import com.dkhs.portfolio.ui.fragment.VisiableLoadFragment;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.ui.messagecenter.MessageReceive;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import io.rong.imlib.model.Message;

/**
 * @author zjz
 * @version 2.0
 * @ClassName NewMainActivity
 * @Description 主界面
 * 对主界面碎片化管理，延迟加载没点击的界面
 * @date 2015-2-5 上午10:26:35
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String TAG_FRAGMENT_MENU = "MENU";
    private static final String TAG_FRAGMENT_A = "A";
    private static final String TAG_FRAGMENT_B = "B";
    private static final String TAG_FRAGMENT_C = "C";
    private static final String TAG_FRAGMENT_D = "D";
    private static final String TAG_FRAGMENT_E = "E";
    private static final String BOTTOM_TAB_INDEX = "bottom_bat_index";
    private static final String TOP_TAB_INDEX = "top_tab_index";

    private static final int INDEX_HOME_TAB = 0;
    private static final int INDEX_REWARD_TAB = 1;
    private static final int INDEX_MARKET_TAB = 2;
    private static final int INDEX_OPTIONAL_TAB = 3;
    private static final int INDEX_USERINFO_TAB = 4;

    private MessageHandler handler;
    private MenuItemFragment mMenuFragment;

    public Bundle mBundle;
    public ScrollViewPager mScrollViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        // 模拟堆栈管理activity

        PortfolioApplication.getInstance().addActivity(this);
        handler = new MessageHandler(this);
        setContentView(R.layout.activity_new_main);

        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        mMenuFragment = new MenuItemFragment();
        Bundle bunlde = new Bundle();
        mMenuFragment.setArguments(bunlde);
        t.replace(R.id.bottom_layout, mMenuFragment, TAG_FRAGMENT_MENU);

        t.commitAllowingStateLoss();
        initViewPager();
//        displayFragmentC();
        new AppUpdateEngine(mContext).checkVersion();
        handIntent();
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.person_setting_backgroud)));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.d(this.getClass().getSimpleName(), "onTrimMemory level:" + level);
    }

    @Override
    protected void onResume() {
        MessageManager.getInstance().connect();
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        handIntent();
    }

    private void handIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (null != intent.getParcelableExtra(MessageReceive.KEY_MESSAGE)) {
            Message message = intent.getParcelableExtra(MessageReceive.KEY_MESSAGE);
            handler.handleMessage(message);
        }else if(null != intent.getStringExtra("handlerUrl")){
           String handlerUrl= intent.getStringExtra("handlerUrl");
            handler.handleURL(handlerUrl);
        }

        int index = intent.getIntExtra("index", 0);

        mBundle = intent.getBundleExtra("arg");
        mMenuFragment.clickTabIndex(index);


    }

    public void showContentIndex(int index) {
        switch (index) {
            case MenuItemFragment.TABINDEX_1: {
//                displayFragmentA();
                mScrollViewPager.setCurrentItem(INDEX_OPTIONAL_TAB,false);
            }
            break;
            case MenuItemFragment.TABINDEX_2: {
//                displayFragmentB();
                mScrollViewPager.setCurrentItem(INDEX_MARKET_TAB,false);
            }
            break;
            case MenuItemFragment.TABINDEX_3: {
//                displayFragmentC();
                mScrollViewPager.setCurrentItem(INDEX_HOME_TAB,false);

            }
            break;
            case MenuItemFragment.TABINDEX_4: {
//                displayFragmentD();
                mScrollViewPager.setCurrentItem(INDEX_REWARD_TAB,false);
            }
            break;
            case MenuItemFragment.TABINDEX_5: {
//                displayFragmentE();
                mScrollViewPager.setCurrentItem(INDEX_USERINFO_TAB,false);
            }
            break;

            default:
                break;
        }
    }

    protected void displayFragmentA() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentA = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_A);
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
            ft.add(R.id.content_layout, fragmentA, TAG_FRAGMENT_A);
        }

        ft.commitAllowingStateLoss();
    }


    protected void displayFragmentB() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentB = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_B);
        if (null == fragmentB) {
            fragmentB = new MainMarketFragment();
        }
        hideAllFragment();
        if (null != fragmentB && fragmentB.isAdded()) { // if the fragment is already in container
            ft.show(fragmentB);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentB, TAG_FRAGMENT_B);
        }
        if (fragmentB instanceof VisiableLoadFragment) {
            ((VisiableLoadFragment) fragmentB).onViewShow();
        }

        ft.commitAllowingStateLoss();
    }

    protected void displayFragmentC() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentC = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_C);
        if (null == fragmentC) {
            fragmentC = new HomePageFragment();
        }
        hideAllFragment();
        if (null != fragmentC && fragmentC.isAdded()) { // if the fragment is already in container
            ft.show(fragmentC);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentC, TAG_FRAGMENT_C);
        }

        ft.commitAllowingStateLoss();
    }

    protected void displayFragmentD() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentD = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_D);
        if (null == fragmentD) {
            fragmentD = new MainRewardFragment();
        }
        hideAllFragment();
        if (null != fragmentD && fragmentD.isAdded()) { // if the fragment is already in container
            ft.show(fragmentD);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentD, TAG_FRAGMENT_D);
        }

        ft.commitAllowingStateLoss();
    }

    protected void displayFragmentE() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragmentE = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_E);
        if (null == fragmentE) {
            fragmentE = new UserFragment();
        }
        hideAllFragment();
        if (null != fragmentE && fragmentE.isAdded()) { // if the fragment is already in container
            ft.show(fragmentE);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentE, TAG_FRAGMENT_E);
        }

        ft.commitAllowingStateLoss();
    }


    private void hideAllFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment fragmentA = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_A);
        Fragment fragmentB = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_B);
        Fragment fragmentC = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_C);
        Fragment fragmentD = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_D);
        Fragment fragmentE = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_E);

        hideFragment(fragmentA, ft);
        hideFragment(fragmentB, ft);
        hideFragment(fragmentC, ft);
        hideFragment(fragmentD, ft);
        hideFragment(fragmentE, ft);
        ft.commitAllowingStateLoss();

        if (fragmentA instanceof VisiableLoadFragment) {
            ((VisiableLoadFragment) fragmentA).onViewHide();
        }
        if (fragmentB instanceof VisiableLoadFragment) {
            ((VisiableLoadFragment) fragmentB).onViewHide();
        }
    }


    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (null != fragment && fragment.isAdded()) {
            ft.hide(fragment);
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_click_once_more), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return true;
            } else {
                GlobalParams.clearUserInfo();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public static void gotoUserActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", 4);
        context.startActivity(intent);
    }


    public static void gotoShakeActivity(Context context) {
        Intent intent = new Intent(context, ShakeDetectorActivity.class);
        context.startActivity(intent);
    }

    public static void gotoOptionSymbols(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", 0);


        NewIntent newIntent = new NewIntent();
        newIntent.bundle.putInt("option_index", 0);
        intent.putExtra("arg", newIntent.bundle);
        context.startActivity(intent);
        BusProvider.getInstance().post(newIntent);

    }

    public static void gotoHostTopicsActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", 3);


        NewIntent newIntent = new NewIntent();
        newIntent.bundle.putInt("bbs_index", 1);
        intent.putExtra("arg", newIntent.bundle);
        context.startActivity(intent);
        BusProvider.getInstance().post(newIntent);
    }

    public static void gotoTopicsHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", 3);
        NewIntent newIntent = new NewIntent();
        newIntent.bundle.putInt("order_index", 1);
        intent.putExtra("arg", newIntent.bundle);
        context.startActivity(intent);
        BusProvider.getInstance().post(newIntent);
    }

//    public static void gotoCombinationRankingActivity(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("index", 1);
//        NewIntent newIntent = new NewIntent();
//        newIntent.bundle.putInt("fund_index", 2);
//        intent.putExtra("arg", newIntent.bundle);
//        context.startActivity(intent);
//        BusProvider.getInstance().post(newIntent);
//    }

//    public static void gotoFundManagerRanking(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("index", 1);
//        NewIntent newIntent = new NewIntent();
//        newIntent.bundle.putInt("fund_index", 0);
//        newIntent.bundle.putBoolean("fund_manager_ranking", true);
//        intent.putExtra("arg", newIntent.bundle);
//        context.startActivity(intent);
//        BusProvider.getInstance().post(newIntent);
//    }

//    public static void gotoFundsRanking(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("index", 1);
//
//
//        NewIntent newIntent = new NewIntent();
//        newIntent.bundle.putInt("fund_index", 0);
//        newIntent.bundle.putBoolean("fund_manager_ranking", false);
//        intent.putExtra("arg", newIntent.bundle);
//        context.startActivity(intent);
//        BusProvider.getInstance().post(newIntent);
//
//    }

    public static void gotoSHActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("index", 1);


        NewIntent newIntent = new NewIntent();
        newIntent.bundle.putInt("fund_index", 0);
        intent.putExtra("arg", newIntent.bundle);
        context.startActivity(intent);
        BusProvider.getInstance().post(newIntent);
    }

    public void   initViewPager(){
        mScrollViewPager = (ScrollViewPager) findViewById(R.id.content_layout);
        mScrollViewPager.setCanScroll(false);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(INDEX_HOME_TAB,new HomePageFragment());
        fragments.add(INDEX_REWARD_TAB,new MainRewardFragment());
        fragments.add(INDEX_MARKET_TAB,new MainMarketFragment());
        fragments.add(INDEX_OPTIONAL_TAB,new MainOptionalFragment());
        fragments.add(INDEX_USERINFO_TAB,new UserFragment());
        mScrollViewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(),fragments));
        mScrollViewPager.setOffscreenPageLimit(5);
    }

    class TabsPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        public TabsPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (fragments == null || fragments.size() == 0) ? null : fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mMenuFragment.clickTabIndex(mScrollViewPager.getCurrentItem());
        }
    }
}
