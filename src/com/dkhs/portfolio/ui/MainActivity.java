package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.MainFragment;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;
import com.dkhs.portfolio.ui.widget.kline.DisplayUtil;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity implements ITitleButtonListener {

    private DrawerLayout mDrawerLayout;
    // private View mRightMenu;
    private View mLeftMenu;

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtil.initDisplayUtil(this);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // mRightMenu = findViewById(R.id.menu_layout_right);
        mLeftMenu = findViewById(R.id.menu_layout_left);

        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MainFragment
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        mainFragment.setTitleClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mainFragment).commit();
        // 删除之前的activity,防止返回键退回到登录界面
        PortfolioApplication.getInstance().clearActivities();
        PortfolioApplication.getInstance().addActivity(this);
    }

    /**
     * 保存Fragment的状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mainFragment);
    }

    // @Override
    // public void rightButtonClick() {
    // if (null != mDrawerLayout && null != mRightMenu) {
    // mDrawerLayout.openDrawer(mRightMenu);
    // }
    //
    // }

    @Override
    public void leftButtonClick() {
        if (null != mDrawerLayout && null != mLeftMenu) {
            mDrawerLayout.openDrawer(mLeftMenu);
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mDrawerLayout.isDrawerOpen(mLeftMenu)) {
            mDrawerLayout.closeDrawer(mLeftMenu);
        }
        // if (mDrawerLayout.isDrawerOpen(mRightMenu)) {
        // mDrawerLayout.closeDrawer(mRightMenu);
        // }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LogUtils.i("================onDestroy()====================");
    }
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onResume(this);
	}
}
