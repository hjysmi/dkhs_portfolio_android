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
import com.dkhs.portfolio.ui.fragment.MenuItemFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * @ClassName NewMainActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version 1.0
 */
public class NewMainActivity extends FragmentActivity {

    private MenuItemFragment mMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mMenuFragment = new MenuItemFragment();
            Bundle bunlde = new Bundle();
            // bunlde.putInt("content", mIndex);
            mMenuFragment.setArguments(bunlde);
            t.replace(R.id.bottom_layout, mMenuFragment);
            t.commit();

        } else {
            mMenuFragment = (MenuItemFragment) this.getSupportFragmentManager().findFragmentById(R.id.bottom_layout);
        }
    }
}
