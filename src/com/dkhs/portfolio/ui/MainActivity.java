package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.MainFragment;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;

public class MainActivity extends FragmentActivity implements
		ITitleButtonListener {

	private DrawerLayout mDrawerLayout;
	private View mRightMenu;
	private View mLeftMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mRightMenu = findViewById(R.id.menu_layout_right);
		mLeftMenu = findViewById(R.id.menu_layout_left);
		replaceFragment();
	}

	private void replaceFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		MainFragment mainFragment = new MainFragment();
		mainFragment.setTitleClickListener(this);
		ft.replace(R.id.fragment_layout, mainFragment);
		ft.commit();
	}

	@Override
	public void rightButtonClick() {
		if (null != mDrawerLayout && null != mRightMenu) {
			mDrawerLayout.openDrawer(mRightMenu);
		}

	}

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
		if (mDrawerLayout.isDrawerOpen(mRightMenu)) {
			mDrawerLayout.closeDrawer(mRightMenu);
		}
	}

}
