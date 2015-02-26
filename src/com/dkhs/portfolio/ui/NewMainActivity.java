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
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.ui.fragment.MainMarketFragment;
import com.dkhs.portfolio.ui.fragment.MainOptionalFragment;
import com.dkhs.portfolio.ui.fragment.MenuItemFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.fragment.UserFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

/**
 * @ClassName NewMainActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 上午10:26:35
 * @version 2.0
 */
public class NewMainActivity extends BaseActivity {

    private MenuItemFragment mMenuFragment;
    private Fragment mContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        PortfolioApplication.getInstance().addActivity(this);
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
        fragmentC = new TestFragment();
        fragmentD = new UserFragment();

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
    private TestFragment fragmentC;
    private Fragment fragmentD;

    protected void displayFragmentA() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null != fragmentA && fragmentA.isAdded()) { // if the fragment is already in container
            ft.show(fragmentA);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentA, "A");
        }
        if (fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentB() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentB.isAdded()) { // if the fragment is already in container
            ft.show(fragmentB);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentB, "B");
        }
        if (fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        if (fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentC() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentC.isAdded()) { // if the fragment is already in container
            ft.show(fragmentC);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentC, "C");
        }
        if (fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        if (fragmentD.isAdded()) {
            ft.hide(fragmentD);
        }
        ft.commit();
    }

    protected void displayFragmentD() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentD.isAdded()) { // if the fragment is already in container
            ft.show(fragmentD);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_layout, fragmentD, "D");
        }
        if (fragmentB.isAdded()) {
            ft.hide(fragmentB);
        }
        if (fragmentC.isAdded()) {
            ft.hide(fragmentC);
        }
        if (fragmentA.isAdded()) {
            ft.hide(fragmentA);
        }
        ft.commit();
    }

}
